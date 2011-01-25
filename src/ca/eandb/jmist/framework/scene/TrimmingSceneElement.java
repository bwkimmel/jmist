/**
 * 
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * A <code>SceneElement</code> decorator that trims the decorated
 * <code>SceneElement</code>.  Ray-intersection tests with the decorated
 * <code>SceneElement</code> only record a hit if the trimming
 * <code>Mask2</code> opacity is greater than <code>0.5</code>.
 * @author Brad Kimmel
 */
public final class TrimmingSceneElement extends SceneElementDecorator {

	/** Serialization version ID. */
	private static final long serialVersionUID = -208418976070459240L;

	/** The <code>SceneElement</code> to trim. */
	private final SceneElement inner;
	
	/** The <code>Mask2</code> to trim with. */
	private final Mask2 trim;
	
	/**
	 * @param inner
	 */
	public TrimmingSceneElement(Mask2 trim, SceneElement inner) {
		super(inner);
		this.inner = inner;
		this.trim = trim;		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		Interval I = recorder.interval();

		do {
			NearestIntersectionRecorder rec = new NearestIntersectionRecorder(I);
			super.intersect(index, ray, rec);

			if (rec.isEmpty()) {
				break;
			}
			Intersection x = rec.nearestIntersection();
			MinimalShadingContext ctx = new MinimalShadingContext(Random.DEFAULT);
			x.prepareShadingContext(ctx);
			Point2 uv = ctx.getUV();
			if (trim.opacity(new Point2(uv.x(), 1.0 - uv.y())) > 0.5) {
				recorder.record(x);
				if (!recorder.needAllIntersections()) {
					break;
				}
			}
			ray = ray.advance(x.getDistance());
			I = new Interval(0, I.maximum() - x.getDistance());
		} while (true);
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		Interval I = recorder.interval();

		do {
			NearestIntersectionRecorder rec = new NearestIntersectionRecorder(I);
			super.intersect(ray, rec);

			if (rec.isEmpty()) {
				break;
			}
			Intersection x = rec.nearestIntersection();
			MinimalShadingContext ctx = new MinimalShadingContext(Random.DEFAULT);
			x.prepareShadingContext(ctx);
			Point2 uv = ctx.getUV();
			if (trim.opacity(new Point2(uv.x(), 1.0 - uv.y())) > 0.5) {
				recorder.record(x);
				if (!recorder.needAllIntersections()) {
					break;
				}
			}
			ray = ray.advance(x.getDistance());
			I = new Interval(0, I.maximum() - x.getDistance());
		} while (true);

	}

}
