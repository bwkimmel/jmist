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

	private ScatterResult(Ray3 scatteredRay, Tuple wavelengths, double[] weights, boolean diffuse, boolean transmitted) {

		assert(scatteredRay != null);
		assert(wavelengths.size() == weights.length);

		this.scatteredRay = scatteredRay;
		this.wavelengths = wavelengths;
		this.weights = weights;
		this.diffuse = diffuse;
		this.transmitted = transmitted;

	}

	private ScatterResult(Ray3 scatteredRay, double wavelength, double weight, boolean diffuse, boolean transmitted) {

		assert(scatteredRay != null);

		this.scatteredRay = scatteredRay;
		this.wavelengths = new Tuple(wavelength);
		this.weights = new double[]{ weight };
		this.diffuse = diffuse;
		this.transmitted = transmitted;

	}

	public static ScatterResult transmitDiffuse(Ray3 transmittedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(transmittedRay, wavelengths, weights, true, true);
	}

	public static ScatterResult transmitSpecular(Ray3 transmittedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(transmittedRay, wavelengths, weights, false, true);
	}

	public static ScatterResult reflectDiffuse(Ray3 reflectedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(reflectedRay, wavelengths, weights, true, false);
	}

	public static ScatterResult reflectSpecular(Ray3 reflectedRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(reflectedRay, wavelengths, weights, false, false);
	}

	public static ScatterResult transmitDiffuse(Ray3 transmittedRay, double wavelength, double weight) {
		return new ScatterResult(transmittedRay, wavelength, weight, true, true);
	}

	public static ScatterResult transmitSpecular(Ray3 transmittedRay, double wavelength, double weight) {
		return new ScatterResult(transmittedRay, wavelength, weight, false, true);
	}

	public static ScatterResult reflectDiffuse(Ray3 reflectedRay, double wavelength, double weight) {
		return new ScatterResult(reflectedRay, wavelength, weight, true, false);
	}

	public static ScatterResult reflectSpecular(Ray3 reflectedRay, double wavelength, double weight) {
		return new ScatterResult(reflectedRay, wavelength, weight, false, false);
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

	public boolean transmitted() {
		return this.transmitted;
	}

	private final Ray3 scatteredRay;
	private final Tuple wavelengths;
	private final double[] weights;
	private final boolean diffuse;
	private final boolean transmitted;

}
