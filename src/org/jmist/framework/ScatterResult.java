/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;

/**
 * @author bkimmel
 *
 */
public class ScatterResult {

	private ScatterResult(Ray3 scatteredRay, Tuple wavelengths, double[] weights, boolean diffuse) {

		assert(scatteredRay != null);
		assert(wavelengths.size() == weights.length);

		this.scatteredRay = scatteredRay;
		this.wavelengths = wavelengths;
		this.weights = weights;
		this.diffuse = diffuse;

	}

	private ScatterResult(Ray3 scatteredRay, double wavelength, double weight, boolean diffuse) {

		assert(scatteredRay != null);

		this.scatteredRay = scatteredRay;
		this.wavelengths = new Tuple(wavelength);
		this.weights = new double[]{ weight };
		this.diffuse = diffuse;

	}

	public static ScatterResult diffuse(Ray3 transmittedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(transmittedRay, wavelengths, weights, true);
	}

	public static ScatterResult specular(Ray3 transmittedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(transmittedRay, wavelengths, weights, false);
	}

	public static ScatterResult diffuse(Ray3 transmittedRay, double wavelength, double weight) {
		return new ScatterResult(transmittedRay, wavelength, weight, true);
	}

	public static ScatterResult specular(Ray3 transmittedRay, double wavelength, double weight) {
		return new ScatterResult(transmittedRay, wavelength, weight, false);
	}

	public boolean absorbed() {
		return (this.scatteredRay == null);
	}

	public Ray3 scatteredRay() {
		return this.scatteredRay;
	}

	public Tuple wavelengths() {
		return this.wavelengths;
	}

	public double weightAt(int index) {
		return this.weights[index];
	}

	public boolean diffuse() {
		return this.diffuse;
	}

	private final Ray3 scatteredRay;
	private final Tuple wavelengths;
	private final double[] weights;
	private final boolean diffuse;

}
