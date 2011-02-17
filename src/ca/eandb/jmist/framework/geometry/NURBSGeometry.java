/**
 * 
 */
package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.geometry.nurbs.NURBS;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.LinearMatrix2;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector2;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.DoubleArray;

/**
 * @author Brad
 *
 */
public final class NURBSGeometry extends AbstractGeometry {
	
	private final NURBS surf;
	
	private final double tolerance;
	
	private final int maxIterations;
	
	private final double flatness;
	
	private final DoubleArray sgrid;
	
	private final DoubleArray tgrid;
	
	

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	@Override
	public int getNumPrimitives() {
		return (sgrid.size() - 1) * (tgrid.size() - 1);
	}
	
	private void intersectRegion(int surfaceId, Box2 region, Ray3 ray, IntersectionRecorder recorder) {
		
		Plane3	ray1 = Plane3.throughPoint(ray.origin(), ray.direction().perp());
		Plane3	ray2 = Plane3.throughPoint(ray.origin(), ray1.normal().cross(ray.direction()));
		
		Interval I = recorder.interval();
		Point2	initialGuess = region.center();
		Point2	guess = initialGuess;
		double	oldNormF = Double.POSITIVE_INFINITY;
		Box2	domain = surf.domain();

		assert(domain.contains(initialGuess));
		
		for (int i = 0; i < maxIterations; i++) {

			Point3	S;			// surface point
			Vector3	Su, Sv;		// tangent vectors
			
			// evaluate at guess
			AffineMatrix3 frame = surf.evaluateFrame(guess);
			S = frame.times(Point3.ORIGIN);
			Su = frame.times(Vector3.I);
			Sv = frame.times(Vector3.J);

			// check if result is close enough
			Vector2	F = new Vector2(ray1.altitude(S), ray2.altitude(S));
			double	normF = F.length();	//! \todo optimize?, can use squared norm
			
			if (normF < tolerance) {	// result is close enough (SUCCESS)

				double	t = ray.origin().distanceTo(S) / ray.direction().length();
				
				if (!recorder.interval().contains(t)) {	// reject intersection if not in given interval
					return;
				}
					
				// we have a recorder, so record the intersection
				Vector3	n = frame.times(Vector3.K);
				boolean	enter = ray.direction().dot(n) < 0.0;
				
				//! \todo optimize, we are computing n twice (once above, once in FromUV)
				//! \todo closed flag should not always be false
				Intersection x = super.newIntersection(ray, t, enter, surfaceId)
					.setLocation(S)
					.setBasis(Basis3.fromWUV(n, Su, Sv))
					.setUV(guess);
				
				recorder.record(x);				
				return;
				
			} else {	// result is not close enough
				
				if (normF > oldNormF) {
					return;		// result is furthur away than last result! quit
				}
				
				// compute the next guess
				LinearMatrix2	J = new LinearMatrix2(
										ray1.normal().dot(Su), 
										ray1.normal().dot(Sv), 
										ray2.normal().dot(Su), 
										ray2.normal().dot(Sv));
				
				if (Math.abs(J.determinant()) < tolerance) {	// we hit a singularity
					// jitter the current guess to get the next guess
					guess = guess.plus(
							guess.vectorTo(initialGuess).times(0.1 * RandomUtil.canonical(Random.DEFAULT)));
				} else {	// no singularity
					guess = guess.minus(J.inverse().times(F));
				}
				
				// if the next guess is outside the domain, move it to
				// the closest point just inside the domain
				if (!domain.expand(-1e-12).contains(guess))
					guess = domain.expand(-1e-12).closestPointTo(guess);

				oldNormF = normF;
				
			}
		}

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	@Override
	public Box3 getBoundingBox(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	@Override
	public Sphere getBoundingSphere(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
