import bpy

class JMistRenderSettings(bpy.types.PropertyGroup):
  @classmethod
  def register(cls):
    bpy.types.Scene.jmist = bpy.props.PointerProperty(
        name="JMist Render Settings",
        description="JMist render settings",
        type=cls)
    cls.debug = bpy.props.BoolProperty(
        name="Debug",
        description="Enable JVM Remote Debugging",
        default=False)
    cls.debug_port = bpy.props.IntProperty(
        name="Debug Port",
        description="Port number to listen on for remote debugger",
        default=8000,
        min=0,
        max=65535)
    cls.remote = bpy.props.BoolProperty(
        name="Remote",
        description="Render scene on a remote JDCP server",
        default=False)
    cls.remote_host = bpy.props.StringProperty(
        name="Host",
        description="Hostname of JDCP server to send job to",
        default="localhost")
    cls.remote_username = bpy.props.StringProperty(
        name="Username",
        description="Username to use to connect to remote JDCP server",
        default="guest")
    cls.remote_password = bpy.props.StringProperty(
        name="Password",
        description="Password to use to connect to remote JDCP server",
        default="",
        subtype='PASSWORD')

  @classmethod
  def unregister(cls):
    del bpy.types.Scene.jmist


def get_panels():
  types = bpy.types
  return (
      types.RENDER_PT_render,
      types.RENDER_PT_dimensions,
      types.RENDER_PT_performance,
      types.MATERIAL_PT_preview)


def draw_debug(self, context):
  scene = context.scene
  layout = self.layout
  if scene.render.engine == 'jmist_renderengine':
    from . import engine
    jscene = scene.jmist
    row = layout.row()
    row.prop(jscene, 'debug')
    sub = row.row()
    sub.active = jscene.debug
    sub.prop(jscene, 'debug_port')
    row = layout.row()
    row.prop(jscene, 'remote')
    sub = layout.row()
    sub.active = jscene.remote
    sub.prop(jscene, 'remote_host')
    sub = layout.row()
    sub.active = jscene.remote
    sub.prop(jscene, 'remote_username')
    sub = layout.row()
    sub.active = jscene.remote
    sub.prop(jscene, 'remote_password')


def register():
  bpy.types.RENDER_PT_render.append(draw_debug)
  for panel in get_panels():
    panel.COMPAT_ENGINES.add('jmist_renderengine')
  bpy.utils.register_class(JMistRenderSettings)


def unregister():
  bpy.types.RENDER_PT_render.remove(draw_debug)
  for panel in get_panels():
    panel.COMPAT_ENGINES.remove('jmist_renderengine')
  bpy.utils.unregister_class(JMistRenderSettings)
