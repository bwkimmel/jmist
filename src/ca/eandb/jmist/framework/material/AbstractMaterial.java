/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Vector3;

/**
 * Provides default implementations for a <code>Material</code>.
 * @author Brad Kimmel
 */
public abstract class AbstractMaterial implements Material {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color emission(SurfacePointGeometry x, Vector3 out) {
		return ColorModel.getInstance().getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void emit(SurfacePointGeometry x, ScatterRecorder recorder) {
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
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.IntersectionGeometry, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(IntersectionGeometry x, ScatterRecorder recorder) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scattering(ca.eandb.jmist.framework.IntersectionGeometry, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color scattering(IntersectionGeometry x, Vector3 in) {
		return ColorModel.getInstance().getBlack();
	}

}
