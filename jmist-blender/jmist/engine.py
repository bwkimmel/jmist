import bpy
import subprocess
import sys
import traceback
import os
#import time

from io import BytesIO
from array import array
import threading

from jmist.message_reader import MessageReader
from jmist.message_writer import MessageWriter
from jmist.scene_exporter import export_scene

import mesh_pb2
import render_pb2
import render_engine_pb2


class JmistRenderEngine(bpy.types.RenderEngine):
  bl_idname = "jmist_renderengine"
  bl_label = "JMist Render Engine"
  bl_use_shading_nodes = True

  def __init__(self):
    self._lock = threading.Lock()

  def render(self, scene):

    try:

      request = render_engine_pb2.RenderRequest()
      request.threads = scene.render.threads

      scale = scene.render.resolution_percentage / 100.0

      request.job.image_size.x = int(scene.render.resolution_x * scale)
      request.job.image_size.y = int(scene.render.resolution_y * scale)
      request.job.tile_size.x = scene.render.tile_x
      request.job.tile_size.y = scene.render.tile_y

      request.job.color_model.type = render_pb2.ColorModel.RGB

      export_scene(scene, request.job.scene)
      args = ['java', '-server', '-XX:+AggressiveOpts', '-XX:+UseLargePages',
              '-XX:+UseFastAccessorMethods', '-XX:+UseBiasedLocking']
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
            return {"CANCELLED"}

          if callback.progress >= 0.0:
            self.update_progress(callback.progress)

          if callback.done:
            return {"FINISHED"}

          if callback.error != '':
            raise Exception('JMist Error: %s' % callback.error)

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

  def isCancelled(self):
    return self.test_break()

  def drawTile(self, rendered):
    if rendered.HasField('tile'):
      tile_result = self.begin_result(rendered.tile.position.x,
                                      rendered.tile.position.y,
                                      rendered.tile.size.x,
                                      rendered.tile.size.y)

      layer = tile_result.layers[0]
      rect = array('d')
      rect.fromstring(rendered.data)

      if (rendered.format ==
          render_engine_pb2.RenderCallback.DrawTile.DOUBLE_RGBA):
        layer.rect = [rect[n:(n+4)] for n in range(0, len(rect), 4)]
      else:
        raise Exception('Unrecognized pixel format: %s' % rendered.format)

      self.end_result(tile_result)
