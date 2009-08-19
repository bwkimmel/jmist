/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Complex;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Vector3;

/**
 * Static utility methods for materials.
 * @author Brad Kimmel
 */
final class MaterialUtil {

	/**
	 * Computes the reflectance at an interface between two dielectrics for
	 * each channel.
	 * @param in The incident direction.
	 * @param n1 A <code>Color</code> whose channel values represent the
	 * 		refractive indices of the medium on the side of the interface
	 * 		pointed to by <code>normal</code>.
	 * @param n2 A <code>Color</code> whose channel values represent the
	 * 		refractive indices of the medium on the side of the interface
	 * 		pointed to by <code>normal.opposite()</code>.
	 * @param normal The interface normal.
	 * @return A <code>Color</code> whose channel values represent the
	 * 		reflectances at the interface.
	 * @see ca.eandb.jmist.math.Vector3#opposite()
	 */
	public static Color reflectance(Vector3 in, Color n1, Color n2, Vector3 normal) {
		ColorModel colorModel = n1.getColorModel();
		WavelengthPacket lambda = n1.getWavelengthPacket();

		double[] n1d = n1.toArray();
		double[] n2d = n2.toArray();

		for (int i = 0; i < n1d.length; i++) {
			n1d[i] = Optics.reflectance(in, n1d[i], n2d[i], normal);
		}

		return colorModel.fromArray(n1d, lambda);
	}

	/**
	 * Computes the reflectance at an interface between two conductors for
	 * each channel.
	 * @param in The incident direction.
	 * @param n1 A <code>Color</code> whose channel values represent the real
	 * 		part of the refractive indices of the medium on the side of the
	 * 		interface pointed to by <code>normal</code>.
	 * @param k1 A <code>Color</code> whose channel values represent the
	 * 		extinction indices (i.e., the imaginary part of the refractive
	 * 		indices) of the medium on the side of the interface pointed to by
	 * 		<code>normal</code>.  This value may be <code>null</code>.  If
	 * 		<code>k1 == null</code>, the extinction index is assumed to be zero
	 * 		on all channels.
	 * @param n2 A <code>Color</code> whose channel values represent the real
	 * 		part of the refractive indices of the medium on the side of the
	 * 		interface pointed to by <code>normal.opposite()</code>.
	 * @param k1 A <code>Color</code> whose channel values represent the
	 * 		extinction indices (i.e., the imaginary part of the refractive
	 * 		indices) of the medium on the side of the interface pointed to by
	 * 		<code>normal.opposite()</code>.  This value may be
	 * 		<code>null</code>.  If <code>k2 == null</code>, the extinction
	 * 		index is assumed to be zero on all channels.
	 * @param normal The interface normal.
	 * @return A <code>Color</code> whose channel values represent the
	 * 		reflectances at the interface.
	 * @see ca.eandb.jmist.math.Vector3#opposite()
	 */
	public static Color reflectance(Vector3 in, Color n1, Color k1, Color n2, Color k2, Vector3 normal) {
		ColorModel colorModel = n1.getColorModel();
		WavelengthPacket lambda = n1.getWavelengthPacket();

		double[] n1d = n1.toArray();
		double[] k1d = k1 != null ? k1.toArray() : null;
		double[] n2d = n2.toArray();
		double[] k2d = k2 != null ? k2.toArray() : null;

		for (int i = 0; i < n1d.length; i++) {
			n1d[i] = Optics.reflectance(in,
					new Complex(n1d[i], k1d != null ? k1d[i] : 0.0),
					new Complex(n2d[i], k2d != null ? k2d[i] : 0.0),
					normal);
		}

		return colorModel.fromArray(n1d, lambda);
	}

	/** Private constructor to prevent creation of instances. */
	private MaterialUtil() {}

}
