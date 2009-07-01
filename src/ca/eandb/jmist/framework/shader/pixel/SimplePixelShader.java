/**
 *
 */
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Box2;

/**
 * @author Brad Kimmel
 *
 */
public final class SimplePixelShader extends ImageRasterizingPixelShader {

	public SimplePixelShader(ImageShader camera, ColorModel model) {
		super(camera, model);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.math.Box2)
	 */
	public Color shadePixel(Box2 bounds) {
		return shadeAt(bounds.center());
	}

}
