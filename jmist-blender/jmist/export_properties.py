import bpy

from bpy.props import StringProperty, BoolProperty, FloatProperty, EnumProperty
from bpy_extras.io_utils import ExportHelper

from google.protobuf import text_format

from jmist.proto import scene_pb2
from jmist.scene_exporter import export_scene



class JMistExporter(bpy.types.Operator, ExportHelper):
  bl_idname = "export_scene.jmist"
  bl_label = "Export JMist Scene"
  bl_options = {"PRESET"}

  filename_ext = ".jmist"
  filter_glob = StringProperty(default="*.jmist", options={"HIDDEN"})

  export_as_ascii = BoolProperty(
    name="Export as ASCII",
    description="Export file in a human-readable form.",
    default=False,
  )

  @property
  def check_extension(self):
    return True

  def execute(self, context):
    if not self.filepath:
      raise Exception("filepath not set")

    scene = scene_pb2.Scene()
    export_scene(context.scene, scene)

    with open(self.filepath, 'w') as f:
      if self.export_as_ascii:
        text_format.PrintMessage(scene, f)
      else:
        f.write(scene.SerializeToString())

    return {"FINISHED"}

  @classmethod
  def register(cls):
    pass

  @classmethod
  def unregister(cls):
    pass


def menu_func(self, context):
  self.layout.operator(JMistExporter.bl_idname, text="JMist Scene (.jmist)")

def register():
  bpy.types.INFO_MT_file_export.append(menu_func)
  # bpy.utils.register_class(JMistExporter)

def unregister():
  bpy.types.INFO_MT_file_export.remove(menu_func)
  # bpy.utils.unregister_class(JMistExporter)
