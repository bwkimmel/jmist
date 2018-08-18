bl_info = {
  "name": "JMist Render Engine",
  "category": "Render",
  "blender": (2, 80, 0)
}

# To support reload properly, try to access a package var,
# if it's there, reload everything
if "bpy" in locals():
  import imp
  imp.reload(engine)
  imp.reload(scene_exporter)
  imp.reload(message_reader)
  imp.reload(message_writer)
  imp.reload(export_properties)
  imp.reload(render_properties)
  imp.reload(yaml_exporter)
else:
  from . import engine
  from . import scene_exporter
  from . import message_reader
  from . import message_writer
  from . import export_properties
  from . import render_properties
  from . import yaml_exporter

import bpy

def register():
  export_properties.register()
  render_properties.register()
  yaml_exporter.register()
  bpy.utils.register_class(engine.JmistRenderEngine)

def unregister():
  export_properties.unregister()
  render_properties.unregister()
  yaml_exporter.unregister()
  bpy.utils.unregister_class(engine.JmistRenderEngine)
