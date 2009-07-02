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

package ca.eandb.jmist.framework.color.polychrome;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Tuple;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * @author brad
 *
 */
public final class PolychromeColorModel extends ColorModel {

	private static final LinearMatrix3 XYZ_TO_sRGBLin = new LinearMatrix3(
			 3.2410, -1.5374, -0.4986,
			-0.9692,  1.8760,  0.0416,
			 0.0556, -0.2040,  1.0570);

	private final Tuple wavelengths;

	private final WavelengthPacket lambda = new WavelengthPacket() {
		public ColorModel getColorModel() {
			return PolychromeColorModel.this;
		}
	};

	private final PolychromeColor black = new PolychromeColor();

	private final PolychromeColor white = new PolychromeColor();

	private final class PolychromeColor implements Color, Spectrum {

		private final double[] values = new double[wavelengths.size()];

		@Override
		public Color clamp(double max) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.min(values[i], max);
			}
			return result;
		}

		@Override
		public Color clamp(double min, double max) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.min(Math.max(values[i], min), max);
			}
			return result;
		}

		@Override
		public Color disperse(int channel) {
			PolychromeColor result = new PolychromeColor();
			result.values[channel] = values[channel];
			return result;
		}

		@Override
		public Color divide(Color other) {
			return divide((PolychromeColor) other);
		}

		public PolychromeColor divide(PolychromeColor other) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] / other.values[i];
			}
			return result;
		}

		@Override
		public Color divide(double c) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] / c;
			}
			return result;
		}

		@Override
		public Color exp() {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.exp(values[i]);
			}
			return result;
		}

		@Override
		public ColorModel getColorModel() {
			return PolychromeColorModel.this;
		}

		@Override
		public double getValue(int channel) {
			return values[channel];
		}

		@Override
		public WavelengthPacket getWavelengthPacket() {
			return lambda;
		}

		@Override
		public Color invert() {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = 1.0 / values[i];
			}
			return result;
		}

		@Override
		public Color minus(Color other) {
			return minus((PolychromeColor) other);
		}

		public PolychromeColor minus(PolychromeColor other) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] - other.values[i];
			}
			return result;
		}

		@Override
		public Color negative() {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = -values[i];
			}
			return result;
		}

		@Override
		public Color plus(Color other) {
			return plus((PolychromeColor) other);
		}

		public PolychromeColor plus(PolychromeColor other) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] + other.values[i];
			}
			return result;
		}

		@Override
		public Color pow(Color other) {
			return pow((PolychromeColor) other);
		}

		public PolychromeColor pow(PolychromeColor other) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.pow(values[i], other.values[i]);
			}
			return result;
		}

		@Override
		public Color pow(double e) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.pow(values[i], e);
			}
			return result;

		}

		@Override
		public Color sqrt() {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = Math.sqrt(values[i]);
			}
			return result;
		}

		@Override
		public Color times(Color other) {
			return times((PolychromeColor) other);
		}

		public PolychromeColor times(PolychromeColor other) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] * other.values[i];
			}
			return result;
		}

		@Override
		public Color times(double c) {
			PolychromeColor result = new PolychromeColor();
			for (int i = 0; i < values.length; i++) {
				result.values[i] = values[i] * c;
			}
			return result;
		}

		@Override
		public double[] toArray() {
			return values.clone();
		}

		@Override
		public Color sample(WavelengthPacket lambda) {
			return this;
		}

	};

	public PolychromeColorModel(Tuple wavelengths) {
		this.wavelengths = wavelengths;
		ArrayUtil.setAll(white.values, 1.0);
	}

	public PolychromeColorModel(double[] wavelengths) {
		this(new Tuple(wavelengths));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Spectrum fromRGB(double r, double g, double b) {
		PolychromeColor result = new PolychromeColor();
		for (int i = 0; i < result.values.length; i++) {
			double lambda = wavelengths.at(i);
			if (lambda >= 380e-9 && lambda < 780e-9) {
				if (lambda < 500e-9) {
					result.values[i] = b;
				} else if (lambda >= 600e-9) {
					result.values[i] = r;
				} else {
					result.values[i] = g;
				}
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromXYZ(double, double, double)
	 */
	@Override
	public Spectrum fromXYZ(double x, double y, double z) {
		Vector3 xyz = new Vector3(x, y, z);
		Vector3 rgb = XYZ_TO_sRGBLin.times(xyz);
		return fromRGB(
				delinearize(rgb.x()),
				delinearize(rgb.y()),
				delinearize(rgb.z()));
	}

	private double delinearize(double c) {
		if (c <= 0.0031308) {
			return 12.92 * c;
		} else {
			return 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
		}
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
		return black;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getContinuous(ca.eandb.jmist.framework.Function1)
	 */
	@Override
	public Spectrum getContinuous(Function1 spectrum) {
		PolychromeColor result = new PolychromeColor();
		for (int i = 0; i < result.values.length; i++) {
			result.values[i] = spectrum.evaluate(wavelengths.at(i));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Spectrum getGray(double value) {
		PolychromeColor result = new PolychromeColor();
		ArrayUtil.setAll(result.values, value);
		return result;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getGray(double value, WavelengthPacket lambda) {
		PolychromeColor result = new PolychromeColor();
		ArrayUtil.setAll(result.values, value);
		return result;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumChannels()
	 */
	@Override
	public int getNumChannels() {
		return wavelengths.size();
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
		return white;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#sample()
	 */
	@Override
	public Color sample() {
		return white;
	}

}
