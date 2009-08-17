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

import ca.eandb.jmist.framework.Emitter;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.Lens.Projection;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.math.Vector4;

/**
 * @author brad
 *
 */
public class LightNode extends AbstractPathNode {

	private final Emitter emitter;

	private final Sphere target;

	private final WavelengthPacket lambda;

	/* package */ LightNode(Emitter emitter, Sphere target, WavelengthPacket lambda) {
		this.emitter = emitter;
		this.target = target;
		this.lambda = lambda;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi.PathNode#evaluate(ca.eandb.jmist.math.Vector3)
	 */
	public Color evaluate(Vector3 v) {
		return emitter.getEmittedRadiance(v, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi.PathNode#getDepth()
	 */
	public int getDepth() {
		return 0;
	}

	public PathNode getParent() {
		return null;
	}

	public HPoint3 getPosition() {
		return emitter.getPosition();
	}

	public Color getValue() {
		return getColorModel().getWhite(lambda);
	}

	public boolean isOnLightPath() {
		return true;
	}

	public Color scatter(Vector3 v) {
		return emitter.getEmittedRadiance(v, lambda);
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
		// TODO Auto-generated method stub

	}

}
