/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Spectrum;

/**
 * @author Brad
 *
 */
public final class MonochromaticColorModel extends ColorModel {

	private final double wavelength;

	private final Sample BLACK = new Sample(0);

	private final Sample UNIT = new Sample(1);

	/**
	 * @author Brad
	 *
	 */
	private final class Sample implements Color {

		private final double value;

		public Sample(double value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public Color divide(Color other) {
			return divide((Sample) other);
		}

		public Sample divide(Sample other) {
			return new Sample(value / other.value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#divide(double)
		 */
		@Override
		public Color divide(double c) {
			return new Sample(value / c);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#exp()
		 */
		@Override
		public Color exp() {
			return new Sample(Math.exp(value));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#invert()
		 */
		@Override
		public Color invert() {
			return new Sample(1.0 / value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public Color minus(Color other) {
			return minus((Sample) other);
		}

		public Sample minus(Sample other) {
			return new Sample(value - other.value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#negative()
		 */
		@Override
		public Color negative() {
			return new Sample(-value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public Color plus(Color other) {
			return plus((Sample) other);
		}

		public Sample plus(Sample other) {
			return new Sample(value + other.value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public Color pow(Color other) {
			return pow((Sample) other);
		}

		public Sample pow(Sample other) {
			return new Sample(Math.pow(value, other.value));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#pow(double)
		 */
		@Override
		public Color pow(double e) {
			return new Sample(Math.pow(value, e));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#sqrt()
		 */
		@Override
		public Color sqrt() {
			return new Sample(Math.sqrt(value));
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
		 */
		@Override
		public Color times(Color other) {
			return times((Sample) other);
		}

		public Sample times(Sample other) {
			return new Sample(value * other.value);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#times(double)
		 */
		@Override
		public Color times(double c) {
			return new Sample(value * c);
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#disperse(int)
		 */
		@Override
		public Color disperse(int channel) {
			if (channel != 0) {
				throw new IndexOutOfBoundsException();
			}
			return this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#getColorModel()
		 */
		@Override
		public ColorModel getColorModel() {
			return MonochromaticColorModel.this;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#getValue(int)
		 */
		@Override
		public double getValue(int channel) {
			if (channel != 0) {
				throw new IndexOutOfBoundsException();
			}
			return value;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.color.Color#toArray()
		 */
		@Override
		public double[] toArray() {
			return new double[]{ value };
		}

	}

	/**
	 * @param wavelength
	 */
	public MonochromaticColorModel(double wavelength) {
		this.wavelength = wavelength;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Color fromRGB(double r, double g, double b) {
		// FIXME
		return new Sample(g);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromSpectrum(ca.eandb.jmist.framework.Spectrum)
	 */
	@Override
	public Color fromSpectrum(Spectrum spectrum) {
		return new Sample(spectrum.sample(wavelength));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack()
	 */
	@Override
	public Color getBlack() {
		return BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Color getGray(double value) {
		return new Sample(value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getUnit()
	 */
	@Override
	public Color getWhite() {
		return UNIT;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumBands()
	 */
	@Override
	public int getNumChannels() {
		return 1;
	}

}
