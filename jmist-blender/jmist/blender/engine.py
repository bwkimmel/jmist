import bpy
import subprocess
import sys
import mmap
#import traceback
#import time

from io import BytesIO
from array import array
import threading

from jmist.blender.message_reader import MessageReader
from jmist.blender.message_writer import MessageWriter
from jmist.blender.mesh_exporter import MeshExporter

import jmist.proto.mesh_pb2
import jmist.proto.render_engine_pb2


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
#      mesh = mesh_pb2.Mesh()
#      mesh.format.CopyFrom(exporter.export(bpy.data.objects[1].data, out))
#      mesh.data = out.getvalue()
#
      print("begin render")
      
      request = render_engine_pb2.RenderRequest()
      
      print("trace1")
  
#      scale = scene.render.resolution_percentage / 100.0
#
#      request.job.image_size.x = int(scene.render.resolution_x * scale)
#      request.job.image_size.y = int(scene.render.resolution_y * scale)
#      request.job.tile_size.x = scene.render.tile_x
#      request.job.tile_size.y = scene.render.tile_y
#      
      p = subprocess.Popen(['java', '-jar', 'jmist-pipe.jar'],
                           stdin=subprocess.PIPE, stdout=subprocess.PIPE)
      writer = MessageWriter(p.stdin)
      writer.write(request)
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
