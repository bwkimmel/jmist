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
package ca.eandb.jmist.framework.accel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.scene.SceneElementDecorator;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.util.ArrayUtil;
import ca.eandb.util.UnexpectedException;

/**
 * A decorator <code>SceneElement</code> that applies a bounded interval
 * hierarchy (BIH) to the primitives of the underlying <code>SceneElement</code>.
 *
 * This is an implementation of the algorithm described in the following paper:
 *
 * <blockquote>
 * C. Wachter, A. Keller,
 * <a href="http://ainc.de/Research/BIH.pdf">Instant ray tracing: The bounded interval hierarchy</a>,
 * In <em>Proceedings of the Eurgraphics Symposium on Rendering</em>
 * pp. 139-149, 2006.
 * </blockquote>
 *
 * @author Brad Kimmel
 */
public final class BoundingIntervalHierarchy extends SceneElementDecorator {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5882424225852208674L;

  private transient int[] items;

  private transient NodeBuffer buffer;

  private transient int root;

  private transient Box3 boundingBox;

  private transient boolean ready = false;

  private final int maxItemsPerLeaf = 2;

  private final double tolerance = MathUtil.EPSILON;

  /**
   * @param inner
   */
  public BoundingIntervalHierarchy(SceneElement inner) {
    super(inner);
  }

  /**
   * @param inner
   * @throws IOException
   */
  public BoundingIntervalHierarchy(SceneElement inner, String filename) throws IOException {
    super(inner);

    File file = new File(filename);
    if (file.isFile()) {
      FileInputStream fs = new FileInputStream(file);
      try {
        restore(fs);
      } finally {
        fs.close();
      }
    } else {
      FileOutputStream fs = new FileOutputStream(file);
      try {
        save(fs);
        fs.flush();
      } finally {
        fs.close();
      }
    }
  }

  public void save(OutputStream out) throws IOException {
    ensureReady();

    ObjectOutputStream oos = new ObjectOutputStream(out);
    oos.writeObject(items);
    oos.writeInt(root);
    oos.writeObject(boundingBox);
    oos.writeInt(buffer.next);

    byte[] buf = buffer.buf.array();
    oos.write(buf, 0, buffer.next);

    oos.flush();

    System.out.printf("Wrote %d bytes from backing array of size %d.", buffer.next, buf.length);
    System.out.println();
  }

  public void restore(InputStream in) throws IOException {
    ObjectInputStream ois = new ObjectInputStream(in);

    try {
      items = (int[]) ois.readObject();
      root = ois.readInt();
      boundingBox = (Box3) ois.readObject();

      buffer = new NodeBuffer();
      int size = ois.readInt();
      byte[] buf = new byte[size];
      int pos = 0;

      while (pos < size) {
        int read = ois.read(buf, pos, size - pos);
        if (read <= 0) {
          throw new IOException(String.format("Failed to read node buffer, only read %d of %d bytes (read=%d).", pos, size, read));
        }
        pos += read;
      }

      buffer.buf = ByteBuffer.wrap(buf);
      buffer.next = size;

      ready = true;
    } catch (ClassNotFoundException e) {
      throw new UnexpectedException(e);
    }

    System.out.println("Restored BIH from file.");
  }


  public void dump() {
    dump(root, 0);
  }

  private void dump(int node, int depth) {
    int type = buffer.getType(node);
    if (type == NodeBuffer.TYPE_LEAF) {
      int start = buffer.getStart(node);
      int end = buffer.getEnd(node);
      indent(depth);
      System.out.printf("LEAF(%d,%d):", start, end);
      for (int i = start; i < end; i++) {
        System.out.printf(" %d", items[i]);
      }
      System.out.println();
    } else {
      int axis = 'x' + type;
      double left = buffer.getLeftPlane(node);
      double right = buffer.getRightPlane(node);
      indent(depth);
      System.out.printf("INTERNAL: AXIS=%c, CLIP=(%f, %f)\n", axis, left, right);
      int leftChild = buffer.getLeftChild(node);
      if (leftChild >= 0) {
        indent(depth);
        System.out.println("{L");
        dump(leftChild, depth + 1);
        indent(depth);
        System.out.println("}");
      }
      int rightChild = buffer.getRightChild(node);
      if (rightChild >= 0) {
        indent(depth);
        System.out.println("{R");
        dump(rightChild, depth + 1);
        indent(depth);
        System.out.println("}");
      }
    }
  }

  private void indent(int depth) {
    for (int i = 0; i < depth; i++) {
      System.out.print("  ");
    }
  }

  private void ensureReady() {
    if (!ready) {
      build();
    }
  }

  private synchronized void build() {
    if (!ready) {
      buffer = new NodeBuffer();
      items = ArrayUtil.range(0, super.getNumPrimitives() - 1);
      boundingBox = boundingBox();
      Bound bound = new Bound(boundingBox);
      Clip clip = new Clip();
      root = buffer.allocateInternal();
      build(root, bound, 0, items.length, clip);
      ready = true;
    }
  }

  private void build(int offset, Bound bound, int start, int end, Clip clip) {
    assert(end > start);

    double lenx = bound.maxx - bound.minx;
    double leny = bound.maxy - bound.miny;
    double lenz = bound.maxz - bound.minz;
    double maxlen = Math.max(Math.max(lenx, leny), lenz);
    int axis;
    double plane;
    if (lenx > leny && lenx > lenz) {
      axis = 0;
      plane = 0.5 * (bound.minx + bound.maxx);
    } else if (leny > lenz) {
      axis = 1;
      plane = 0.5 * (bound.miny + bound.maxy);
    } else {
      axis = 2;
      plane = 0.5 * (bound.minz + bound.maxz);
    }
    int split = split(axis, plane, start, end, clip);
    int left = split - start;
    int right = end - split;

    int leftChild = (left > maxItemsPerLeaf && maxlen >= tolerance) ? buffer.allocateInternal() : (left > 0) ? buffer.allocateLeaf() : -1;
    int rightChild = (right > maxItemsPerLeaf && maxlen >= tolerance) ? buffer.allocateInternal() : (right > 0) ? buffer.allocateLeaf() : -1;

    assert(offset >= 0);
    int firstChild = (left > 0) ? leftChild : rightChild;

    buffer.writeInternal(offset, axis, clip, firstChild);

    // add new node here
    if (left > maxItemsPerLeaf && maxlen >= tolerance) {
      double temp = bound.setMax(axis, plane);
      build(leftChild, bound, start, split, clip);
      bound.setMax(axis, temp);
    } else if (left > 0) {
      assert(leftChild >= 0);
      if (start == 1) {
        start = 1;
      }
      buffer.writeLeaf(leftChild, start, split);
    }
    if (right > maxItemsPerLeaf && maxlen >= tolerance) {
      double temp = bound.setMin(axis, plane);
      build(rightChild, bound, split, end, clip);
      bound.setMin(axis, temp);
    } else if (right > 0) {
      assert(rightChild >= 0);
      if (split == 1) {
        split = 1;
      }
      buffer.writeLeaf(rightChild, split, end);
    }
  }

  private static class Bound {
    public double minx;
    public double maxx;
    public double miny;
    public double maxy;
    public double minz;
    public double maxz;

    public Bound(Box3 box) {
      minx = box.minimumX();
      miny = box.minimumY();
      minz = box.minimumZ();
      maxx = box.maximumX();
      maxy = box.maximumY();
      maxz = box.maximumZ();
    }

    public double setMin(int axis, double value) {
      double temp;
      switch (axis) {
      case 0:
        temp = minx;
        minx = value;
        return temp;
      case 1:
        temp = miny;
        miny = value;
        return temp;
      case 2:
        temp = minz;
        minz = value;
        return temp;
      }
      throw new IllegalArgumentException();
    }

    public double setMax(int axis, double value) {
      double temp;
      switch (axis) {
      case 0:
        temp = maxx;
        maxx = value;
        return temp;
      case 1:
        temp = maxy;
        maxy = value;
        return temp;
      case 2:
        temp = maxz;
        maxz = value;
        return temp;
      }
      throw new IllegalArgumentException();
    }

  }

  private static class Clip {

    public double left;
    public double right;

    public Clip() {
      reset();
    }

    public void reset() {
      left = Double.NEGATIVE_INFINITY;
      right = Double.POSITIVE_INFINITY;
    }

  }

  private int split(int axis, double plane, int start, int end, Clip clip) {
    double min, max, mid;
    int split = start;
    clip.reset();
    for (int i = start; i < end; i++) {
      Box3 bound = getBoundingBox(items[i]);
      min = bound.minimum(axis);
      max = bound.maximum(axis);
      mid = 0.5 * (min + max);
      if (mid < plane) {
        if (max > clip.left) {
          clip.left = max;
        }
        if (i > split) {
          ArrayUtil.swap(items, split, i);
        }
        split++;
      } else {
        if (min < clip.right) {
          clip.right = min;
        }
      }
    }
    return split;
  }

  private static final class NodeBuffer {

    private static final int SIZE_INTERNAL = 12;

    private static final int SIZE_LEAF = 8;

    public static final int TYPE_LEAF = 3;

    private ByteBuffer buf = ByteBuffer.allocate(16384);

    private int next = 0;

    public void writeInternal(int offset, int axis, Clip clip, int firstChild) {
      assert((firstChild & 0x3) == 0);
      buf.position(offset);
      buf.putInt(firstChild | axis);
      float left = (float) clip.left;
      float right = (float) clip.right;
      if (left < clip.left) {
        left = Math.nextUp(left);
      }
      if (right > clip.right) {
        right = Math.nextDown(right);
      }
      buf.putFloat(left);
      buf.putFloat(right);
    }

    public void writeLeaf(int offset, int start, int end) {
      buf.position(offset);
      buf.putInt((start << 2) | 3);
      buf.putInt(end);
    }

    public int allocateInternal() {
      return allocate(SIZE_INTERNAL);
    }

    public int allocateLeaf() {
      return allocate(SIZE_LEAF);
    }

    private int allocate(int size) {
      int result = next;
      next += size;
      if (next > buf.capacity()) {
        ByteBuffer newBuf = ByteBuffer.allocate(2 * buf.capacity());
        buf.clear();
        newBuf.put(buf);
        buf = newBuf;
      }
      return result;
    }

    public int getStart(int offset) {
      return buf.getInt(offset) >> 2;
    }

    public int getEnd(int offset) {
      return buf.getInt(offset + 4);
    }

    public int getType(int offset) {
      return buf.getInt(offset) & 0x3;
    }

    public boolean isLeaf(int offset) {
      return getType(offset) == TYPE_LEAF;
    }

    public int getNext(int offset) {
      if (isLeaf(offset)) {
        return offset + SIZE_LEAF;
      } else {
        return offset + SIZE_INTERNAL;
      }
    }

    public int getLeftChild(int offset) {
      return Double.isInfinite(getLeftPlane(offset)) ? -1 : getFirstChild(offset);
    }

    public int getRightChild(int offset) {
      if (Double.isInfinite(getRightPlane(offset))) {
        return -1;
      } else if (Double.isInfinite(getLeftPlane(offset))) {
        return getFirstChild(offset);
      } else {
        return getNext(getFirstChild(offset));
      }
    }

    public int getFirstChild(int offset) {
      return buf.getInt(offset) & ~0x3;
    }

    public double getLeftPlane(int offset) {
      return (double) buf.getFloat(offset + 4);
    }

    public double getRightPlane(int offset) {
      return (double) buf.getFloat(offset + 8);
    }

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    ensureReady();
    Interval I = boundingBox.intersect(ray).intersect(recorder.interval());
    if (!I.isEmpty()) {
      intersectNode(root, I.minimum(), I.maximum(), ray, recorder);
    }
  }

  private void intersectNode(int node, double near, double far, Ray3 ray,
      IntersectionRecorder recorder) {

    if (far < near) {
      return;
    }

    int type = buffer.getType(node);

    if (type == NodeBuffer.TYPE_LEAF) {
      int start = buffer.getStart(node);
      int end = buffer.getEnd(node);
      for (int i = start; i < end; i++) {
        if (i == 1) {
          i = 1;
        }
        super.intersect(items[i], ray, recorder);
      }
    } else {
      double p = ray.origin().get(type);
      double v = ray.direction().get(type);

      double lp = buffer.getLeftPlane(node);
      double rp = buffer.getRightPlane(node);

      double ld = (lp - p) / v;
      double rd = (rp - p) / v;

      if (v > 0.0) { // left to right

        if (near < ld) {
          int child = buffer.getLeftChild(node);
          if (child >= 0) {
            intersectNode(child, near, Math.min(ld, far), ray, recorder);
            far = Math.min(far, recorder.interval().maximum());
          }
        }

        if (rd < far) {
          int child = buffer.getRightChild(node);
          if (child >= 0) {
            intersectNode(child, Math.max(near, rd), far, ray, recorder);
          }
        }

      } else { // right to left

        if (near < rd) {
          int child = buffer.getRightChild(node);
          if (child >= 0) {
            intersectNode(child, near, Math.min(rd, far), ray, recorder);
            far = Math.min(far, recorder.interval().maximum());
          }
        }

        if (ld < far) {
          int child = buffer.getLeftChild(node);
          if (child >= 0) {
            intersectNode(child, Math.max(near, ld), far, ray, recorder);
          }
        }

      }
    }

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#visibility(ca.eandb.jmist.math.Ray3)
   */
  @Override
  public boolean visibility(Ray3 ray) {
    NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(new Interval(0.0, ray.limit()));
    intersect(ray, recorder);
    return recorder.isEmpty();
  }


}
