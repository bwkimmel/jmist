/**
 *
 */
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a spherical virtual screen.
 * @author Brad Kimmel
 */
public final class SphericalLens extends TransformableLens {

	/**
	 * Creates a new <code>SphericalLens</code>.
	 */
	public SphericalLens() {
		this.hfov = DEFAULT_HORIZONTAL_FIELD_OF_VIEW;
		this.vfov = DEFAULT_VERTICAL_FIELD_OF_VIEW;
	}

	/**
	 * Creates a new <code>SphericalLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 * @param vfov The vertical field of view (in radians).
	 */
	public SphericalLens(double hfov, double vfov) {
		this.hfov = hfov;
		this.vfov = vfov;
	}

	/** The default horizontal field of view (in radians). */
	public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = 2.0 * Math.PI;

	/** The default vertical field of view (in radians). */
	public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.TransformableLens#viewRayAt(ca.eandb.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

	    double		nx = (0.5 - p.x()) * hfov;
	    double		ny = (0.5 - p.y()) * vfov;

	    Vector3		dir = Vector3.NEGATIVE_K;

	    /* TODO: optimize, using rotation matrices is not necessary. */
	    dir = LinearMatrix3.rotateXMatrix(ny).times(dir);
	    dir = LinearMatrix3.rotateYMatrix(nx).times(dir);

	    return new Ray3(Point3.ORIGIN, dir);

	}

	/** Horizontal field of view (in radians). */
	private final double hfov;

	/** Vertical field of view (in radians). */
	private final double vfov;

}
