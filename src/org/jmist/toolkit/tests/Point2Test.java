/**
 *
 */
package org.jmist.toolkit.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jmist.toolkit.*;
import org.jmist.util.*;

/**
 * Tests for {@link org.jmist.toolkit.Point2}.
 * @author bkimmel
 */
public class Point2Test {

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#Point2(double, double)}.
	 */
	@Test
	public final void testPoint2() {
		Point2 p = new Point2(1.0, 2.0);
		assertEquals(1.0, p.x());
		assertEquals(2.0, p.y());
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#getX()}.
	 */
	@Test
	public final void testGetX() {
		Point2 p = new Point2(3.0, 4.0);
		assertEquals(3.0, p.getX());
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#getY()}.
	 */
	@Test
	public final void testGetY() {
		Point2 p = new Point2(5.0, 6.0);
		assertEquals(6.0, p.getY());
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#squaredDistanceTo(org.jmist.toolkit.Point2)}.
	 */
	@Test
	public final void testSquaredDistanceTo() {
		Point2 p = new Point2(1.0, 2.0);
		Point2 q = new Point2(5.0, 8.0);

		assertEquals((4.0 * 4.0) + (6.0 * 6.0), p.squaredDistanceTo(q), MathUtil.TINY_EPSILON);
		assertEquals((4.0 * 4.0) + (6.0 * 6.0), q.squaredDistanceTo(p), MathUtil.TINY_EPSILON);

		assertEquals(0.0, p.squaredDistanceTo(p), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#distanceTo(org.jmist.toolkit.Point2)}.
	 */
	@Test
	public final void testDistanceTo() {
		Point2 p = new Point2(1.0, 2.0);
		Point2 q = new Point2(-2.0, 6.0);

		assertEquals(5.0, p.distanceTo(q), MathUtil.TINY_EPSILON);
		assertEquals(5.0, q.distanceTo(p), MathUtil.TINY_EPSILON);

		assertEquals(0.0, p.distanceTo(p), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#vectorTo(org.jmist.toolkit.Point2)}.
	 */
	@Test
	public final void testVectorTo() {
		Point2 p = new Point2(1.0, 2.0);
		Point2 q = new Point2(-2.0, 6.0);

		Vector2 pq = p.vectorTo(q);
		Vector2 qp = q.vectorTo(p);

		assertEquals(-3.0, pq.x());
		assertEquals(4.0, pq.y());

		assertEquals(3.0, qp.x());
		assertEquals(-4.0, qp.y());

		Vector2 O = p.vectorTo(p);

		assertEquals(0.0, O.length(), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#vectorFrom(org.jmist.toolkit.Point2)}.
	 */
	@Test
	public final void testVectorFrom() {
		Point2 p = new Point2(1.0, 2.0);
		Point2 q = new Point2(-2.0, 6.0);

		Vector2 qp = p.vectorFrom(q);
		Vector2 pq = q.vectorFrom(p);

		assertEquals(-3.0, pq.x());
		assertEquals(4.0, pq.y());

		assertEquals(3.0, qp.x());
		assertEquals(-4.0, qp.y());

		Vector2 O = p.vectorFrom(p);

		assertEquals(0.0, O.length(), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#plus(org.jmist.toolkit.Vector2)}.
	 */
	@Test
	public final void testPlus() {
		Point2 p = new Point2(2.0, -3.0);
		Vector2 v = new Vector2(-2.0, 4.0);

		Point2 q = p.plus(v);

		assertEquals(0.0, q.x());
		assertEquals(1.0, q.y());

		q = p.plus(Vector2.ZERO);

		assertEquals(0.0, p.distanceTo(q));
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#minus(org.jmist.toolkit.Vector2)}.
	 */
	@Test
	public final void testMinus() {
		Point2 p = new Point2(2.0, -3.0);
		Vector2 v = new Vector2(-2.0, 4.0);

		Point2 q = p.minus(v);

		assertEquals(4.0, q.x());
		assertEquals(-7.0, q.y());

		q = p.minus(Vector2.ZERO);

		assertEquals(0.0, p.distanceTo(q));
	}

	/**
	 * Test method for {@link org.jmist.toolkit.Point2#ORIGIN}.
	 */
	@Test
	public final void testOrigin() {
		assertEquals(0.0, Point2.ORIGIN.x());
		assertEquals(0.0, Point2.ORIGIN.y());
	}

}
