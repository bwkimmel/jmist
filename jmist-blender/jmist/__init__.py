bl_info = {
  "name": "JMist Render Engine",
  "category": "Render"
}

# To support reload properly, try to access a package var,
# if it's there, reload everything
if "bpy" in locals():
    import imp
    imp.reload(blender)
else:
    import jmist.blender


import bpy
from jmist.blender.engine import JmistRenderEngine

 
def register():
  bpy.utils.register_class(JmistRenderEngine)


def unregister():
  bpy.utils.unregister_class(JmistRenderEngine)
  

if __name__ == "__main__":
  register()
