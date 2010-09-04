/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that always samples the same <code>Spectrum</code>.
 * 
 * @author Brad Kimmel
 */
public final class UniformTexture2 implements Texture2 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 5034772953605550946L;
	
	/** The constant <code>Spectrum</code> sampled by this texture. */
	private final Spectrum spectrum;
	
	/**
	 * Creates a new <code>UniformTexture2</code>.
	 * @param spectrum The constant <code>Spectrum</code> sampled by this
	 * 		texture.
	 */
	public UniformTexture2(Spectrum spectrum) {
		this.spectrum = spectrum;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		return spectrum.sample(lambda);
	}

}
