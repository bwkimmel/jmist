/**
 *
 */
package ca.eandb.jmist.framework.texture;

import java.awt.image.Raster;

import ca.eandb.jmist.framework.PixelSpectrumFactory;
import ca.eandb.jmist.framework.Spectrum;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that is extrapolated from a <code>Raster</code>
 * image.
 * @author Brad Kimmel
 */
public final class RasterTexture2 implements Texture2 {

	/**
	 * Creates a new <code>RasterTexture2</code>.
	 * @param raster The <code>Raster</code> to use as the basis for the new
	 * 		<code>Texture2</code>.
	 * @param factory The <code>PixelSpectrumFactory</code> to use to create
	 * 		spectra from <code>Pixel</code>s.
	 */
	public RasterTexture2(Raster raster, PixelSpectrumFactory factory) {
		this.raster = raster;
		this.factory = factory;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.toolkit.Point2)
	 */
	public Spectrum evaluate(Point2 p) {

		int			w		= raster.getWidth();
		int			h		= raster.getHeight();
		int			x		= MathUtil.threshold((int) Math.floor(p.x() * (double) w), 0, w - 1);
		int			y		= MathUtil.threshold((int) Math.floor(p.y() * (double) h), 0, h - 1);
		double[]	pixel	= this.raster.getPixel(x, y, (double[]) null);

		return this.factory.createSpectrum(pixel);

	}

	/**
	 * The <code>Raster</code> that serves as the basis for this
	 * <code>Texture2</code>.
	 */
	private final Raster raster;

	/**
	 * The <code>PixelSpectrumFactory</code> to use to extrapolate
	 * <code>Pixel</code>s.
	 */
	private final PixelSpectrumFactory factory;

}
