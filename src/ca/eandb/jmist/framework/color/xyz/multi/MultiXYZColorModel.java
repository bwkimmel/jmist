/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.eandb.jmist.framework.color.xyz.multi;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.pdf.PiecewiseLinearProbabilityDensityFunction;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author brad
 *
 */
public final class MultiXYZColorModel extends ColorModel {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3425932421454424320L;

	private static final ProbabilityDensityFunction X_PDF = new PiecewiseLinearProbabilityDensityFunction(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.X_BAR);
	private static final ProbabilityDensityFunction Y_PDF = new PiecewiseLinearProbabilityDensityFunction(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Y_BAR);
	private static final ProbabilityDensityFunction Z_PDF = new PiecewiseLinearProbabilityDensityFunction(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Z_BAR);

	private static final double X_CONST = ColorUtil.LUMENS_PER_WATT
			* MathUtil.trapz(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.X_BAR);

	private static final double Y_CONST = ColorUtil.LUMENS_PER_WATT
			* MathUtil.trapz(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Y_BAR);

	private static final double Z_CONST = ColorUtil.LUMENS_PER_WATT
			* MathUtil.trapz(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Z_BAR);

	private final int channelsX;

	private final int channelsY;

	private final int channelsZ;

	private final XYZColor black;

	private final XYZColor white;

	/**
	 * This constructor is private because this class is a singleton.
	 */
	public MultiXYZColorModel() {
		this(5);
	}

	public MultiXYZColorModel(int channelsPerComponent) {
		this(channelsPerComponent, channelsPerComponent, channelsPerComponent);
	}

	public MultiXYZColorModel(int channelsX, int channelsY, int channelsZ) {
		this.channelsX = channelsX;
		this.channelsY = channelsY;
		this.channelsZ = channelsZ;
		this.black = new XYZColor(0, 0, 0, this);
		this.white = new XYZColor(1, 1, 1, this);
	}

	public int getChannelsX() {
		return channelsX;
	}

	public int getOffsetX() {
		return 0;
	}

	public int getChannelsY() {
		return channelsY;
	}

	public int getOffsetY() {
		return channelsX;
	}

	public int getChannelsZ() {
		return channelsZ;
	}

	public int getOffsetZ() {
		return channelsX + channelsY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Spectrum fromRGB(double r, double g, double b) {
		CIEXYZ xyz = ColorUtil.convertRGB2XYZ(r, g, b);
		return new XYZColor(xyz.X(), xyz.Y(), xyz.Z(), this);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromXYZ(double, double, double)
	 */
	@Override
	public Spectrum fromXYZ(double x, double y, double z) {
		return new XYZColor(x, y, z, this);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack()
	 */
	@Override
	public Spectrum getBlack() {
		return black;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getBlack(WavelengthPacket lambda) {
		return black.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getContinuous(ca.eandb.jmist.framework.Function1)
	 */
	@Override
	public Spectrum getContinuous(Function1 spectrum) {
		return new MultiXYZContinuousSpectrum(spectrum);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Spectrum getGray(double value) {
		return new XYZColor(value, value, value, this);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getGray(double value, WavelengthPacket lambda) {
		Spectrum s = new XYZColor(value, value, value, this);
		return s.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumChannels()
	 */
	@Override
	public int getNumChannels() {
		return channelsX + channelsY + channelsZ;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromChannels(double[], ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color fromArray(double[] values, WavelengthPacket lambda) {
		return fromArray(values, (MultiXYZWavelengthPacket) lambda);
	}

	public Color fromArray(double[] values, MultiXYZWavelengthPacket lambda) {
		if (values.length < getNumChannels()) {
			throw new IllegalArgumentException("values.length < getNumChannels()");
		}
		if (lambda != null) {
			return new MultiXYZColor(values.clone(), lambda);
		} else {
			double x = 0.0;
			double y = 0.0;
			double z = 0.0;
			for (int i = 0; i < channelsX; i++) {
				x += values[i];
			}
			for (int i = 0; i < channelsY; i++) {
				y += values[channelsX + i];
			}
			for (int i = 0; i < channelsZ; i++) {
				z += values[channelsX + channelsY + i];
			}
			x /= (double) channelsX;
			y /= (double) channelsY;
			z /= (double) channelsZ;
			return new XYZColor(x, y, z, this);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getWhite()
	 */
	@Override
	public Spectrum getWhite() {
		return white;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getWhite(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getWhite(WavelengthPacket lambda) {
		return white.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#sample(ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Color sample(Random random) {
		int n = getNumChannels();
		double[] lambda = new double[n];
		double[] values = new double[n];
		for (int i = 0, ofs = getOffsetX(); i < channelsX; i++) {
			lambda[ofs + i] = X_PDF.sample(random);
			values[ofs + i] = X_CONST;
		}
		for (int i = 0, ofs = getOffsetY(); i < channelsY; i++) {
			lambda[ofs + i] = Y_PDF.sample(random);
			values[ofs + i] = Y_CONST;
		}
		for (int i = 0, ofs = getOffsetZ(); i < channelsZ; i++) {
			lambda[ofs + i] = Z_PDF.sample(random);
			values[ofs + i] = Z_CONST;
		}
		MultiXYZWavelengthPacket wavelengths = new MultiXYZWavelengthPacket(lambda, this);
		return new MultiXYZColor(values, wavelengths);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#createRaster(int, int)
	 */
	@Override
	public Raster createRaster(int width, int height) {
		return new DoubleRaster(width, height, 3) {
			private static final long serialVersionUID = 8774357659017319631L;
			protected Color getPixel(double[] raster, int index) {
				return new XYZColor(raster[index], raster[index + 1], raster[index + 2], MultiXYZColorModel.this);
			}
			protected void addPixel(double[] raster, int index, Color pixel) {
				CIEXYZ xyz = pixel.toXYZ();
				raster[index] += xyz.X();
				raster[index + 1] += xyz.Y();
				raster[index + 2] += xyz.Z();
			}
			protected void setPixel(double[] raster, int index, Color pixel) {
				CIEXYZ xyz = pixel.toXYZ();
				raster[index] = xyz.X();
				raster[index + 1] = xyz.Y();
				raster[index + 2] = xyz.Z();
			}
		};
	}

}
