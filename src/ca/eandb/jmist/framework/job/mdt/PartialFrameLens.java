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
package ca.eandb.jmist.framework.job.mdt;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.ScatteringNode;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PartialFrameLens implements Lens {
	
	private final Lens inner;
	
	private final double fraction;

	/**
	 * @param inner
	 */
	public PartialFrameLens(Lens inner, double fraction) {
		this.inner = inner;
		this.fraction = fraction;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd) {
		return inner.rayAt(p, lambda, rnd); 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, double, double, double)
	 */
	public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
			double rj) {
		return new Node(inner.sample(p, pathInfo, ru, rv, rj));
	}
	
	private final class Node extends EyeTerminalNode {
		
		private final EyeNode inner;
		
		public Node(EyeNode inner) {
			super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
			this.inner = inner;
		}

		public Point2 project(HPoint3 x) {
			return inner.project(x);
		}

		public double getCosine(Vector3 v) {
			return inner.getCosine(v);
		}

		public double getPDF() {
			return inner.getPDF() / fraction;
		}

		public double getPDF(Vector3 v) {
			return inner.getPDF(v) / fraction;
		}

		public HPoint3 getPosition() {
			return inner.getPosition();
		}

		public boolean isSpecular() {
			return inner.isSpecular();
		}

		public ScatteredRay sample(double ru, double rv, double rj) {
			ScatteredRay sr = inner.sample(ru, rv, rj);
			return new ScatteredRay(sr.getRay(), sr.getColor(), sr.getType(), sr.getPDF() / fraction, sr.isTransmitted());
		}

		public Color scatter(Vector3 v) {
			return inner.scatter(v);
		}
		
	}

}
