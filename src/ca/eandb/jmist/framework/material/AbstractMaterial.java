/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Vector3;

/**
 * Provides default implementations for a <code>Material</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractMaterial implements Material {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color emission(SurfacePoint x, Vector3 out) {
		return ColorModel.getInstance().getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void emit(SurfacePoint x, ScatteredRayRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(Intersection x, ScatteredRayRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scattering(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color scattering(Intersection x, Vector3 in) {
		return ColorModel.getInstance().getBlack();
	}

}
