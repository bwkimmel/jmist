/**
 *
 */
package ca.eandb.jmist.framework.shader.image;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * An image shader that uses a Lens to shade rays corresponding to points
 * on the image plane.
 * @author Brad Kimmel
 */
public final class CameraImageShader implements ImageShader {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 5027288502738131936L;

	/**
	 * Initializes the lens and ray shader to use to shade points on
	 * the image plane.
	 * @param lens The lens to use to generate rays corresponding to
	 * 		points on the image plane.
	 * @param rayShader The shader to use to shade rays.
	 */
	public CameraImageShader(Lens lens, RayShader rayShader) {
		this.lens = lens;
		this.rayShader = rayShader;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ImageShader#shadeAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color shadeAt(Point2 p, WavelengthPacket lambda) {
		ScatteredRay sr = lens.rayAt(p, lambda, Random.DEFAULT);
		if (sr != null) {
			Ray3 ray = sr.getRay();
			Color scale = sr.getColor();
			Color shade = rayShader.shadeRay(ray, lambda);
			return shade.times(scale);
		} else {
			return ColorUtil.getBlack(lambda);
		}
	}

	/**
	 * The lens to use to obtain rays corresponding to points on the
	 * image plane.
	 */
	private final Lens lens;

	/** The shader to use to shade rays. */
	private final RayShader rayShader;

}
