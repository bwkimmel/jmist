/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.Ray3;
import ca.eandb.jmist.toolkit.Tuple;

/**
 * @author bkimmel
 *
 */
public class ScatterResult {

	private ScatterResult(Ray3 scatteredRay, Tuple wavelengths, double[] weights, double specularity) {

		assert(scatteredRay != null);
		assert(wavelengths.size() == weights.length);

		this.scatteredRay = scatteredRay;
		this.wavelengths = wavelengths;
		this.weights = weights;
		this.specularity = specularity;
		this.dispersionIndex = -1;

	}

	private ScatterResult(Ray3 scatteredRay, double wavelength, double weight, double specularity, int dispersionIndex) {

		assert(scatteredRay != null);

		this.scatteredRay = scatteredRay;
		this.wavelengths = new Tuple(wavelength);
		this.weights = new double[]{ weight };
		this.specularity = specularity;
		this.dispersionIndex = dispersionIndex;

	}

	public static ScatterResult diffuse(Ray3 scatteredRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(scatteredRay, wavelengths, weights, 0.0);
	}

	public static ScatterResult specular(Ray3 scatteredRay, Tuple wavelengths, double[] weights) {
		return new ScatterResult(scatteredRay, wavelengths, weights, 1.0);
	}

	public static ScatterResult scatter(Ray3 scatteredRay, Tuple wavelengths, double[] weights, double specularity) {
		return new ScatterResult(scatteredRay, wavelengths, weights, specularity);
	}

	public static ScatterResult disperse(Ray3 scatteredRay, int index, double wavelength, double weight, double specularity) {
		return new ScatterResult(scatteredRay, wavelength, weight, 1.0, index);
	}

	public Ray3 scatteredRay() {
		return this.scatteredRay;
	}

	public Tuple wavelengths() {
		return this.wavelengths;
	}

	public double[] weights() {
		return this.weights;
	}

	public double weightAt(int index) {
		if (this.dispersed()) {
			return (index == this.dispersionIndex) ? this.weights[0] : 0.0;
		} else if (index < this.weights.length) {
			return this.weights[index];
		} else {
			return 0.0;
		}
	}

	public double specularity() {
		return this.specularity;
	}

	public boolean dispersed() {
		return this.dispersionIndex >= 0;
	}

	public int dispersionIndex() {
		return this.dispersionIndex;
	}

	private final Ray3 scatteredRay;
	private final Tuple wavelengths;
	private final double[] weights;
	private final double specularity;
	private final int dispersionIndex;

}
