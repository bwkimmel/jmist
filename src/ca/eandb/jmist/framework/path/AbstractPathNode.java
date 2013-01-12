/**
 *
 */
package ca.eandb.jmist.framework.path;

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
 * Abstract base class for <code>PathNode</code>s.
 * @author Brad Kimmel
 */
public abstract class AbstractPathNode implements PathNode {

	private final PathInfo pathInfo;
	
	private final double ru, rv, rj;

	protected AbstractPathNode(PathInfo pathInfo, double ru, double rv, double rj) {
		this.pathInfo = pathInfo;
		this.ru = ru;
		this.rv = rv;
		this.rj = rj;
	}
	
	public final double getRU() {
		return ru;
	}
	
	public final double getRV() {
		return rv;
	}
	
	public final double getRJ() {
		return rj;
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

	protected final ScatteringNode trace(ScatteredRay sr, double ru, double rv, double rj) {
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
			return new SurfaceNode(this, sr, context, ru, rv, rj);
		} else {
			return new BackgroundNode(this, sr, ru, rv, rj);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#expand(double, double, double)
	 */
	public final ScatteringNode expand(double ru, double rv, double rj) {
		return trace(sample(ru, rv, rj), ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isAtInfinity()
	 */
	public boolean isAtInfinity() {
		return getPosition().isVector();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnEyePath()
	 */
	public final boolean isOnEyePath() {
		return !isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPathInfo()
	 */
	public final PathInfo getPathInfo() {
		return pathInfo;
	}

}
