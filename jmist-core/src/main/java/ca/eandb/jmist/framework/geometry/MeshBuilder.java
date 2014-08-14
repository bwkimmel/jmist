/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.geometry;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.AffineTransformation3;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.IntegerArray;

/**
 * A builder for creating polygonal meshes.
 * @author Brad Kimmel
 */
public final class MeshBuilder implements AffineTransformable3 {

  /** A <code>List</code> of vertices. */
  private final List<Vertex> vertices = new ArrayList<Vertex>();

  /** A <code>List</code> of faces. */
  private final List<Face> faces = new ArrayList<Face>();

  /**
   * The pending <code>AffineTransformation3</code> to be applied to the
   * vertices.
   */
  private final AffineTransformation3 trans = new AffineTransformation3();

  /**
   * A mesh vertex.
   */
  private static final class Vertex {

    /** The <code>Point3</code> indicating the position of the vertex. */
    public Point3 position;

    /** A temporary <code>boolean</code> value. */
    public boolean flag;

    /** A temporary integer value. */
    public int tempInt;

    /**
     * Creates a new <code>Vertex</code>.
     * @param position The <code>Point3</code> indicating the position of
     *     the vertex.
     */
    public Vertex(Point3 position) {
      this.position = position;
    }

  }

  /**
   * A mesh face.
   */
  private static final class Face {

    /**
     * An <code>IntegerArray</code> containing the indices into
     * {@link MeshBuilder#vertices} of the vertices of this face.  The
     * face normal is indicated using the right hand rule.
     */
    public final IntegerArray vertexIndex = new IntegerArray();

    /** A temporary <code>boolean</code> value. */
    public boolean flag;

    /**
     * Creates a new <code>Face</code>.
     * @param index The indices into {@link MeshBuilder#vertices} of the
     *     vertices of this face.
     */
    public Face(int... index) {
      vertexIndex.addAll(index);
    }

  };

  /**
   * An edge joining two vertices.
   */
  private static final class Edge {

    /** Index into {@link MeshBuilder#vertices} of the first vertex. */
    public final int index1;

    /** Index into {@link MeshBuilder#vertices} of the second vertex. */
    public final int index2;

    /**
     * Creates a new <code>Edge</code>.
     * @param index1 Index into {@link MeshBuilder#vertices} of the first
     *     vertex.
     * @param index2 Index into {@link MeshBuilder#vertices} of the second
     *     vertex.
     */
    public Edge(int index1, int index2) {
      this.index1 = index1;
      this.index2 = index2;
    }

    /**
     * Gets the <code>Edge</code> in the opposite direction.
     * @return The <code>Edge</code> in the opposite direction.
     */
    public Edge reverse() {
      return new Edge(index2, index1);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
      return obj instanceof Edge && ((Edge) obj).index1 == index1
          && ((Edge) obj).index2 == index2;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
      return new Integer(index1 + index2).hashCode();
    }

  }

  /**
   * Creates a new <code>MeshBuilder</code> initialized with a box.
   * @param box The <code>Box3</code> to initialize the builder with.
   * @return The new <code>MeshBuilder</code>.
   */
  public static MeshBuilder fromBox(Box3 box) {
    MeshBuilder b = new MeshBuilder();
    for (int i = 0; i < 8; i++) {
      b.vertices.add(new Vertex(box.interpolate(
          (i & 1) != 0 ? 1 : 0,
          (i & 2) != 0 ? 1 : 0,
          (i & 4) != 0 ? 1 : 0)));
    }
    b.faces.add(new Face(0, 2, 3, 1));
    b.faces.add(new Face(4, 5, 6, 7));
    b.faces.add(new Face(0, 1, 5, 4));
    b.faces.add(new Face(1, 3, 7, 5));
    b.faces.add(new Face(3, 2, 6, 7));
    b.faces.add(new Face(2, 0, 4, 6));
    return b;
  }

  /**
   * Creates a new <code>MeshBuilder</code> initialized with a cylinder.  The
   * center of the base of the cylinder is at the origin and the axis is
   * parallel to the z-axis.
   * @param radius The radius of the cylinder.
   * @param height The height of the cylinder.
   * @param sideFaces The number of faces to divide the side of the cylinder
   *     into.
   * @return The new <code>MeshBuilder</code>.
   */
  public static MeshBuilder fromCylinder(double radius, double height, int sideFaces) {
    MeshBuilder b = new MeshBuilder();
    Face top = new Face();
    Face bot = new Face();
    b.faces.add(top);
    b.faces.add(bot);
    for (int i = 0; i < sideFaces; i++) {
      double theta = 2.0 * Math.PI * (double) i / (double) sideFaces;
      double x = radius * Math.cos(theta);
      double y = radius * Math.sin(theta);
      b.vertices.add(new Vertex(new Point3(x, y, 0.0)));
      b.vertices.add(new Vertex(new Point3(x, y, height)));
      int i0 = 2 * i;
      int i1 = 2 * i + 1;
      int i2 = 2 * (i - 1) + 1;
      int i3 = 2 * (i - 1);
      if (i2 < 0) {
        i2 += 2 * sideFaces;
        i3 += 2 * sideFaces;
      }
      b.faces.add(new Face(i0, i1, i2, i3));
      top.vertexIndex.add(i1);
      bot.vertexIndex.add(2 * (sideFaces - i - 1));
    }
    return b;
  }

  /**
   * Slices the mesh at a given plane.
   * @param plane The <code>Plane3</code> through which to slide the mesh.
   *     The region of the mesh below the plane will be kept.
   * @param cap A value indicating whether holes introduced by the operation
   *     should be capped.
   */
  public void slice(Plane3 plane, boolean cap) {
    applyTrans();
    compareVertices(plane);
    Map<Edge, Integer> splitMap = new HashMap<Edge, Integer>();
    if (cap) {
      setAllVertexTemp(-1);
    }
    for (int i = 0; i < faces.size(); i++) {
      sliceFace(plane, i, splitMap);
    }
    if (cap) {
      addCaps();
    }
    removeUnflaggedFaces();
    removeUnflaggedVertices();
  }

  private void addCaps() {
    for (int i = 0, n = vertices.size(); i < n; i++) {
      Vertex v = vertices.get(i);
      if (v.tempInt >= 0) {
        addCap(i);
      }
    }
  }

  private void addCap(int start) {
    Face f = new Face();
    Vertex v = vertices.get(start);
    while (v.tempInt >= 0) {
      int index = v.tempInt;
      f.vertexIndex.add(index);
      v.tempInt = -1;
      v = vertices.get(index);
    }
    f.flag = true;
    faces.add(f);
  }

  /**
   * Writes the mesh out in Wavefront OBJ format.
   * @param os The <code>OutputStream</code> to write to.
   */
  public void writeWavefrontObj(OutputStream os) {
    applyTrans();
    PrintStream out = new PrintStream(os);
    for (Vertex v : vertices) {
      out.printf("v %f %f %f", v.position.x(), v.position.y(), v.position.z());
      out.println();
    }
    for (Face f : faces) {
      out.print("f");
      for (int index : f.vertexIndex) {
        out.print(" ");
        out.print(index + 1);
      }
      out.println();
    }
  }

  /**
   * Creates a <code>SceneElement</code> representing the mesh geometry.
   * @return A <code>SceneElement</code> representing the mesh geometry.
   */
  public SceneElement createGeometry() {
    applyTrans();
    Point3[] vs = new Point3[vertices.size()];
    for (int i = 0; i < vs.length; i++) {
      vs[i] = vertices.get(i).position;
    }
    int[][] fs = new int[faces.size()][];
    for (int i = 0; i < fs.length; i++) {
      fs[i] = faces.get(i).vertexIndex.toIntegerArray();
    }
    return new PolyhedronGeometry(vs, fs);
  }

  private void setAllVertexTemp(int temp) {
    for (Vertex v : vertices) {
      v.tempInt = temp;
    }
  }

  private void removeUnflaggedVertices() {
    for (int i = 0, j = 0, n = vertices.size(); i < n; i++) {
      Vertex v = vertices.get(i);
      v.tempInt = j;
      if (v.flag) {
        j++;
      }
    }
    for (int i = 0, n = faces.size(); i < n; i++) {
      Face f = faces.get(i);
      for (int j = 0, m = f.vertexIndex.size(); j < m; j++) {
        Vertex v = vertices.get(f.vertexIndex.get(j));
        f.vertexIndex.set(j, v.tempInt);
        assert(v.flag);
      }
    }
    Iterator<Vertex> iter = vertices.iterator();
    while (iter.hasNext()) {
      if (!iter.next().flag) {
        iter.remove();
      }
    }
  }

  private void removeUnflaggedFaces() {
    Iterator<Face> iter = faces.iterator();
    while (iter.hasNext()) {
      Face face = iter.next();
      if (!face.flag) {
        iter.remove();
      }
    }
  }

  private void compareVertices(Plane3 plane) {
    for (int i = 0; i < vertices.size(); i++) {
      Vertex v = vertices.get(i);
      v.flag = (plane.altitude(v.position) < 0.0);
    }
  }

  private void setVertexTempToIndex() {
    for (int i = 0; i < vertices.size(); i++) {
      vertices.get(i).tempInt = i;
    }
  }

  private void setAllVertexFlags(boolean value) {
    for (Vertex v : vertices) {
      v.flag = value;
    }
  }

  /**
   * Merge vertices that are near one another.
   * @param epsilon The maximum distance between vertices to be merged.
   */
  public void mergeVertices(double epsilon) {
    setVertexTempToIndex();
    setAllVertexFlags(true);
    int n = vertices.size();
    for (int i = 0; i < n; i++) {
      Vertex vi = vertices.get(i);
      if (vi.flag) {
        for (int j = i + 1; j < n; j++) {
          Vertex vj = vertices.get(j);
          if (vj.flag && vi.position.distanceTo(vj.position) < epsilon) {
            vj.flag = false;
            vj.tempInt = i;
          }
        }
      }
    }
    for (Face f : faces) {
      for (int i = 0, m = f.vertexIndex.size(); i < m; i++) {
        Vertex v = vertices.get(f.vertexIndex.get(i));
        if (!v.flag) {
          f.vertexIndex.set(i, v.tempInt);
        }
      }
    }
    removeUnflaggedVertices();
    for (Face f : faces) {
      int lastIndex = f.vertexIndex.get(f.vertexIndex.size() - 1);
      Iterator<Integer> iter = f.vertexIndex.iterator();
      while (iter.hasNext()) {
        int index = iter.next();
        if (index == lastIndex) {
          iter.remove();
        }
        lastIndex = index;
      }
    }
    Iterator<Face> iter = faces.iterator();
    while (iter.hasNext()) {
      Face f = iter.next();
      if (f.vertexIndex.size() < 3) {
        iter.remove();
      }
    }
  }

  private int addVertex(Point3 p) {
    int result = vertices.size();
    vertices.add(new Vertex(p));
    return result;
  }

  private void sliceFace(Plane3 plane, int faceIndex, Map<Edge, Integer> splitMap) {
    Face face = faces.get(faceIndex);
    boolean isAbove = false;
    boolean isBelow = false;
    for (int i : face.vertexIndex) {
      Vertex v = vertices.get(i);
      if (v.flag) {
        isAbove = true;
      } else {
        isBelow = true;
      }
      if (isAbove && isBelow) {
        break;
      }
    }

    if (isAbove && !isBelow) {
      face.flag = true;
      return;
    } else if (isBelow && !isAbove) {
      face.flag = false;
      return;
    }

    assert(isAbove && isBelow);

    IntegerArray indices = new IntegerArray();
    int ia = -1;
    int ofs = 0;
    int n = face.vertexIndex.size();
    while (!vertices.get(face.vertexIndex.get(ofs)).flag) {
      ofs++;
    }
    for (int j = 0; j < n; j++) {
      int i = (j + ofs) % n;
      int i1 = face.vertexIndex.get(i);
      int i2 = face.vertexIndex.get((i + 1) % n);
      int i3 = face.vertexIndex.get((i + (n - 1)) % n);
      Vertex v1 = vertices.get(i1);
      Vertex v2 = vertices.get(i2);
      Vertex v3 = vertices.get(i3);
      if (v1.flag) {
        indices.add(i1);
      } else { // !v1.isAbove
        if (v3.flag) {
          Edge edge = new Edge(i3, i1);
          Edge edgeRev = edge.reverse();
          if (splitMap.containsKey(edgeRev)) {
            int index = splitMap.get(edgeRev);
            ia = index;
            indices.add(index);
          } else {
            assert(!splitMap.containsKey(edge));
            Ray3 ray = new Ray3(v1.position, v3.position);
            Point3 p = ray.pointAt(plane.intersect(ray));
            int index = addVertex(p);
            ia = index;
            vertices.get(index).tempInt = -1;
            vertices.get(index).flag = true;
            indices.add(index);
            splitMap.put(edge, index);
          }
        }
        if (v2.flag) {
          Edge edge = new Edge(i1, i2);
          Edge edgeRev = edge.reverse();
          if (splitMap.containsKey(edgeRev)) {
            int index = splitMap.get(edgeRev);
            assert(ia >= 0);
            vertices.get(index).tempInt = ia;
            ia = -1;
            indices.add(index);
          } else {
            assert(!splitMap.containsKey(edge));
            Ray3 ray = new Ray3(v1.position, v2.position);
            Point3 p = ray.pointAt(plane.intersect(ray));
            int index = addVertex(p);
            assert(ia >= 0);
            if (ia < 0) {
              ia = -1;
            }
            vertices.get(index).tempInt = ia;
            ia = -1;
            vertices.get(index).flag = true;
            indices.add(index);
            splitMap.put(edge, index);
          }
        }
      }
    }

    assert(!indices.isEmpty());

    face.vertexIndex.clear();
    face.vertexIndex.addAll(indices);

    face.flag = true;
  }

  /**
   * Applies pending transformations to the vertices.
   */
  private void applyTrans() {
    if (trans.isDirty()) {
      for (Vertex v : vertices) {
        v.position = trans.apply(v.position);
      }
      trans.reset();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotate(ca.eandb.jmist.math.Vector3, double)
   */
  public void rotate(Vector3 axis, double angle) {
    trans.rotate(axis, angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateX(double)
   */
  public void rotateX(double angle) {
    trans.rotateX(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateY(double)
   */
  public void rotateY(double angle) {
    trans.rotateY(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateZ(double)
   */
  public void rotateZ(double angle) {
    trans.rotateZ(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Scalable#scale(double)
   */
  public void scale(double c) {
    trans.scale(c);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretch(double, double, double)
   */
  public void stretch(double cx, double cy, double cz) {
    trans.stretch(cx, cy, cz);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Stretchable3#stretch(ca.eandb.jmist.math.Vector3, double)
   */
  public void stretch(Vector3 axis, double c) {
    trans.stretch(axis, c);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchX(double)
   */
  public void stretchX(double cx) {
    trans.stretchX(cx);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchY(double)
   */
  public void stretchY(double cy) {
    trans.stretchY(cy);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchZ(double)
   */
  public void stretchZ(double cz) {
    trans.stretchZ(cz);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AffineTransformable3#transform(ca.eandb.jmist.math.AffineMatrix3)
   */
  public void transform(AffineMatrix3 T) {
    trans.transform(T);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.LinearTransformable3#transform(ca.eandb.jmist.math.LinearMatrix3)
   */
  public void transform(LinearMatrix3 T) {
    trans.transform(T);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Translatable3#translate(ca.eandb.jmist.math.Vector3)
   */
  public void translate(Vector3 v) {
    trans.translate(v);
  }

}
