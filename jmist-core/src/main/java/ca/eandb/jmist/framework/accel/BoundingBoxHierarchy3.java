/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.RayTraversalStrategy3;
import ca.eandb.jmist.framework.Visitor;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * Stores bounded objects in a tree structure so that intersection tests may be
 * accelerated.
 * @author Brad Kimmel
 */
public final class BoundingBoxHierarchy3 implements RayTraversalStrategy3 {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7768577656591759503L;

  /**
   * Creates a new <code>BoundingBoxHierarchy3</code>.
   */
  public BoundingBoxHierarchy3() {
  }

  /**
   * Adds a new item to this <code>BoundingBoxHierarchy3</code>.
   * @param item The <code>Bounded3</code> item to add.
   * @return A reference to this <code>BoundingBoxHierarchy3</code> so that
   *     calls to this method may be chained.
   */
  public BoundingBoxHierarchy3 addItem(Bounded3 item) {
    return addItem(item, item.boundingBox());
  }

  /**
   * Adds a new item to this <code>BoundingBoxHierarchy3</code>.
   * @param item The <code>Object</code> to add.
   * @param bound The bounding <code>Box3</code> for the item.
   * @return A reference to this <code>BoundingBoxHierarchy3</code> so that
   *     calls to this method may be chained.
   */
  public BoundingBoxHierarchy3 addItem(Object item, Box3 bound) {

    /* Invalidate the tree if we've already built it. */
    this.root = null;

    /* Add the new leaf node. */
    this.leaves.add(new Node(item, bound));

    return this;

  }

  @Override
  public boolean intersect(Ray3 ray, Interval I, Visitor visitor) {

    if (this.leaves.size() > 0) {

      /* Rebuild the tree if necessary. */
      this.ensureReady();

      return this.root.intersect(ray, I, visitor);

    }

    return true;

  }

  /**
   * Rebuilds the tree if necessary.
   */
  private synchronized void ensureReady() {
    if (this.root == null) {
      this.rebuild();
    }
  }

  /**
   * Gets the outer bounding <code>Box3</code> of this
   * <code>BoundingBoxHierarchy3</code>.
   * @return The smallest <code>Box3</code> containing all of the bounding
   *     boxes that were added to this <code>BoundingBoxHierarchy3</code>.
   */
  public Box3 getBoundingBox() {
    if (this.leaves.size() > 0) {
      this.ensureReady();
      return this.root.bound;
    }
    return Box3.EMPTY;
  }

  /**
   * Rebuilds the bounding box tree from the leaves.
   */
  private void rebuild() {
    Node[] nodes = new Node[this.leaves.size()];
    this.leaves.toArray(nodes);
    this.root = this.rebuild(nodes, 0, nodes.length - 1);
  }

  /**
   * Rebuilds the bounding box tree for the specified range of leaves
   * in an array.
   * @param nodes The array containing the leaf <code>Node</code>s to build
   *     the subtree for.
   * @param fromIndex The index of the first node of the range to build the
   *     subtree for.
   * @param toIndex The index of the last node of the range to build the
   *     subtree for.
   * @return The root <code>Node</code> of the subtree containing the
   *     specified leaf <code>Node</code>s.
   */
  private Node rebuild(Node[] nodes, int fromIndex, int toIndex) {

    assert(toIndex >= fromIndex);

    int len = toIndex - fromIndex + 1;

    if (len > 2) {

      /* This is a binary tree, so we deal with more than two nodes by
       * splitting the list in half and a subtree for each half.
       */
      int midIndex = (fromIndex + toIndex) / 2;
      Arrays.sort(nodes, fromIndex, toIndex, this.getComparator(nodes, fromIndex, toIndex));

      return new Node(
          this.rebuild(nodes, fromIndex, midIndex),
          this.rebuild(nodes, midIndex + 1, toIndex)
      );

    } else if (len == 2) {

      /* If there are two nodes, create a single node with the two leaves
       * as its children.
       */
      return new Node(nodes[fromIndex], nodes[toIndex]);

    } else { /* len == 1 */

      /* If there is only one node, then it is the subtree. */
      assert(len == 1);
      return nodes[fromIndex];

    }

  }

  /**
   * Gets the <code>Comparator</code> to use to sort the specified list of
   * <code>Node</code>s.
   * @param nodes The array of <code>Node</code>s to be sorted.
   * @param fromIndex The first index of the range of <code>Node</code>s to
   *     be sorted.
   * @param toIndex The last index of the range of <code>Node</code>s to be
   *     sorted.
   * @return The <code>Comparator</code> that will sort the
   *     <code>Node</code>s by the centers of their bounding boxes in the
   *     direction in which the bounding box of all the nodes is the
   *     longest.
   */
  private Comparator<Node> getComparator(Node[] nodes, int fromIndex, int toIndex) {

    Box3 bound = getBoundingBox(nodes, fromIndex, toIndex);

    if (bound.lengthX() > bound.lengthY() && bound.lengthX() > bound.lengthZ()) {
      return COMPARATORS[NodeComparator.X_AXIS];
    } else if (bound.lengthY() > bound.lengthZ()) {
      return COMPARATORS[NodeComparator.Y_AXIS];
    } else {
      return COMPARATORS[NodeComparator.Z_AXIS];
    }

  }

  /**
   * Gets the bounding box of a collection of <code>Node</code>s.
   * @param nodes The array containing the <code>Node</code>s to get the
   *     bounding box for.
   * @param fromIndex The first index into <code>nodes</code> of the range to
   *     get the bounding box for.
   * @param toIndex The last index into <code>nodes</code> of the range to
   *     get the bounding box for.
   * @return The smallest <code>Box3</code> that contains all of the bounding
   *     boxes of the specified <code>Node</code>s.
   */
  private Box3 getBoundingBox(Node[] nodes, int fromIndex, int toIndex) {

    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();

    for (int i = fromIndex; i <= toIndex; i++) {
      builder.add(nodes[i].bound);
    }

    return builder.getBoundingBox();

  }

  /**
   * A node in the bounding box tree.
   * @author Brad Kimmel
   */
  private static final class Node {

    /** The <code>Object</code> contained at this <code>Node</code>. */
    public final Object item;

    /** The bounding <code>Box3</code> of this <code>Node</code>. */
    public final Box3 bound;

    /** The root <code>Node</code> of the first subtree. */
    public final Node a;

    /** The root <code>Node</code> of the second subtree. */
    public final Node b;

    /**
     * Creates a leaf <code>Node</code>.
     * @param item The <code>Object</code> to be stored at the new
     *    <code>Node</code>.
     * @param bound The bounding <code>Box3</code> of the new
     *     <code>Node</code>.
     */
    public Node(Object item, Box3 bound) {
      this.item = item;
      this.bound = bound;
      this.a = null;
      this.b = null;
    }

    /**
     * Creates an internal <code>Node</code>.
     * @param a The root <code>Node</code> of the first subtree.
     * @param b The root <code>Node</code> of the second subtree.
     */
    public Node(Node a, Node b) {
      this.item = null;
      this.bound = Box3.smallestContaining(a.bound, b.bound);
      this.a = a;
      this.b = b;
    }

    /**
     * Intersects a <code>Ray3</code> with the subtree rooted at this
     * <code>Node</code>.
     * @param ray The <code>Ray3</code> to intersect with this subtree.
     * @param I The <code>Interval</code> along the ray to consider.
     * @param visitor The <code>Visitor</code> to notify when a
     *     <code>Node</code> containing an item is hit.
     * @return A value indicating whether the operation was completed
     *     without being cancelled.
     */
    public boolean intersect(Ray3 ray, Interval I, Visitor visitor) {

      if (this.bound.intersects(ray, I)) {

        if (item != null && !visitor.visit(item)) {
          return false;
        }

        if (a != null && !a.intersect(ray, I, visitor)) {
          return false;
        }

        if (b != null && !b.intersect(ray, I, visitor)) {
          return false;
        }

      }

      return true;

    }

  }

  /**
   * Compares <code>Node</code>s according to the centers of their bounding
   * boxes along a particular axis.
   * @author Brad Kimmel
   */
  private static final class NodeComparator implements Comparator<Node> {

    /**
     * Indicates that this <code>NodeComparator</code> should compare the
     * x-coordinates of the centers of the bounding boxes.
     */
    public static final int X_AXIS = 0;

    /**
     * Indicates that this <code>NodeComparator</code> should compare the
     * y-coordinates of the centers of the bounding boxes.
     */
    public static final int Y_AXIS = 1;

    /**
     * Indicates that this <code>NodeComparator</code> should compare the
     * z-coordinates of the centers of the bounding boxes.
     */
    public static final int Z_AXIS = 2;

    /**
     * Creates a new <code>NodeComparator</code>.
     * @param axis A value indicating the axis along which the comparison
     *     should take place.
     */
    public NodeComparator(int axis) {
      this.axis = axis;
    }

    @Override
    public int compare(Node o1, Node o2) {
      Point3 p1 = o1.bound.center();
      Point3 p2 = o2.bound.center();

      switch (this.axis) {
      case X_AXIS: return Double.compare(p1.x(), p2.x());
      case Y_AXIS: return Double.compare(p1.y(), p2.y());
      case Z_AXIS: return Double.compare(p1.z(), p2.z());
      }

      throw new UnsupportedOperationException("invalid axis");
    }

    /**
     * A value indicating the axis along which the comparison should take
     * place.
     */
    private final int axis;

  }

  /** The root node of the bounding box tree. */
  private Node root;

  /** The <code>List</code> of leaf nodes in the bounding box tree. */
  private final List<Node> leaves = new ArrayList<Node>();

  /** The <code>NodeComparator</code>s for each axis. */
  private static final NodeComparator[] COMPARATORS = new NodeComparator[]{
      new NodeComparator(NodeComparator.X_AXIS),
      new NodeComparator(NodeComparator.Y_AXIS),
      new NodeComparator(NodeComparator.Z_AXIS)
  };

}
