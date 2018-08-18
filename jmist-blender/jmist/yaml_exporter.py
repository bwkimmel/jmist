import bpy
import mathutils
import yaml

from bpy.props import StringProperty, BoolProperty, FloatProperty, EnumProperty
from bpy.types import bpy_struct, bpy_prop_collection, Property
from bpy_extras.io_utils import ExportHelper


class YamlExporter(bpy.types.Operator, ExportHelper):
  bl_idname = "export_scene.yaml"
  bl_label = "Export YAML Scene"
  bl_options = {"PRESET"}

  filename_ext = ".yml"
  filter_glob = StringProperty(default="*.yml", options={"HIDDEN"})

  @property
  def check_extension(self):
    return True

  exportable_types = (bool, int, str, float, bpy.types.ID, bpy_prop_collection, bpy.types.MeshPolygon,
                      bpy.types.MeshVertex, bpy.types.MeshEdge, bpy.types.MeshLoop, mathutils.Vector)
  excluded_types = (bpy.types.Property, bpy.types.Struct, bpy.types.Function)
  depth = 0

  def _export_vector(self, dumper, vec):
    return dumper.represent_sequence('mathutils.Vector', vec.to_tuple())

  def _export_bpy_struct(self, dumper, struct):
    struct_id = struct.as_pointer()
#    print('%sExporting bpy_struct %s(%d)' % ('> ' * self.depth, type(struct), struct_id))
    if struct_id in self.represented_bpy_structs:
#      print('%sSeen this one before' % ('> ' * self.depth))
      return self.represented_bpy_structs[struct_id]
    self.represented_bpy_structs[struct_id] = dumper.represent_scalar('recurse', '...')
    if self.depth > 5:
#      print('%sReached max depth' % ('> ' * self.depth))
      return self.represented_bpy_structs[struct_id]
    self.depth += 1
#    print('%sEntering depth: %d' % ('> ' * self.depth, self.depth))
    mapping = {}
    for key in dir(struct):
      if key.startswith('_') or key.startswith('bl_') or key == 'base' or key == 'rna_type':
#        print('%sSkipping proeprty %s' % ('> ' * self.depth, key))
        continue
      try:
        value = getattr(struct, key)
#        print('%sGot value for key %s, type is %s' % ('> ' * self.depth, key, type(value)))
      except:
        value = '<ERROR>'
#        print('%sCould not get value for key %s' % ('> ' * self.depth, key))
      if value is not None and isinstance(value, self.exportable_types) and not isinstance(value, self.excluded_types):
#        print('%sMapping key %s' % ('> ' * self.depth, key))
        mapping[key] = value

    node = dumper.represent_mapping(
        'bpy_struct:%s.%s' % (type(struct).__module__, type(struct).__name__),
        mapping)
#    print('%sExiting depth: %d' % ('> ' * self.depth, self.depth))
    self.depth -= 1
    self.represented_bpy_structs[struct_id] = node
#    print('%sDone exporting bpy_struct %s(%d)' % ('> ' * self.depth, type(struct), struct_id))
    return node

  def _export_bpy_prop_collection(self, dumper, collection):
    sequence = [value for value in collection.values()
                if value is not None
                    and isinstance(value, self.exportable_types)
                    and not isinstance(value, self.excluded_types)]
    return dumper.represent_sequence('bpy_prop_collection', sequence)

  def execute(self, context):
    if not self.filepath:
      raise Exception("filepath not set")

    yaml.add_multi_representer(bpy_struct, self._export_bpy_struct)
    yaml.add_representer(bpy_prop_collection, self._export_bpy_prop_collection)
    yaml.add_representer(mathutils.Vector, self._export_vector)

    self.represented_bpy_structs = {}
    self.depth = 0

    with open(self.filepath, 'w') as f:
      yaml.dump(context.scene, f)

    return {"FINISHED"}

  @classmethod
  def register(cls):
    pass

  @classmethod
  def unregister(cls):
    pass


def menu_func(self, context):
  self.layout.operator(YamlExporter.bl_idname, text="YAML Scene (.yml)")

def register():
  bpy.types.INFO_MT_file_export.append(menu_func)
  bpy.utils.register_class(YamlExporter)

def unregister():
  bpy.types.INFO_MT_file_export.remove(menu_func)
  bpy.utils.unregister_class(YamlExporter)
