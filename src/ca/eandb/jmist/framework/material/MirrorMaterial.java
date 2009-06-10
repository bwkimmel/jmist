/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
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
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.ScatterRecorder)
	 */
	@Override
	public void scatter(Intersection x, ScatterRecorder recorder) {

		Vector3 out = Optics.reflect(x.incident(), x.shadingNormal());

		recorder.record(ScatterResult.specular(new Ray3(x.location(), out),
				reflectance.getColor(x)));

	}

	/**
	 * The reflectance <code>Painter</code> of this
	 * <code>MirrorMaterial</code>.
	 */
	private final Painter reflectance;

}
