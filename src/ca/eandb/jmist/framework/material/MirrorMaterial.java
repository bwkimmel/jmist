/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An ideally reflective <code>Material</code>.
 * @author Brad Kimmel
 */
public final class MirrorMaterial extends OpaqueMaterial {

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Painter</code> of this mirror.
	 */
	public MirrorMaterial(Painter reflectance) {
		this.reflectance = reflectance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.IntersectionGeometry, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(IntersectionGeometry x, ScatteredRayRecorder recorder) {

		Vector3 out = Optics.reflect(x.getIncident(), x.getShadingNormal());

		recorder.add(ScatteredRay.specular(new Ray3(x.getPosition(), out),
				reflectance.getColor(x)));

	}

	/**
	 * The reflectance <code>Painter</code> of this
	 * <code>MirrorMaterial</code>.
	 */
	private final Painter reflectance;

}
