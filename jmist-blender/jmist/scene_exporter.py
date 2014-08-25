import bpy
import struct
import sys

import jmist.proto.mesh_pb2 as mesh_pb2
import jmist.proto.camera_pb2 as camera_pb2
import jmist.proto.light_pb2 as light_pb2
import jmist.proto.core_pb2 as core_pb2
import jmist.proto.scene_pb2 as scene_pb2

from io import BytesIO


def export_camera(bl_camera, camera):
  if bl_camera.data.type == 'PERSP':
    camera.type = camera_pb2.Camera.PINHOLE
    camera.pinhole_camera.angle_x = bl_camera.data.angle_x
    camera.pinhole_camera.angle_y = bl_camera.data.angle_y
  elif bl_camera.data.type == 'ORTHO':
    camera.type = camera_pb2.Camera.ORTHOGRAPHIC
    camera.orthographic_camera.scale_x = bl_camera.data.sensor_width
    camera.orthographic_camera.scale_y = bl_camera.data.sensor_height
  else:
    raise Exception('Unsupported camera type: %s' % bl_camera.data.type)

  del camera.world_to_view[:]
  camera.world_to_view.extend(
      [bl_camera.matrix_world[i][j]
        for i in range(0, 3)
        for j in range(0, 4)])


def export_lamp(bl_lamp, light):
  if bl_lamp.data.type == 'POINT':
    light.type = light_pb2.Light.POINT
    light.point_light.position.x = bl_lamp.location[0]
    light.point_light.position.y = bl_lamp.location[1]
    light.point_light.position.z = bl_lamp.location[2]
  else:
    raise Exception('Unsupported lamp type: %s' % bl_lamp.data.type)

  light.color.type = core_pb2.Color.RGB
  light.color.channels.extend(bl_lamp.data.color[:])

  light.energy = bl_lamp.data.energy


def export_mesh(bl_mesh, mesh):
  vertex_struct = struct.Struct("!3d")
  normal_struct = struct.Struct("!3d")
  vertex_index_struct = struct.Struct("!i")
  uv_struct = struct.Struct("!2d")
  poly_index_struct = struct.Struct("!i")
  poly_count_struct = struct.Struct("!i")

  out = BytesIO()
 
  for vertex in bl_mesh.vertices:
    out.write(vertex_struct.pack(*vertex.co))
    out.write(normal_struct.pack(*vertex.normal))
  
  coords_slice = mesh.format.vertices.coords.slice
  normals_slice = mesh.format.vertices.normals.slice
  coords_slice.offset = 0
  coords_slice.stride = vertex_struct.size + normal_struct.size
  coords_slice.count = len(bl_mesh.vertices)
  normals_slice.offset = coords_slice.offset + vertex_struct.size
  normals_slice.stride = coords_slice.stride
  normals_slice.count = coords_slice.count
   
  for loop_index in range(len(bl_mesh.loops)):
    loop = bl_mesh.loops[loop_index]
    out.write(vertex_index_struct.pack(loop.vertex_index))

  loops_slice = mesh.format.loops.indices.slice
  loops_slice.offset = (coords_slice.offset +
      coords_slice.stride * coords_slice.count)
  loops_slice.stride = vertex_index_struct.size
  loops_slice.count = len(bl_mesh.loops)
    
  for poly in bl_mesh.polygons:
    out.write(poly_index_struct.pack(poly.loop_start))
    out.write(poly_count_struct.pack(poly.loop_total))
    
  poly_index_slice = mesh.format.faces.polygons.index_slice
  poly_count_slice = mesh.format.faces.polygons.count_slice
  poly_index_slice.offset = (loops_slice.offset +
      loops_slice.stride * loops_slice.count)
  poly_index_slice.stride = poly_index_struct.size + poly_count_struct.size
  poly_index_slice.count = len(bl_mesh.polygons)
  poly_count_slice.offset = poly_index_slice.offset + poly_index_struct.size
  poly_count_slice.stride = poly_index_slice.stride
  poly_count_slice.count = poly_index_slice.count
  
  mesh.format.vertices.coords.format = mesh_pb2.DOUBLE_XYZ
  mesh.format.vertices.normals.format = mesh_pb2.DOUBLE_XYZ
  mesh.format.loops.indices.format = mesh_pb2.UINT32
  mesh.format.faces.polygons.index_format = mesh_pb2.UINT32
  mesh.format.faces.polygons.count_format = mesh_pb2.UINT32

  mesh.data = out.getvalue()


def export_object(bl_obj, obj):
  if bl_obj.type == 'MESH':
    obj.type = scene_pb2.Object.MESH
    export_mesh(bl_obj.data, obj.mesh_object)
  else:
    raise Exception('Unrecognized object type: %s' % bl_obj.type)

  del obj.world_to_local[:]
  obj.world_to_local.extend(
      [bl_obj.matrix_world[i][j]
        for i in range(0, 3)
        for j in range(0, 4)])


def export_scene(bl_scene, scene):
  export_camera(bl_scene.camera, scene.camera)
  for bl_obj in bl_scene.objects:
    if bl_obj.type == 'LAMP':
      export_lamp(bl_obj, scene.lights.add())
    elif bl_obj.type == 'CAMERA':
      pass
    else:
      export_object(bl_obj, scene.objects.add())
  
