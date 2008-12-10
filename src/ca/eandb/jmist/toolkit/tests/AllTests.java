/**
 * 
 */
package ca.eandb.jmist.toolkit.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Tests everything in {@link ca.eandb.jmist.toolkit}.
 * @author Brad Kimmel
 */
@RunWith(Suite.class)
@SuiteClasses({
	AffineMatrix3Test.class,
	BoundingBoxBuilder3Test.class,
	Box2Test.class,
	Box3Test.class,
	CircleTest.class,
	ComplexTest.class,
	Grid3Test.class,
	IntervalTest.class,
	LinearMatrix3Test.class,
	OpticsTest.class,
	Point2Test.class,
	Point3Test.class,
	Ray2Test.class,
	Ray3Test.class,
	SphereTest.class,
	TupleTest.class,
	Vector2Test.class,
	Vector3Test.class
})
public class AllTests {

}
