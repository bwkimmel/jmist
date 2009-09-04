/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Ray3;


/**
 * @author Brad
 *
 */
public abstract class AbstractPathNode implements PathNode {

	private final PathInfo pathInfo;

	protected AbstractPathNode(PathInfo pathInfo) {
		this.pathInfo = pathInfo;
	}

	protected final Color getWhite() {
		return ColorUtil.getWhite(pathInfo.getWavelengthPacket());
	}

	protected final Color getBlack() {
		return ColorUtil.getBlack(pathInfo.getWavelengthPacket());
	}

	protected final Color getGray(double value) {
		return ColorUtil.getGray(value, pathInfo.getWavelengthPacket());
	}

	protected final Color sample(Spectrum s) {
		return s.sample(pathInfo.getWavelengthPacket());
	}

	protected final ScatteringNode trace(ScatteredRay sr) {
		if (sr == null) {
			return null;
		}
		Scene scene = pathInfo.getScene();
		Ray3 ray = sr.getRay();
		SceneElement root = scene.getRoot();
		Intersection x = NearestIntersectionRecorder
				.computeNearestIntersection(ray, root);
		if (x != null) {
			ShadingContext context = new MinimalShadingContext(Random.DEFAULT);
			x.prepareShadingContext(context);
			context.getModifier().modify(context);
			return new SurfaceNode(this, sr, context);
		} else {
			return new BackgroundNode(this, sr);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#expand(ca.eandb.jmist.framework.Random)
	 */
	public final ScatteringNode expand(Random rnd) {
		return trace(sample(rnd));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isAtInfinity()
	 */
	public boolean isAtInfinity() {
		return getPosition().isVector();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnEyePath()
	 */
	public final boolean isOnEyePath() {
		return !isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#getPathInfo()
	 */
	public final PathInfo getPathInfo() {
		return pathInfo;
	}

}
