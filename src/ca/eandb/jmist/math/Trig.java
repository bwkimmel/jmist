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

package ca.eandb.jmist.math;

/**
 * This class contains precomputed arrays of trigonometric quantities.
 * @author brad
 */
public final class Trig {

	/** 256 cosine values uniformly spaced from zero to pi. */
	public static final double[] COS_THETA = new double[256];

	/** 256 sine values uniformly spaced from zero to pi. */
	public static final double[] SIN_THETA = new double[256];

	/** 256 cosine values uniformly spaced from zero to 2*pi. */
	public static final double[] COS_PHI = new double[256];

	/** 256 sine values uniformly spaced from zero to 2*pi. */
	public static final double[] SIN_PHI = new double[256];

	/**
	 * Precomputed trigonometric quantities.
	 */
	static {
		for (int i = 0; i < 256; i++) {
			double angle = (double) i * (1.0 / 256.0) * Math.PI;
			COS_THETA[i] = Math.cos(angle);
			SIN_THETA[i] = Math.sin(angle);
			COS_PHI[i] = Math.cos(2.0 * angle);
			SIN_PHI[i] = Math.sin(2.0 * angle);
		}
	}

	/** Private constructure to ensure that this class is not instantiated. */
	private Trig() {}

}
