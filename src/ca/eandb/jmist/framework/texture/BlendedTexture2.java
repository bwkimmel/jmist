/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2<code> that combines two other <code>Texture2</code>s using
 * a <code>Mask2</code>.
 * 
 * At areas where the mask value is zero, the first texture is used.  Where the
 * mask value is one, the second texture is used.  For other areas, the texture
 * is interpolated between the first and second textures.
 * 
 * @author Brad Kimmel
 */
public final class BlendedTexture2 implements Texture2 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 3825212205211905179L;

	/** The <code>Texture2</code> to use where the mask value is zero. */
	private final Texture2 a;
	
	/** The <code>Texture2</code> to use where the mask value is one. */
	private final Texture2 b;
	
	/** The <code>Mask2</code> that controls the interpolation. */
	private final Mask2 mask;

	/**
	 * Creates a new <code>BlendedTexture2</code>.
	 * @param a The <code>Texture2</code> to use where the mask value is zero.
	 * @param b The <code>Texture2</code> to use where the mask value is one.
	 * @param mask The <code>Mask2</code> that controls the interpolation.
	 */
	public BlendedTexture2(Texture2 a, Texture2 b, Mask2 mask) {
		super();
		this.a = a;
		this.b = b;
		this.mask = mask;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		double t = mask.opacity(p);
		return a.evaluate(p, lambda).times(1.0 - t).plus(
				b.evaluate(p, lambda).times(t));
	}

}
