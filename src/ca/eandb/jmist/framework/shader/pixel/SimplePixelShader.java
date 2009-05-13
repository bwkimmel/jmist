/**
 *
 */
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.math.Box2;

/**
 * @author Brad Kimmel
 *
 */
public final class SimplePixelShader extends ImageRasterizingPixelShader {

	public SimplePixelShader(ImageShader camera) {
		super(camera);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.toolkit.Box2, double[])
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		return this.shadeAt(bounds.center(), pixel);
	}

}
