bl_info = {
  "name": "JMist Render Engine",
  "category": "Render"
}

import bpy
from jmist.blender.engine import JmistRenderEngine

 
def register():
  bpy.utils.register_class(JmistRenderEngine)


def unregister():
  bpy.utils.unregister_class(JmistRenderEngine)
  

if __name__ == "__main__":
  register()
