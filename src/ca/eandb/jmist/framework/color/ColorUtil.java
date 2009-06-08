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

package ca.eandb.jmist.framework.color;

/**
 * @author brad
 *
 */
public final class ColorUtil {

	public static double getMeanChannelValue(Color color) {
		int channels = color.getColorModel().getNumChannels();
		double sum = 0.0;
		for (int i = 0; i < channels; i++) {
			sum += color.getValue(i);
		}
		return sum / (double) channels;
	}

	public static double getTotalChannelValue(Color color) {
		int channels = color.getColorModel().getNumChannels();
		double sum = 0.0;
		for (int i = 0; i < channels; i++) {
			sum += color.getValue(i);
		}
		return sum;
	}

	/** Instances of <code>ColorUtil</code>	cannot be created. */
	private ColorUtil() {}

}
