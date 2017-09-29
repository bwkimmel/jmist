import bpy
import struct
import sys

from jmist.proto import mesh_pb2
from jmist.proto import camera_pb2
from jmist.proto import light_pb2
from jmist.proto import core_pb2
from jmist.proto import scene_pb2

from io import BytesIO


class _Context(object):
  material_index = {}


def export_camera(bl_camera, camera):
  if bl_camera.data.type == 'PERSP':
    camera.pinhole_camera.angle_x = bl_camera.data.angle_x
    camera.pinhole_camera.angle_y = bl_camera.data.angle_y
  elif bl_camera.data.type == 'ORTHO':
    camera.orthographic_camera.scale_x = bl_camera.data.sensor_width
    camera.orthographic_camera.scale_y = bl_camera.data.sensor_height
  else:
    raise Exception('Unsupported camera type: %s' % bl_camera.data.type)

  def multiplier(i, j):
    return -1 if j == 1 else 1;

  del camera.world_to_view[:]
  camera.world_to_view.extend(
      [bl_camera.matrix_world[i][j] * multiplier(i, j)
        for i in range(0, 3)
        for j in range(0, 4)])


def export_lamp(bl_lamp, light):
  if bl_lamp.data.type == 'POINT':
    light.point_light.position.x = bl_lamp.location[0]
    light.point_light.position.y = bl_lamp.location[1]
    light.point_light.position.z = bl_lamp.location[2]
  elif bl_lamp.data.type == 'AREA':
    light.area_light.center.x = bl_lamp.location[0]
    light.area_light.center.y = bl_lamp.location[1]
    light.area_light.center.z = bl_lamp.location[2]
    light.area_light.size_u = bl_lamp.data.size
    light.area_light.size_v = (bl_lamp.data.size_y
        if bl_lamp.data.shape == 'RECTANGLE'
        else bl_lamp.data.size)
    light.area_light.u.x = bl_lamp.matrix_world[0][0]
    light.area_light.u.y = bl_lamp.matrix_world[1][0]
    light.area_light.u.z = bl_lamp.matrix_world[2][0]
    light.area_light.v.x = -bl_lamp.matrix_world[0][1]
    light.area_light.v.y = -bl_lamp.matrix_world[1][1]
    light.area_light.v.z = -bl_lamp.matrix_world[2][1]
  elif bl_lamp.data.type == 'SUN':
    light.directional_light.direction.x = bl_lamp.matrix_world[0][2]
    light.directional_light.direction.y = bl_lamp.matrix_world[1][2]
    light.directional_light.direction.z = bl_lamp.matrix_world[2][2]
  else:
    raise Exception('Unsupported lamp type: %s' % bl_lamp.data.type)

  light.color.type = core_pb2.Color.RGB
  light.color.channels.extend(bl_lamp.data.color[:])

  light.energy = bl_lamp.data.energy * 1000
  light.shadow = bl_lamp.data.use_shadow


def export_mesh(bl_mesh, mesh, context):
  vertices = mesh.format.vertices
  vertices.coords.format = mesh_pb2.VectorSlice.DOUBLE_XYZ
  vertices.normals.format = mesh_pb2.VectorSlice.DOUBLE_XYZ

  loops = mesh.format.loops
  loops.indices.format = mesh_pb2.IndexSlice.UINT32
  loops.normals.format = mesh_pb2.VectorSlice.DOUBLE_XYZ

  faces = mesh.format.faces;
  faces.loop_start.format = mesh_pb2.IndexSlice.UINT32
  faces.loop_count.format = mesh_pb2.IndexSlice.UINT8
  faces.materials.format = mesh_pb2.IndexSlice.UINT16

  vertex_struct = struct.Struct("!3d")
  normal_struct = struct.Struct("!3d")
  vertex_index_struct = struct.Struct("!i")
  uv_struct = struct.Struct("!2d")
  poly_index_struct = struct.Struct("!i")
  poly_count_struct = struct.Struct("!B")
  materials_struct = struct.Struct("!H")
  
  out = BytesIO()

  vertices.offset = out.tell()
  vertices.stride = vertex_struct.size + normal_struct.size
  vertices.count = len(bl_mesh.vertices)
  vertices.coords.offset = 0
  vertices.normals.offset = vertex_struct.size
  for vertex in bl_mesh.vertices:
    out.write(vertex_struct.pack(*vertex.co))
    out.write(normal_struct.pack(*vertex.normal))
  
  bl_mesh.calc_normals_split()
  loops.offset = out.tell()
  loops.stride = vertex_index_struct.size + normal_struct.size
  loops.count = len(bl_mesh.loops)
  loops.indices.offset = 0
  loops.normals.offset = vertex_index_struct.size
  for loop in bl_mesh.loops:
    out.write(vertex_index_struct.pack(loop.vertex_index))
    out.write(normal_struct.pack(*loop.normal))
  bl_mesh.free_normals_split()
  
  faces.offset = out.tell()
  faces.stride = poly_index_struct.size + poly_count_struct.size + materials_struct.size
  faces.count = len(bl_mesh.polygons)
  faces.loop_start.offset = 0
  faces.loop_count.offset = poly_index_struct.size
  faces.materials.offset = poly_index_struct.size + poly_count_struct.size
  for poly in bl_mesh.polygons:
    out.write(poly_index_struct.pack(poly.loop_start))
    out.write(poly_count_struct.pack(poly.loop_total))
    material_index = context.material_index[
                         bl_mesh.materials[poly.material_index].name]
    out.write(materials_struct.pack(material_index))

  mesh.data = out.getvalue()


def export_object(bl_obj, obj, bl_scene, context):
  if bl_obj.type == 'MESH':
    export_mesh(bl_obj.to_mesh(scene=bl_scene,
                               apply_modifiers=True,
                               settings='RENDER'),
                obj.mesh_object,
                context)
  else:
    raise Exception('Unrecognized object type: %s' % bl_obj.type)

  del obj.world_to_local[:]
  obj.world_to_local.extend(
      [bl_obj.matrix_world[i][j]
        for i in range(0, 3)
        for j in range(0, 4)])


def export_material(bl_material, material):
  material.lambertian_material.reflectance.type = core_pb2.Color.RGB
  material.lambertian_material.reflectance.channels.extend(
      bl_material.diffuse_color[:])


def export_scene(bl_scene, scene):
  context = _Context()
  export_camera(bl_scene.camera, scene.camera)
  for bl_material in bpy.data.materials:
    context.material_index[bl_material.name] = len(scene.materials)
    export_material(bl_material, scene.materials.add())
  for bl_obj in bl_scene.objects:
    if bl_obj.is_visible(bl_scene):
      if bl_obj.type == 'LAMP':
        export_lamp(bl_obj, scene.lights.add())
      elif bl_obj.type == 'CAMERA':
        pass
      elif bl_obj.type == 'EMPTY':
        pass
      else:
        export_object(bl_obj, scene.objects.add(), bl_scene=bl_scene,
                      context=context)
  
