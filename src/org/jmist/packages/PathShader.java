/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Geometry;
import org.jmist.framework.Intersection;
import org.jmist.framework.Material;
import org.jmist.framework.Observer;
import org.jmist.framework.RayShader;
import org.jmist.framework.ScatterResult;
import org.jmist.framework.SpectralEstimator;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.RandomUtil;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.util.ArrayUtil;
import org.jmist.util.MathUtil;

/**
 * @author bkimmel
 *
 */
public final class PathShader implements RayShader {

	/**
	 * Creates a new <code>PathShader</code>.
	 * @param geometry
	 * @param observer
	 */
	public PathShader(Geometry geometry, Observer observer) {
		this.geometry = geometry;
		this.observer = observer;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.RayShader#shadeRay(org.jmist.toolkit.Ray3, double[])
	 */
	@Override
	public double[] shadeRay(Ray3 ray, double[] pixel) {
		return this.observer.acquire(new PathEstimator(ray), pixel);
	}

	private final class PathEstimator implements SpectralEstimator {

		public PathEstimator(Ray3 ray) {
			this.ray = ray;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.SpectralEstimator#sample(org.jmist.toolkit.Tuple, double[])
		 */
		@Override
		public double[] sample(Tuple wavelengths, double[] responses) {
			return PathShader.this.sample(this.ray, wavelengths, responses);
		}

		private final Ray3 ray;

	}

	private double[] sample(Ray3 ray, Tuple wavelengths, double[] responses) {
		double[] importance = ArrayUtil.setAll(new double[wavelengths.size()], 1.0);
		return this.sample(ray, wavelengths, new RandomScatterRecorder(), null, importance, null);
	}

	private double[] sample(Ray3 ray, Tuple wavelengths,
			RandomScatterRecorder scattering, double[] sample,
			double[] importance, double[] responses) {

		Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, this.geometry);

		if (x == null) {
			return responses;
		}

		Material material = x.material();
		Spectrum emission = material.emission(x, ray.direction().opposite());

		sample = emission.sample(wavelengths, sample);
		sample = MathUtil.modulate(sample, importance);
		responses = MathUtil.add(responses, sample);

		scattering.reset(RandomUtil.categorical(importance));
		material.scatter(x, wavelengths, scattering);

		ScatterResult sr = scattering.getScatterResult();

		if (sr == null) {
			return responses;
		}

		if (importance.length > 1 && sr.dispersed()) {

			int index = sr.dispersionIndex();

			double[] dispersedImportance = MathUtil.scale(sr.weights(),
					importance[index]);
			double[] dispersedResponses = this
					.sample(sr.scatteredRay(), sr.wavelengths(), scattering,
							null, dispersedImportance, null);

			assert(dispersedResponses.length == 1);
			responses[index] += dispersedResponses[0];

			return responses;

		} else {

			MathUtil.modulate(importance, sr.weights());
			return this.sample(sr.scatteredRay(), sr.wavelengths(), scattering,
					sample, importance, responses);

		}

	}

	private final Geometry geometry;
	private final Observer observer;

}
