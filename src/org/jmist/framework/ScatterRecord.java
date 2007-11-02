/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public class ScatterRecord {

	public ScatterRecord(Ray3 ray, double[] weights, boolean transmitted, boolean diffuse) {
		this.ray = ray;
		this.weights = weights;
		this.transmitted = transmitted;
		this.diffuse = diffuse;
	}

	public Ray3 ray() {
		return this.ray;
	}

	public double weightAt(int index) {
		return this.weights[index];
	}

	public boolean transmitted() {
		return this.transmitted;
	}

	public boolean diffuse() {
		return this.diffuse;
	}

	private final Ray3 ray;
	private final double[] weights;
	private final boolean transmitted;
	private final boolean diffuse;

}
