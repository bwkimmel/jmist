/**
 *
 */
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * Represents a pixel shader that rasterizes an image represented by
 * an image shader.  The inheriting pixel shader's sole responsibility
 * will be anti-aliasing.
 * @author Brad Kimmel
 */
public abstract class ImageRasterizingPixelShader implements PixelShader {

	/**
	 * Initializes the image shader to use for this pixel shader.
	 * @param shader The <code>ImageShader</code> to use for this pixel shader.
	 * @param model The <code>ColorModel</code> to use for sampling in te
	 * 		wavelength domain.
	 */
	protected ImageRasterizingPixelShader(ImageShader shader, ColorModel model) {
		this.shader = shader;
		this.model = model;
	}

	/**
	 * Shades the specified pixel using this shader's image shader.
	 * @param p The point on the image plane to shade.
	 * @return The shaded pixel.
	 */
	protected Color shadeAt(Point2 p) {
		Color				sample = model.sample(Random.DEFAULT);
		WavelengthPacket	lambda = sample.getWavelengthPacket();

		Color				shade = shader.shadeAt(p, lambda);
		return shade.times(sample);
	}

	/** The <code>ImageShader</code> to use for shading points. */
	private final ImageShader shader;

	/**
	 * The <code>ColorModel</code> to use for sampling in the wavelength
	 * domain.
	 */
	private final ColorModel model;

}
