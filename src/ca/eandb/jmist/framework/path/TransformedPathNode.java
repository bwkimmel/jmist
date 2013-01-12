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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public abstract class TransformedPathNode extends AbstractPathNode {

	protected final PathNode inner;

	protected final AffineMatrix3 localToWorld;

	protected final AffineMatrix3 worldToLocal;

	/**
	 * @param pathInfo
	 */
	public TransformedPathNode(PathNode inner, AffineMatrix3 localToWorld, AffineMatrix3 worldToLocal) {
		super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
		this.inner = inner;
		this.localToWorld = localToWorld;
		this.worldToLocal = worldToLocal;
	}

	public TransformedPathNode(PathNode inner, AffineMatrix3 worldToLocal) {
		this(inner, worldToLocal, worldToLocal.inverse());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCosine(ca.eandb.jmist.math.Vector3)
	 */
	public double getCosine(Vector3 v) {
		v = worldToLocal.times(v);
		return inner.getCosine(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getCumulativeWeight()
	 */
	public Color getCumulativeWeight() {
		return inner.getCumulativeWeight();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getDepth()
	 */
	public int getDepth() {
		return inner.getDepth();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getGeometricFactor()
	 */
	public double getGeometricFactor() {
		return inner.getGeometricFactor();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPDF()
	 */
	public double getPDF() {
		return inner.getPDF();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getParent()
	 */
	public PathNode getParent() {
		return inner.getParent();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#getPosition()
	 */
	public HPoint3 getPosition() {
		return localToWorld.times(inner.getPosition());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isOnLightPath()
	 */
	public boolean isOnLightPath() {
		return inner.isOnLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#isSpecular()
	 */
	public boolean isSpecular() {
		return inner.isSpecular();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#sample(ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay sample(double ru, double rv, double rj) {
		ScatteredRay sr = inner.sample(ru, rv, rj);
		return sr != null ? sr.transform(localToWorld) : null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.path.PathNode#scatter(ca.eandb.jmist.math.Vector3)
	 */
	public Color scatter(Vector3 v) {
		v = worldToLocal.times(v);
		return inner.scatter(v);
	}

}
