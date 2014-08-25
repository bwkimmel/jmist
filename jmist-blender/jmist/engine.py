import bpy
import subprocess
import sys
import mmap
import traceback
import os
#import time

from io import BytesIO
from array import array
import threading

from jmist.message_reader import MessageReader
from jmist.message_writer import MessageWriter
from jmist.scene_exporter import export_scene

import jmist.proto.mesh_pb2 as mesh_pb2
import jmist.proto.render_pb2 as render_pb2
import jmist.proto.render_engine_pb2 as render_engine_pb2


class JmistRenderEngine(bpy.types.RenderEngine):
  bl_idname = "jmist_renderengine"
  bl_label = "JMist Render Engine"
  bl_use_shading_nodes = True

  def __init__(self):
    self._lock = threading.Lock()

  def render(self, scene):

    try:

      request = render_engine_pb2.RenderRequest()

      scale = scene.render.resolution_percentage / 100.0

      request.job.image_size.x = int(scene.render.resolution_x * scale)
      request.job.image_size.y = int(scene.render.resolution_y * scale)
      request.job.tile_size.x = scene.render.tile_x
      request.job.tile_size.y = scene.render.tile_y

      request.job.color_model.type = render_pb2.ColorModel.RGB

      export_scene(scene, request.job.scene)

      args = ['java']
      if scene.jmist.debug:
        port = scene.jmist.debug_port
        args.extend(
            ['-Xdebug',
             '-Xrunjdwp:server=y,transport=dt_socket,address=%d,suspend=y'
                 % port])
      jar_file = os.path.join(os.path.dirname(__file__),
                              'jmist-pipe.jar')
      args.extend(['-jar', jar_file])
      print(args)
      with subprocess.Popen(args,
                            stdin=subprocess.PIPE,
                            stdout=subprocess.PIPE) as p:
        writer = MessageWriter(p.stdin)
        writer.write(request)
        p.stdin.flush()
        p.stdin.close()

        reader = MessageReader(render_engine_pb2.RenderCallback, p.stdout)
        while not self.isCancelled():
          callback = reader.read()
          if not callback:
            self.done()
            return {"CANCELLED"}

          if callback.HasField('progress'):
            self.update_progress(callback.progress)

          if callback.done:
            self.done()
            return {"FINISHED2"}

          if callback.HasField('error'):
            raise Exception('JMist Error: %s' % callback.error)

          if callback.HasField('set_transfer_file'):
            self.setTransferFile(callback.set_transfer_file.filename,
                                 callback.set_transfer_file.size)

          if callback.HasField('draw_tile'):
            self.drawTile(callback.draw_tile)

    except:
      print("Exception in user code:")
      print('-'*60)
      traceback.print_exc(file=sys.stdout)
      print('-'*60)
      sys.stdout.flush()
      return {"ERROR"}

    return {"FALLTHROUGH"}

  def done(self):
    if hasattr(self, 'transfer_file') and self.transfer_file:
      self.transfer_file.close()
      del self.transfer_file
    if hasattr(self, 'transfer') and self.transfer:
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

      if (rendered.format ==
          render_engine_pb2.RenderCallback.DrawTile.DOUBLE_RGBA):
        layer.rect = [rect[n:(n+4)] for n in range(0, len(rect), 4)]
      else:
        raise Exception('Unrecognized pixel format: %s' % rendered.format)

      self.end_result(tile_result)
