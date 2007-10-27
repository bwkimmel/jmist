/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.ImageShader;
import org.jmist.toolkit.Box2;
import org.jmist.toolkit.Pixel;

/**
 * @author bkimmel
 *
 */
public final class SimplePixelShader extends ImageRasterizingPixelShader {

	public SimplePixelShader(ImageShader camera) {
		super(camera);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.PixelShader#shadePixel(org.jmist.toolkit.Box2, org.jmist.toolkit.Pixel)
	 */
	public void shadePixel(Box2 bounds, Pixel pixel) {
		this.shadeAt(bounds.center(), pixel);
	}

}
