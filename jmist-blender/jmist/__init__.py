bl_info = {
  "name": "JMist Render Engine",
  "category": "Render"
}

# To support reload properly, try to access a package var,
# if it's there, reload everything
if "bpy" in locals():
  import imp
  imp.reload(engine)
  imp.reload(scene_exporter)
  imp.reload(message_reader)
  imp.reload(message_writer)
  imp.reload(properties)
else:
  from . import engine
  from . import scene_exporter
  from . import message_reader
  from . import message_writer
  from . import properties

import bpy

def register():
  properties.register()
  bpy.utils.register_class(engine.JmistRenderEngine)

def unregister():
  properties.unregister()
  bpy.utils.unregister_class(engine.JmistRenderEngine)
