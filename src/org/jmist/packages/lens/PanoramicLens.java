/**
 *
 */
package org.jmist.packages.lens;

import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * A <code>Lens</code> that projects the scene onto a cylindrical virtual
 * screen.
 * @author bkimmel
 */
public final class PanoramicLens extends TransformableLens {

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 */
	public PanoramicLens() {
		this.hfov = DEFAULT_HORIZONTAL_FIELD_OF_VIEW;
		this.vfov = DEFAULT_VERTICAL_FIELD_OF_VIEW;
	}

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 */
	public PanoramicLens(double hfov) {
		this.hfov = hfov;
		this.vfov = DEFAULT_VERTICAL_FIELD_OF_VIEW;
	}

	/**
	 * Creates a new <code>PanoramicLens</code>.
	 * @param hfov The horizontal field of view (in radians).
	 * @param vfov The vertical field of view (in radians).
	 */
	public PanoramicLens(double hfov, double vfov) {
		this.hfov = hfov;
		this.vfov = vfov;
	}

	/** The default horizontal field of view (in radians). */
	public static final double DEFAULT_HORIZONTAL_FIELD_OF_VIEW = Math.PI;

	/** The default vertical field of view (in radians). */
	public static final double DEFAULT_VERTICAL_FIELD_OF_VIEW = Math.PI / 2.0;

	/* (non-Javadoc)
	 * @see org.jmist.packages.TransformableLens#viewRayAt(org.jmist.toolkit.Point2)
	 */
	@Override
	protected Ray3 viewRayAt(Point2 p) {

		double theta = (p.x() - 0.05) * hfov;
		double height = 2.0 * Math.tan(vfov / 2.0);

		return new Ray3(
				Point3.ORIGIN,
				new Vector3(
						Math.sin(theta),
						(0.5 - p.y()) * height,
						-Math.cos(theta)
				).unit()
		);

	}

	/** Horizontal field of view (in radians). */
	private final double hfov;

	/** Vertical field of view (in radians). */
	private final double vfov;

}
