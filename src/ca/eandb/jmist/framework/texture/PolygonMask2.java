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
package ca.eandb.jmist.framework.texture;

import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder2;
import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.util.IntegerArray;

/**
 * A <code>Mask2</code> that is opaque inside a specified polygon, and
 * transparent outside a polygon.
 * @author Brad Kimmel
 */
public final class PolygonMask2 implements Mask2 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -8489210119316141268L;
	
	/** The default lookup table size. */
	private static final int DEFAULT_GRID_SIZE = 100;

	/** The bounding <code>Box2</code> of the polygon. */
	private final Box2 boundingBox;
	
	/** The array of vertices of the polygon. */
	private final Point2[] vertices;
	
	/**
	 * A lookup table to quickly determine which segments cross which equally
	 * sized bins in the y-coordinate. 
	 */
	private final IntegerArray[] lookup;
	
	/**
	 * Creates a new <code>PolygonMask2</code>.
	 * @param vertices The <code>List</code> of vertices defining the polygon.
	 * @param gridSize The number of bins to divide the space occupied by the
	 * 		polygon into for the purposes of accelerating the point-in-polygon
	 * 		test.
	 */
	public PolygonMask2(List<Point2> vertices, int gridSize) {
		this.vertices = vertices.toArray(new Point2[vertices.size()]);
		this.lookup = new IntegerArray[gridSize];
		
		for (int i = 0; i < gridSize; i++) {
			lookup[i] = new IntegerArray();
		}
		
		BoundingBoxBuilder2 builder = new BoundingBoxBuilder2();
		for (int i = 0; i < this.vertices.length; i++) {
			builder.add(this.vertices[i]);
		}
		
		this.boundingBox = builder.getBoundingBox();
		double yMin = boundingBox.minimumY();
		double yLen = boundingBox.lengthY();
		int n = this.vertices.length;
		double yScale = lookup.length;
		
		for (int i = 0; i < n; i++) {
			int j = i + 1;
			if (j == n) { j = 0; }
			int a = MathUtil.clamp((int) Math.floor(yScale * (this.vertices[i].y() - yMin) / yLen), 0, lookup.length - 1); 
			int b = MathUtil.clamp((int) Math.floor(yScale * (this.vertices[j].y() - yMin) / yLen), 0, lookup.length - 1);
			if (a > b) {
				int temp = a;
				a = b;
				b = temp;
			}
			for (j = a; j <= b; j++) {
				lookup[j].add(i);
			}
		}
	}
		
	/**
	 * Creates a new <code>PolygonMask2</code>.
	 * @param vertices The <code>List</code> of vertices defining the polygon.
	 */
	public PolygonMask2(List<Point2> vertices) {
		this(vertices, DEFAULT_GRID_SIZE);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Mask2#opacity(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public double opacity(Point2 p) {
		if (boundingBox.contains(p)) {
			double yMin = boundingBox.minimumY();
			double yLen = boundingBox.lengthY();
			double yScale = lookup.length;
			int index = MathUtil.clamp((int) Math.floor(yScale * (p.y() - yMin) / yLen), 0, lookup.length - 1);
			boolean inside = false;
			for (int i = 0, n = lookup[index].size(); i < n; i++) {
				int j1 = lookup[index].get(i);
				int j2 = j1 < vertices.length - 1 ? j1 + 1 : 0;
				double x1 = vertices[j1].x() - p.x();
				double y1 = vertices[j1].y() - p.y();
				double x2 = vertices[j2].x() - p.x();
				double y2 = vertices[j2].y() - p.y();
				if ((y1 >= 0.0 && y2 < 0.0) || (y1 <= 0.0 && y2 > 0.0)) {
					if (x1 >= 0.0 && x2 > 0.0) {
						inside = !inside;
					} else if (x1 >= 0.0 || x2 > 0.0) {
						double x = x1 + (x2 - x1) * (0.0 - y1) / (y2 - y1);
						if (x >= 0.0) {
							inside = !inside;
						}
					}
				}
			}
			return inside ? 1.0 : 0.0;
		}
		return 0.0;
	}

}
