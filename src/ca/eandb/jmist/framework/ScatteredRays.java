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

package ca.eandb.jmist.framework;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.RandomUtil;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class ScatteredRays extends AbstractList<ScatteredRay> {

	private final SurfacePoint x;

	private final Vector3 v;

	private final WavelengthPacket lambda;

	private final Random rng;

	private final Material material;

	private List<ScatteredRay> rays = new ArrayList<ScatteredRay>(10);

	/**
	 * @param x
	 * @param v
	 * @param lambda
	 * @param material
	 */
	public ScatteredRays(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, Material material) {
		this.x = x;
		this.v = v;
		this.lambda = lambda;
		this.rng = rng;
		this.material = material;
	}

	private synchronized void ensureReady() {
		if (modCount == 0) {
			material.scatter(x, v, lambda, rng, new ScatteredRayRecorder() {
				public void add(ScatteredRay sr) {
					rays.add(sr);
				}
			});
			modCount++;
		}
	}

	public ScatteredRay getRandomScatteredRay(double rnd, EnumSet<ScatteredRay.Type> filter, boolean allowNone) {
		double[] cdf = new double[size()];
		for (int i = 0; i < cdf.length; i++) {
			ScatteredRay ray = rays.get(i);
			if (filter.contains(ray.getType())) {
				double weight = ColorUtil.getMeanChannelValue(ray.getColor());
				cdf[i] = (i > 0 ? cdf[i - 1] + weight : weight);
			}
		}
		double total = cdf[cdf.length - 1];
		if (allowNone) {
			if (rnd >= cdf[cdf.length - 1]) {
				return null;
			}
			total = Math.max(1.0, total);
		}
		if (!MathUtil.isZero(total) && rnd < cdf[cdf.length - 1]) {
			for (int i = 0; i < cdf.length; i++) {
				if (rnd < (cdf[i] / total)) {
					double pdf = (i > 0 ? cdf[i] - cdf[i - 1] : cdf[i]);
					return new WeightedScatteredRay(rays.get(i), total / pdf);
				}
			}
		}
		return null;
	}

	public ScatteredRay getRandomScatteredRay(double rnd, boolean allowNone) {
		return getRandomScatteredRay(RandomUtil.canonical(), EnumSet.allOf(ScatteredRay.Type.class), allowNone);
	}

	public ScatteredRay getRandomScatteredRay(EnumSet<ScatteredRay.Type> filter, boolean allowNone) {
		return getRandomScatteredRay(RandomUtil.canonical(), filter, allowNone);
	}

	public ScatteredRay getRandomScatteredRay(boolean allowNone) {
		return getRandomScatteredRay(RandomUtil.canonical(), EnumSet.allOf(ScatteredRay.Type.class), allowNone);
	}

	public ScatteredRay getRandomScatteredRay(double rnd, EnumSet<ScatteredRay.Type> filter) {
		return getRandomScatteredRay(rnd, filter, false);
	}

	public ScatteredRay getRandomScatteredRay(double rnd) {
		return getRandomScatteredRay(RandomUtil.canonical(), EnumSet.allOf(ScatteredRay.Type.class), false);
	}

	public ScatteredRay getRandomScatteredRay(EnumSet<ScatteredRay.Type> filter) {
		return getRandomScatteredRay(RandomUtil.canonical(), filter, false);
	}

	public ScatteredRay getRandomScatteredRay() {
		return getRandomScatteredRay(RandomUtil.canonical(), EnumSet.allOf(ScatteredRay.Type.class), false);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public ScatteredRay get(int index) {
		ensureReady();
		return rays.get(index);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		ensureReady();
		return rays.size();
	}

}
