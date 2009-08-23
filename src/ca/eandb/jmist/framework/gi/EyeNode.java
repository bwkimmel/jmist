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
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;


/**
 * @author brad
 *
 */
public final class EyeNode extends AbstractPathNode {

	private final Point2 p;

	/* package */ EyeNode(Point2 p, Color value, PathNodeFactory nodes) {
		super(value, nodes);
		this.p = p;
	}

	public ScatteringNode expand(Random rnd) {
		Scene scene = getScene();
		Lens lens = scene.getLens();
		Ray3 ray = lens.rayAt(p);
		return trace(ray, getValue());
	}

	public int getDepth() {
		return 0;
	}

	public PathNode getParent() {
		return null;
	}

	public HPoint3 getPosition() {
		return null;
	}

	public boolean isOnLightPath() {
		return false;
	}

	public Color scatter(Vector3 v) {
		return getColorModel().getBlack(getValue().getWavelengthPacket());
	}

}
