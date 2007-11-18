/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ImageShader;
import org.jmist.toolkit.Box2;

/**
 * @author bkimmel
 *
 */
public final class SimplePixelShader extends ImageRasterizingPixelShader {

	public SimplePixelShader(ImageShader camera) {
		super(camera);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelShader#shadePixel(org.jmist.toolkit.Box2, double[])
	 */
	public double[] shadePixel(Box2 bounds, double[] pixel) {
		return this.shadeAt(bounds.center(), pixel);
	}

}
