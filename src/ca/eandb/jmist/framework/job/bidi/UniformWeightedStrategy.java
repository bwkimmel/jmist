/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.job.bidi;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.PathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public final class UniformWeightedStrategy implements BidiPathStrategy {

	/** Serialization version ID. */
	private static final long serialVersionUID = 214808820256943457L;

	private final int maxLightDepth;

	private final int maxEyeDepth;

	/**
	 * @param maxLightDepth
	 * @param maxEyeDepth
	 */
	public UniformWeightedStrategy(int maxLightDepth, int maxEyeDepth) {
		this.maxLightDepth = maxLightDepth;
		this.maxEyeDepth = maxEyeDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#getWeight(ca.eandb.jmist.framework.path.PathNode, ca.eandb.jmist.framework.path.PathNode)
	 */
	public double getWeight(PathNode lightNode, PathNode eyeNode) {
		int s = lightNode != null ? lightNode.getDepth() + 1 : 0;
		int t = eyeNode != null ? eyeNode.getDepth() + 1 : 0;
//		double[] weights = new double[s + t];

		// FIXME account for maximum eye and light depth
		int k = s + t + 1;
		while (eyeNode != null) {
			if (eyeNode.isSpecular()) {
				k--;
			}
			eyeNode = eyeNode.getParent();
		}

		while (lightNode != null) {
			if (lightNode.isSpecular()) {
				k--;
			}
			lightNode = lightNode.getParent();
		}

		return 1.0 / (double) k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceEyePath(ca.eandb.jmist.framework.Lens, ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
			Random rnd) {
		if (maxEyeDepth > 0) {
			PathNode head = lens.sample(p, pathInfo, rnd.next(), rnd.next(), rnd.next());
			return PathUtil.expand(head, maxEyeDepth - 1, rnd);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.BidiPathStrategy#traceLightPath(ca.eandb.jmist.framework.Light, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
	 */
	public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
		if (maxLightDepth > 0) {
			PathNode head = light.sample(pathInfo, rnd.next(), rnd.next(), rnd.next());
			return PathUtil.expand(head, maxLightDepth - 1, rnd);
		}
		return null;
	}

}
