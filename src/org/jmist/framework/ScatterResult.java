/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public class ScatterResult {

	public ScatterResult(Ray3 scatteredRay, double[] weights, boolean diffuse, boolean transmitted) {
		this.scatteredRay = scatteredRay;
		this.weights = weights;
		this.diffuse = diffuse;
		this.transmitted = transmitted;
	}

	public Ray3 scatteredRay() {
		return this.scatteredRay;
	}

	public double weightAt(int index) {
		return this.weights[index];
	}

	public boolean diffuse() {
		return this.diffuse;
	}

	public boolean transmitted() {
		return this.transmitted;
	}

	private final Ray3 scatteredRay;
	private final double[] weights;
	private final boolean diffuse;
	private final boolean transmitted;

}
