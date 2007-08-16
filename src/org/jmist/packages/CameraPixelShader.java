/**
 * 
 */
package org.jmist.packages;

import org.jmist.toolkit.Pixel;
import org.jmist.toolkit.Point2;
import org.jmist.framework.IImageShader;
import org.jmist.framework.IPixelShader;

/**
 * Represents a pixel shader that uses a camera to shade points
 * on the image plane.  The inheriting pixel shader's sole
 * responsibility will be anti-aliasing.
 * @author bkimmel
 */
public abstract class CameraPixelShader implements IPixelShader {

	/**
	 * Initializes the camera to use for this pixel shader.
	 * @param camera The camera to use for this pixel shader.
	 */
	protected CameraPixelShader(IImageShader camera) {
		this.camera = camera;
	}
	
	/**
	 * Shades the specified pixel using this shader's
	 * camera.
	 * @param p The point on the image plane to shade.
	 * @param pixel The pixel to write the shading to.
	 */
	protected void shadeAt(Point2 p, Pixel pixel) {
		this.camera.shadeAt(p, pixel);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IPixelFactory#createPixel()
	 */
	public Pixel createPixel() {
		return this.camera.createPixel();
	}
	
	/** The camera to use for shading points. */
	private final IImageShader camera;
	
}
