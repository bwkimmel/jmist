/**
 * 
 */
package ca.eandb.jmist.framework.shader.image;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * An <code>ImageShader</code> that adapts a <code>Texture2</code>.
 * @author Brad Kimmel
 */
public final class Texture2ImageShader implements ImageShader {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -5238069418249067354L;
	
	/** The <code>Texture2</code> to adapt. */
	private final Texture2 texture;
	
	/**
	 * Creates a new <code>Texture2ImageShader</code>.
	 * @param texture The <code>Texture2</code> to adapt.
	 */
	public Texture2ImageShader(Texture2 texture) {
		this.texture = texture;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ImageShader#shadeAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color shadeAt(Point2 p, WavelengthPacket lambda) {
		return texture.evaluate(p, lambda);
	}

}
