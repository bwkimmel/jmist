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

package ca.eandb.jmist.framework.gi;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.Lens.Projection;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * @author brad
 *
 */
public abstract class AbstractPathNode implements PathNode {

	private final PathNodeFactory nodes;

	private final Color value;

	protected AbstractPathNode(Color value, PathNodeFactory nodes) {
		this.nodes = nodes;
		this.value = value;
	}

	public final PathNodeFactory getFactory() {
		return nodes;
	}

	public final Color getValue() {
		return value;
	}

	public final boolean atInfinity() {
		return getPosition().isVector();
	}

	public final boolean isOnEyePath() {
		return !isOnLightPath();
	}

	public final Scene getScene() {
		return nodes.getScene();
	}

	public final ColorModel getColorModel() {
		return nodes.getColorModel();
	}

	protected final ScatteringNode trace(Ray3 ray, Color power) {
		return nodes.trace(ray, power, this);
	}

	public void scatterToEye(Raster raster, double weight) {
		Scene scene = getScene();
		Lens lens = scene.getLens();
		HPoint3 pos = getPosition();
		Projection proj = lens.project(pos);
		if (proj != null) {
			Ray3 ray = new Ray3(proj.pointOnLens(), pos);
			if (scene.getRoot().visibility(ray)) {
				int w = raster.getWidth();
				int h = raster.getHeight();
				Point2 uv = proj.pointOnImagePlane();
				int x = MathUtil.threshold((int) Math.floor(w * uv.x()), 0, w - 1);
				int y = MathUtil.threshold((int) Math.floor(h * uv.y()), 0, h - 1);
				Color color = scatter(ray.direction().opposite()).times(weight);
				raster.addPixel(x, y, color);
			}
		}
	}

}
