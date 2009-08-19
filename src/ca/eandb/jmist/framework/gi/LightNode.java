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
import ca.eandb.jmist.framework.Photon;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public class LightNode extends AbstractPathNode {

	private final Emitter emitter;

	private final Sphere target;

	/* package */ LightNode(Emitter emitter, Sphere target, Color value, PathNodeFactory nodes) {
		super(value, nodes);
		this.emitter = emitter;
		this.target = target;
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

	public boolean isOnLightPath() {
		return true;
	}

	public Color scatter(Vector3 v) {
		WavelengthPacket lambda = getValue().getWavelengthPacket();
		return getValue().times(emitter.getEmittedRadiance(v, lambda));
	}

	public ScatteringNode expand(Random rnd) {
		WavelengthPacket lambda = getValue().getWavelengthPacket();
		Photon photon = emitter.emit(target, lambda, rnd);
		return trace(photon.ray(), getValue().times(photon.power()));
	}

}
