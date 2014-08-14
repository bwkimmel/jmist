/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * Spectral power distribution for CIE Illuminant D.
 * @see http://en.wikipedia.org/wiki/Standard_illuminant#Illuminant_D
 */
public final class CIEIlluminantDSpectrum implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5032520267769162406L;

  /** CIE Illuminant D50 spectral power distribution. */
  public static final CIEIlluminantDSpectrum D50 = fromNominalColorTemperature(5000);

  /** CIE Illuminant D55 spectral power distribution. */
  public static final CIEIlluminantDSpectrum D55 = fromNominalColorTemperature(5500);

  /** CIE Illuminant D65 spectral power distribution. */
  public static final CIEIlluminantDSpectrum D65 = fromNominalColorTemperature(6500);

  /** CIE Illuminant D75 spectral power distribution. */
  public static final CIEIlluminantDSpectrum D75 = fromNominalColorTemperature(7500);

  /** Valid correlated color temperature range. */
  private static final Interval COLOR_TEMPERATURE_RANGE = new Interval(4000, 25000);

  private static final double[] WAVELENGTHS = ArrayUtil.range(300e-9, 830e-9, 107);

  /** D-illuminant mean spectral distribution. */
  private static final Function1 S0 = new PiecewiseLinearFunction1(WAVELENGTHS, new double[] {
      0.04,   3.02,   6.00,  17.80,  29.60,  42.45,  55.30,  56.30,  57.30,  59.55, // 300-345nm
     61.80,  61.65,  61.50,  65.15,  68.80,  66.10,  63.40,  64.60,  65.80,  80.30, // 350-395nm
     94.80,  99.80, 104.80, 105.35, 105.90, 101.35,  96.80, 105.35, 113.90, 119.75, // 400-445nm
    125.60, 125.55, 125.50, 123.40, 121.30, 121.30, 121.30, 117.40, 113.50, 113.30, // 450-495nm
    113.10, 111.95, 110.80, 108.65, 106.50, 107.65, 108.80, 107.05, 105.30, 104.85, // 500-545nm
    104.40, 102.20, 100.00,  98.00,  96.00,  95.55,  95.10,  92.10,  89.10,  89.80, // 550-595nm
     90.50,  90.40,  90.30,  89.35,  88.40,  86.20,  84.00,  84.55,  85.10,  83.50, // 600-645nm
     81.90,  82.25,  82.60,  83.75,  84.90,  83.10,  81.30,  76.60,  71.90,  73.10, // 650-695nm
     74.30,  75.35,  76.40,  69.85,  63.30,  67.50,  71.70,  74.35,  77.00,  71.10, // 700-745nm
     65.20,  56.45,  47.70,  58.15,  68.60,  66.80,  65.00,  65.50,  66.00,  63.50, // 750-795nm
     61.00,  57.15,  53.30,  56.10,  58.90,  60.40,  61.90                          // 800-830nm
  });

  /** D-illuminant first principal component. */
  private static final Function1 S1 = new PiecewiseLinearFunction1(WAVELENGTHS, new double[] {
      0.02,   2.26,   4.50,  13.45,  22.40,  32.20,  42.00,  41.30,  40.60,  41.10, // 300-345nm
     41.60,  39.80,  38.00,  40.20,  42.40,  40.45,  38.50,  36.75,  35.00,  39.20, // 350-395nm
     43.40,  44.85,  46.30,  45.10,  43.90,  40.50,  37.10,  36.90,  36.70,  36.30, // 400-445nm
     35.90,  34.25,  32.60,  30.25,  27.90,  26.10,  24.30,  22.20,  20.10,  18.15, // 450-495nm
     16.20,  14.70,  13.20,  10.90,   8.60,   7.35,   6.10,   5.15,   4.20,   3.05, // 500-545nm
      1.90,   0.95,   0.00,  -0.80,  -1.60,  -2.55,  -3.50,  -3.50,  -3.50,  -4.65, // 550-595nm
     -5.80,  -6.50,  -7.20,  -7.90,  -8.60,  -9.05,  -9.50, -10.20, -10.90, -10.80, // 600-645nm
    -10.70, -11.35, -12.00, -13.00, -14.00, -13.80, -13.60, -12.80, -12.00, -12.65, // 650-695nm
    -13.30, -13.10, -12.90, -11.75, -10.60, -11.10, -11.60, -11.90, -12.20, -11.20, // 700-745nm
    -10.20,  -9.00,  -7.80,  -9.50, -11.20, -10.80, -10.40, -10.50, -10.60, -10.15, // 750-795nm
     -9.70,  -9.00,  -8.30,  -8.80,  -9.30,  -9.55,  -9.80                          // 800-830nm
  });

  /** D-illuminant second principal component. */
  private static final Function1 S2 = new PiecewiseLinearFunction1(WAVELENGTHS, new double[] {
      0.00,   1.00,   2.00,   3.00,   4.00,   6.25,   8.50,   8.15,   7.80,   7.25, // 300-345nm
      6.70,   6.00,   5.30,   5.70,   6.10,   4.55,   3.00,   2.10,   1.20,   0.05, // 350-395nm
     -1.10,  -0.80,  -0.50,  -0.60,  -0.70,  -0.95,  -1.20,  -1.90,  -2.60,  -2.75, // 400-445nm
     -2.90,  -2.85,  -2.80,  -2.70,  -2.60,  -2.60,  -2.60,  -2.20,  -1.80,  -1.65, // 450-495nm
     -1.50,  -1.40,  -1.30,  -1.25,  -1.20,  -1.10,  -1.00,  -0.75,  -0.50,  -0.40, // 500-545nm
     -0.30,  -0.15,   0.00,   0.10,   0.20,   0.35,   0.50,   1.30,   2.10,   2.65, // 550-595nm
      3.20,   3.65,   4.10,   4.40,   4.70,   4.90,   5.10,   5.90,   6.70,   7.00, // 600-645nm
      7.30,   7.95,   8.60,   9.20,   9.80,  10.00,  10.20,   9.25,   8.30,   8.95, // 650-695nm
      9.60,   9.05,   8.50,   7.75,   7.00,   7.30,   7.60,   7.80,   8.00,   7.35, // 700-745nm
      6.70,   5.95,   5.20,   6.30,   7.40,   7.10,   6.80,   6.90,   7.00,   6.70, // 750-795nm
      6.40,   5.95,   5.50,   5.80,   6.10,   6.30,   6.50                          // 800-830nm
  });

  /** Coefficient for first principal component. */
  private final double m1;

  /** Coefficient for second principal component. */
  private final double m2;

  /**
   * Creates a new <code>CIEIlluminantDSpectrum</code>.
   * @param m1 The coefficient for the first principal component.
   * @param m2 The coefficient for the second principal component.
   */
  public CIEIlluminantDSpectrum(double m1, double m2) {
    this.m1 = m1;
    this.m2 = m2;
  }

  /**
   * Creates a new <code>CIEIlluminantDSpectrum</code> corresponding to the
   * provided chromaticity values.
   * @param x The x-coordinate of the chromaticity.
   * @param y The y-coordinate of the chromaticity.
   * @return The CIE Illuminant D spectral power distribution with the
   *     specified chromaticity.
   */
  public static CIEIlluminantDSpectrum fromChromaticity(double x, double y) {
    double m = 0.0241 + 0.2562 * x - 0.7341 * y;
    double m1 = (-1.3515 - 1.7703 * x + 5.9114 * y) / m;
    double m2 = (0.03000 - 31.4424 * x + 30.0717 * y) / m;

    // Coefficients need to be rounded to 3 decimal places to match
    // published CIE D-illuminant data.
    // http://en.wikipedia.org/wiki/Standard_illuminant#Illuminant_series_D
    m1 = Math.round(m1 * 1000.0) / 1000.0;
    m2 = Math.round(m2 * 1000.0) / 1000.0;

    return new CIEIlluminantDSpectrum(m1, m2);
  }

  /**
   * Creates a new <code>CIEIlluminantDSpectrum</code> corresponding to the
   * provided correlated color temperature.
   * @param t The correlated color temperature (in Kelvin).
   * @return The CIE Illuminant D spectral power distribution with the
   *     specified correlated color temperature.
   */
  public static CIEIlluminantDSpectrum fromCorrelatedColorTemperature(double t) {
    if (!COLOR_TEMPERATURE_RANGE.contains(t, MathUtil.EPSILON)) {
      throw new IllegalArgumentException("Color temperature outside valid range for D illuminant");
    }

    double t2 = t * t;
    double t3 = t * t2;
    double x = (t <= 7000.0)
        ? 0.244063 + 0.09911 * (1e3 / t) + 2.9678 * (1e6 / t2) - 4.6070 * (1e9 / t3)
        : 0.237040 + 0.24748 * (1e3 / t) + 1.9018 * (1e6 / t2) - 2.0064 * (1e9 / t3);
    double y = -3.000 * (x * x) + 2.870 * x - 0.275;

    return fromChromaticity(x, y);
  }

  /**
   * Creates a new <code>CIEIlluminantDSpectrum</code> corresponding to the
   * provided nominal color temperature (i.e., the temperature suggested by
   * the name of a standard illuminant.  For example: 6500K for D65).  This
   * differs slightly from correlated color temperature (see
   * {@link http://en.wikipedia.org/wiki/Illuminant_D65#Why_6504_K.3F}).
   * @param t The correlated color temperature (in Kelvin).
   * @return The CIE Illuminant D spectral power distribution with the
   *     specified correlated color temperature.
   * @see http://en.wikipedia.org/wiki/Illuminant_D65#Why_6504_K.3F
   */
  public static CIEIlluminantDSpectrum fromNominalColorTemperature(double t) {
    return fromCorrelatedColorTemperature(t * (1.4388 / 1.438));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Function1#evaluate(double)
   */
  @Override
  public double evaluate(double x) {
    return S0.evaluate(x) + m1 * S1.evaluate(x) + m2 * S2.evaluate(x);
  }

}
