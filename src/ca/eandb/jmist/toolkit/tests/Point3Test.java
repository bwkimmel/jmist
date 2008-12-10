/**
 *
 */
package ca.eandb.jmist.toolkit.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import ca.eandb.jmist.toolkit.*;
import ca.eandb.jmist.util.*;

/**
 * Tests for {@link ca.eandb.jmist.toolkit.Point3}.
 * @author Brad Kimmel
 *
 */
public class Point3Test {

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#Point3(double, double, double)}.
	 */
	@Test
	public final void testPoint3() {
		Point3 p = new Point3(1.0, 2.0, 3.0);

		assertEquals(1.0, p.x());
		assertEquals(2.0, p.y());
		assertEquals(3.0, p.z());
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#squaredDistanceTo(ca.eandb.jmist.toolkit.Point3)}.
	 */
	@Test
	public final void testSquaredDistanceTo() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Point3 q = new Point3(2.0, 4.0, 8.0);
		double d = (1.0 * 1.0) + (2.0 * 2.0) + (5.0 * 5.0);

		assertEquals(d, p.squaredDistanceTo(q), MathUtil.TINY_EPSILON);
		assertEquals(d, q.squaredDistanceTo(p), MathUtil.TINY_EPSILON);

		assertEquals(0.0, p.squaredDistanceTo(p));
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#distanceTo(ca.eandb.jmist.toolkit.Point3)}.
	 */
	@Test
	public final void testDistanceTo() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Point3 q = new Point3(2.0, 4.0, 8.0);
		double d = Math.sqrt((1.0 * 1.0) + (2.0 * 2.0) + (5.0 * 5.0));

		assertEquals(d, p.distanceTo(q), MathUtil.TINY_EPSILON);
		assertEquals(d, q.distanceTo(p), MathUtil.TINY_EPSILON);

		assertEquals(0.0, p.distanceTo(p));
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#vectorTo(ca.eandb.jmist.toolkit.Point3)}.
	 */
	@Test
	public final void testVectorTo() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Point3 q = new Point3(-1.0, -2.0, -3.0);

		Vector3 pq = p.vectorTo(q);
		Vector3 qp = q.vectorTo(p);

		assertEquals(-2.0, pq.x());
		assertEquals(-4.0, pq.y());
		assertEquals(-6.0, pq.z());

		assertEquals(2.0, qp.x());
		assertEquals(4.0, qp.y());
		assertEquals(6.0, qp.z());

		Vector3 O = p.vectorTo(p);

		assertEquals(0.0, O.length(), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#vectorFrom(ca.eandb.jmist.toolkit.Point3)}.
	 */
	@Test
	public final void testVectorFrom() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Point3 q = new Point3(-1.0, -2.0, -3.0);

		Vector3 qp = p.vectorFrom(q);
		Vector3 pq = q.vectorFrom(p);

		assertEquals(-2.0, pq.x());
		assertEquals(-4.0, pq.y());
		assertEquals(-6.0, pq.z());

		assertEquals(2.0, qp.x());
		assertEquals(4.0, qp.y());
		assertEquals(6.0, qp.z());

		Vector3 O = p.vectorFrom(p);

		assertEquals(0.0, O.length(), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#plus(ca.eandb.jmist.toolkit.Vector3)}.
	 */
	@Test
	public final void testPlus() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Vector3 v = new Vector3(-1.0, 2.0, 4.0);

		Point3 q = p.plus(v);

		assertEquals(0.0, q.x());
		assertEquals(4.0, q.y());
		assertEquals(7.0, q.z());

		q = p.plus(Vector3.ZERO);

		assertEquals(0.0, p.distanceTo(q), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#minus(ca.eandb.jmist.toolkit.Vector3)}.
	 */
	@Test
	public final void testMinus() {
		Point3 p = new Point3(1.0, 2.0, 3.0);
		Vector3 v = new Vector3(-1.0, 2.0, 4.0);

		Point3 q = p.minus(v);

		assertEquals(2.0, q.x());
		assertEquals(0.0, q.y());
		assertEquals(-1.0, q.z());

		q = p.minus(Vector3.ZERO);

		assertEquals(0.0, p.distanceTo(q), MathUtil.TINY_EPSILON);
	}

	/**
	 * Test method for {@link ca.eandb.jmist.toolkit.Point3#ORIGIN}.
	 */
	@Test
	public final void testOrigin() {
		assertEquals(0.0, Point3.ORIGIN.x());
		assertEquals(0.0, Point3.ORIGIN.y());
		assertEquals(0.0, Point3.ORIGIN.z());
	}

}
