import bpy
import struct
import sys

import jmist.proto.mesh_pb2 as mesh_pb2


class MeshExporter:
  
  vertex_struct = struct.Struct("!3d")
  normal_struct = struct.Struct("!3d")
  vertex_index_struct = struct.Struct("!i")
  uv_struct = struct.Struct("!2d")
  poly_index_struct = struct.Struct("!i")
  poly_count_struct = struct.Struct("!i")
  
  def export(self, mesh, out):
    format = mesh_pb2.MeshFormat()
 
    for vertex in mesh.vertices:
      out.write(self.vertex_struct.pack(*vertex.co))
      out.write(self.normal_struct.pack(*vertex.normal))
    
    format.vertices.coords.slice.offset = 0
    format.vertices.coords.slice.stride = (
        self.vertex_struct.size + self.normal_struct.size)
    format.vertices.coords.slice.count = len(mesh.vertices)
    format.vertices.normals.slice.offset = (
        format.vertices.coords.slice.offset + self.vertex_struct.size)
    format.vertices.normals.slice.stride = format.vertices.coords.slice.stride
    format.vertices.normals.slice.count = format.vertices.coords.slice.count
     
    for loop_index in range(len(mesh.loops)):
      loop = mesh.loops[loop_index]
      out.write(self.vertex_index_struct.pack(loop.vertex_index))

    format.loops.indices.slice.offset = (format.vertices.coords.slice.offset +
        format.vertices.coords.slice.stride * format.vertices.coords.slice.count)
    format.loops.indices.slice.stride = self.vertex_index_struct.size
    format.loops.indices.slice.count = len(mesh.loops)
      
    for poly in mesh.polygons:
      out.write(self.poly_index_struct.pack(poly.loop_start))
      out.write(self.poly_count_struct.pack(poly.loop_total))
      
    format.faces.polygons.index_slice.offset = (format.loops.indices.slice.offset +
        format.loops.indices.slice.stride * format.loops.indices.slice.count)
    format.faces.polygons.index_slice.stride = (
        self.poly_index_struct.size + self.poly_count_struct.size)
    format.faces.polygons.index_slice.count = len(mesh.polygons)
    format.faces.polygons.count_slice.offset = (
        format.faces.polygons.index_slice.offset + self.poly_index_struct.size)
    format.faces.polygons.count_slice.stride = (
        format.faces.polygons.index_slice.stride)
    format.faces.polygons.count_slice.count = (
        format.faces.polygons.index_slice.count)
    
    format.vertices.coords.format = DOUBLE_XYZ
    format.vertices.normals.format = DOUBLE_XYZ
    format.loops.indices.format = UINT32
    format.faces.polygons.index_format = UINT32
    format.faces.polygons.count_format = UINT32
    return format
  
