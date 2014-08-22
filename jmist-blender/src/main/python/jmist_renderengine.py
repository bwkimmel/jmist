bl_info = {
  "name": "JMist Render Engine (Thrift)",
  "category": "Render"
}

import subprocess
import sys
import bpy
import mmap
import traceback
import time

from io import BytesIO
from array import array
import threading

from Mesh_pb2 import *
from Render_pb2 import *
from Scene_pb2 import *
from Core_pb2 import *
from RenderEngine_pb2 import *

import struct

# Read a message from Java's writeDelimitedTo:
import google.protobuf.internal.decoder as decoder
# Output a message to be read with Java's parseDelimitedFrom
import google.protobuf.internal.encoder as encoder


def serialize_delimited(message):
  out = message.SerializeToString()
  out = encoder._VarintBytes(len(out)) + out
  return out
  

class MessageReader:
  def __init__(self, message_factory, input):
    self.message_factory = message_factory
    self.input = input
    self.buffer = bytes()
    self.min_buffer_size = 10  # This must be enough to hold a varint.
  
  def _requireBytes(self, total_bytes):
    bytes_needed = total_bytes - len(self.buffer)
    while bytes_needed > 0:
      bytes_in = self.input.read(bytes_needed)
      bytes_needed = bytes_needed - len(bytes_in)
      self.buffer = self.buffer + bytes_in
    
  def read(self):
    if not self._requireBytes(self.min_buffer_size) and self.buffer == 0:
      return None
    (size, position) = decoder._DecodeVarint(self.buffer, 0)

    if not self._requireBytes(position + size):
      raise Exception('Unexpected end of file.')
    message = self.message_factory()
    message.ParseFromString(self.buffer[position:position+size])
    
    # It is possible that in reading enough bytes to parse the message length,
    # we may have read all the way into the next message.  So make sure we
    # preserve those leading bytes of the next message, if any.
    self.buffer = self.buffer[position+size:]
      
    return message
    
    



    


class MeshExporter:
  
  vertex_struct = struct.Struct("!3d")
  normal_struct = struct.Struct("!3d")
  vertex_index_struct = struct.Struct("!i")
  uv_struct = struct.Struct("!2d")
  poly_index_struct = struct.Struct("!i")
  poly_count_struct = struct.Struct("!i")
  
  def export(self, mesh, out):
    format = MeshFormat()
 
    for vertex in mesh.vertices:
      out.write(self.vertex_struct.pack(*vertex.co))
      out.write(self.normal_struct.pack(*vertex.normal))
    
    format.vertices.coords.slice.offset = 0
    format.vertices.coords.slice.stride = (
        self.vertex_struct.size + self.normal_struct.size)
    format.vertices.coords.slice.count = len(mesh.vertices)
    format.vertices.normals.slice.offset = (
        format.vertices.coords.slice.offset + self.vertex_struct.size)
    format.vertices.normals.slice.stride = format.vertices.coords.slice.stride
    format.vertices.normals.slice.count = format.vertices.coords.slice.count
     
    for loop_index in range(len(mesh.loops)):
      loop = mesh.loops[loop_index]
      out.write(self.vertex_index_struct.pack(loop.vertex_index))

    format.loops.indices.slice.offset = (format.vertices.coords.slice.offset +
        format.vertices.coords.slice.stride * format.vertices.coords.slice.count)
    format.loops.indices.slice.stride = self.vertex_index_struct.size
    format.loops.indices.slice.count = len(mesh.loops)
      
    for poly in mesh.polygons:
      out.write(self.poly_index_struct.pack(poly.loop_start))
      out.write(self.poly_count_struct.pack(poly.loop_total))
      
    format.faces.polygons.index_slice.offset = (format.loops.indices.slice.offset +
        format.loops.indices.slice.stride * format.loops.indices.slice.count)
    format.faces.polygons.index_slice.stride = (
        self.poly_index_struct.size + self.poly_count_struct.size)
    format.faces.polygons.index_slice.count = len(mesh.polygons)
    format.faces.polygons.count_slice.offset = (
        format.faces.polygons.index_slice.offset + self.poly_index_struct.size)
    format.faces.polygons.count_slice.stride = (
        format.faces.polygons.index_slice.stride)
    format.faces.polygons.count_slice.count = (
        format.faces.polygons.index_slice.count)
    
    format.vertices.coords.format = DOUBLE_XYZ
    format.vertices.normals.format = DOUBLE_XYZ
    format.loops.indices.format = UINT32
    format.faces.polygons.index_format = UINT32
    format.faces.polygons.count_format = UINT32
    return format
  
  
class JmistRenderEngine(bpy.types.RenderEngine):
  bl_idname = "jmist_renderengine"
  bl_label = "JMist Render Engine"
 
  def __init__(self):
    self._lock = threading.Lock()
    
  def render(self, scene):
    
    try:
      
      print("exporting")
      
#      exporter = MeshExporter()
#      out = BytesIO()
#      mesh = Mesh()
#      mesh.format.CopyFrom(exporter.export(bpy.data.objects[1].data, out))
#      mesh.data = out.getvalue()
#
      print("begin render")
      
      request = RenderRequest()
      
      print("trace1")
  
#      scale = scene.render.resolution_percentage / 100.0
#
#      request.job.image_size.x = int(scene.render.resolution_x * scale)
#      request.job.image_size.y = int(scene.render.resolution_y * scale)
#      request.job.tile_size.x = scene.render.tile_x
#      request.job.tile_size.y = scene.render.tile_y
#      
      p = subprocess.Popen(['java', '-jar', 'jmist-blender-0.1.1.jar'],
                           stdin=subprocess.PIPE, stdout=subprocess.PIPE)
      p.stdin.write(serialize_delimited(request))
      p.stdin.flush()
      p.stdin.close()
      
      reader = MessageReader(RenderCallback, p.stdout)
      while not self.isCancelled():
        callback = reader.read()
        if not callback:
          self.done()
          return {"CANCELLED"}

        if callback.HasField('progress'):
          self.update_progress(callback.progress)

        if callback.done:
          self.done()
          return {"FINISHED"}
        
        if callback.HasField('set_transfer_file'):
          self.setTransferFile(callback.set_transfer_file.filename,
                               callback.set_transfer_file.size)
          
        if callback.HasField('draw_tile'):
          self.drawTile(callback.draw_tile)
      
    except Exception as e:
      return {"ERROR: %s" % str(e)}

    return {"FINISHED"}
  
  def done(self):
    if self.transfer_file:
      self.transfer_file.close()
      del self.transfer_file
    if self.transfer:
      self.transfer.close()
      del self.transfer
    
  def isCancelled(self):
    return self.test_break()
  
  def setTransferFile(self, filename, size):
    print("Transfer file: name=%s, size=%d" % (filename, size))
    self.transfer_file = open(filename, "rb")
    self.transfer = mmap.mmap(self.transfer_file.fileno(), size,
                              access=mmap.ACCESS_READ)
 
  def drawTile(self, rendered):
    if rendered.HasField('tile'):
      tile_result = self.begin_result(rendered.tile.position.x,
                                      rendered.tile.position.y,
                                      rendered.tile.size.x,
                                      rendered.tile.size.y)

      layer = tile_result.layers[0]
      rect = array('d')
      if rendered.data:
        rect.fromstring(rendered.data)
      else:
        with self._lock:
          self.transfer.seek(rendered.offset)
          data = self.transfer.read(rendered.length)
          rect.fromstring(data)
  
      if rendered.format == DOUBLE_RGBA:
        layer.rect = [rect[n:(n+4)] for n in range(0, len(rect), 4)]
      else:
        raise Exception('Unrecognized pixel format: %s' % rendered.format)

 
def register():
  bpy.utils.register_class(JmistRenderEngine)


def unregister():
  bpy.utils.unregister_class(JmistRenderEngine)
  

if __name__ == "__main__":
#  register()
  engine = JmistRenderEngine()
  engine.render(None)
