/**
 *
 */
package ca.eandb.jmist.framework;

import java.util.HashSet;

import ca.eandb.jmist.toolkit.Box3;
import ca.eandb.jmist.toolkit.Interval;
import ca.eandb.jmist.toolkit.Point3;
import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public final class Octree implements RayTraversalStrategy3 {

	/**
	 * @param bound
	 * @param maxDepth
	 */
	public Octree(Box3 bound, int maxDepth) {
		this.root = EmptyNode.getInstance();
		this.bound = bound;
		this.maxDepth = maxDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayTraversalStrategy3#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.toolkit.Interval, ca.eandb.jmist.framework.Visitor)
	 */
	public boolean intersect(Ray3 ray, Interval I, Visitor visitor) {
		Interval J = bound.intersect(ray).intersect(I);

		if (!J.isEmpty()) {
			Point3 p = ray.pointAt(J.minimum());
			TraversalStatus status = new TraversalStatus();
			status.px = p.x();
			status.py = p.y();
			status.pz = p.z();

			return this.root.traverse(ray, I, visitor, status, bound);
		}

		return true;
	}

	public void addItem(PartialBoundable3 item) {
		this.root = this.root.insert(item, maxDepth, bound);
	}

	private static class TraversalStatus {
		public double px;
		public double py;
		public double pz;
		public int cx;
		public int cy;
		public int cz;
		public HashSet<Object> visited = new HashSet<Object>();
	}

	private static abstract class Node {

		public abstract Node insert(PartialBoundable3 item, int maxDepth, Box3 bound);

		public boolean traverse(Ray3 ray, Interval I, Visitor visitor,
				TraversalStatus status, Box3 bound) {

			double t, tx = 0.0, ty = 0.0, tz = 0.0;
			int ncx = 0, ncy = 0, ncz = 0;
			Vector3 d = ray.direction();

			if (d.x() > 0.0) {

				tx = (bound.maximumX() - status.px) / d.x();
				ncx = 1;

			} else if (d.x() < 0.0) {

				tx = (bound.minimumX() - status.px) / d.x();
				ncx = -1;

			}

			if (d.y() > 0.0) {

				ty = (bound.maximumY() - status.py) / d.y();
				ncy = 1;

			} else if (d.y() < 0.0) {

				ty = (bound.minimumY() - status.py) / d.y();
				ncy = -1;

			}

			if (d.z() > 0.0) {

				tz = (bound.maximumZ() - status.pz) / d.z();
				ncz = 1;

			} else if (d.z() < 0.0) {

				tz = (bound.minimumZ() - status.pz) / d.z();
				ncz = -1;

			}

			status.cx = status.cy = status.cz = 0;

			if (tx < ty && tx < tz) {
				t = tx;
				status.cx = ncx;
			} else if (ty < tz) {
				t = ty;
				status.cy = ncy;
			} else {
				t = tz;
				status.cz = ncz;
			}

			status.px += t * d.x();
			status.py += t * d.y();
			status.pz += t * d.z();

			return true;

		}

		public Node find(Point3 p, Box3 bound) {
			return bound.contains(p) ? this : null;
		}

	}

	private static final class LeafNode extends Node {

		private PartialBoundable3 item;
		private LeafNode next;

		/**
		 * @param item
		 * @param next
		 */
		public LeafNode(PartialBoundable3 item, LeafNode next) {
			this.item = item;
			this.next = next;
		}

		/**
		 * @param item
		 */
		public LeafNode(PartialBoundable3 item) {
			this(item, null);
		}

		@Override
		public Node insert(PartialBoundable3 item, int maxDepth, Box3 bound) {
			return split(new LeafNode(item, this), maxDepth, bound);
		}

		@Override
		public boolean traverse(Ray3 ray, Interval I, Visitor visitor,
				TraversalStatus status, Box3 bound) {

			boolean result = true;

			for (LeafNode leaf = this; leaf != null; leaf = leaf.next) {
				if (!status.visited.contains(leaf.item)) {
					status.visited.add(leaf.item);
					if (!visitor.visit(leaf.item)) {
						result = false;
					}
				}
			}

			return result && super.traverse(ray, I, visitor, status, bound);

		}

	}

	private static final class InternalNode extends Node {

		public InternalNode() {
			for (int i = 0; i < children.length; i++) {
				children[i] = EmptyNode.getInstance();
			}
		}

		@Override
		public Node insert(PartialBoundable3 item, int maxDepth, Box3 bound) {

			Point3 center = bound.center();

			for (int i = 0; i < 8; i++) {
				Box3 cell = new Box3(center, bound.corner(i));

				if (item.surfaceMayIntersect(cell)) {
					this.children[i] = this.children[i].insert(item, maxDepth - 1, cell);
				}
			}

			return this;

		}

		@Override
		public boolean traverse(Ray3 ray, Interval I, Visitor visitor,
				TraversalStatus status, Box3 bound) {

			Point3 center = bound.center();
			int index = this.chooseCellIndex(status, center, bound);

			while (index >= 0) {
				Box3 cell = new Box3(center, bound.corner(index));
				if (!this.children[index].traverse(ray, I, visitor, status, cell)) {
					return false;
				}
				index = this.advanceCellIndex(index, status);
			}

			return true;

		}

		public int advanceCellIndex(int index, TraversalStatus status) {
			if (status.cx > 0) {
				return (index & 0x1) == 0 ? (index | 0x1) : -1;
			} else if (status.cx < 0) {
				return (index & 0x1) != 0 ? (index & ~0x1) : -1;
			} else if (status.cy > 0) {
				return (index & 0x2) == 0 ? (index | 0x2) : -1;
			} else if (status.cy < 0) {
				return (index & 0x2) != 0 ? (index & ~0x2) : -1;
			} else if (status.cz > 0) {
				return (index & 0x4) == 0 ? (index | 0x4) : -1;
			} else if (status.cz < 0) {
				return (index & 0x4) != 0 ? (index & ~0x4) : -1;
			} else {
				return -1;
			}
		}

		public int chooseCellIndex(TraversalStatus status, Point3 center, Box3 bound) {

			int index = 0;

			if (status.px > center.x()) {
				index |= 0x1;
			}

			if (status.py > center.y()) {
				index |= 0x2;
			}

			if (status.pz > center.z()) {
				index |= 0x4;
			}

			return index;

		}

		@Override
		public Node find(Point3 p, Box3 bound) {

			Point3 center = bound.center();

			for (int i = 0; i < 8; i++) {
				Box3 cell = new Box3(center, bound.corner(i));
				Node node = this.children[i].find(p, cell);

				if (node != null) {
					return node;
				}
			}

			return null;

		}

		private final Node[] children = new Node[8];

	}

	private static final class EmptyNode extends Node {

		private EmptyNode() {
			/* do nothing */
		}

		@Override
		public Node insert(PartialBoundable3 item, int maxDepth, Box3 bound) {
			return split(new LeafNode(item, null), maxDepth, bound);
		}

		public static EmptyNode getInstance() {
			if (instance == null) {
				instance = new EmptyNode();
			}
			return instance;
		}

		private static EmptyNode instance = null;

	}

	private static Node split(LeafNode leaf, int maxDepth, Box3 bound) {

		if (leaf.next == null || maxDepth <= 0) {
			return leaf;
		}

		Node node = new InternalNode();

		for (LeafNode child = leaf; child != null; child = child.next) {
			node = node.insert(child.item, maxDepth - 1, bound);
		}

		return node;

	}

	private Node root;
	private final Box3 bound;
	private final int maxDepth;

}
