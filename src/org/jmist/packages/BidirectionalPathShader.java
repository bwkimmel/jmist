/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.List;

import org.jmist.framework.Geometry;
import org.jmist.framework.Illuminable;
import org.jmist.framework.Intersection;
import org.jmist.framework.Light;
import org.jmist.framework.Material;
import org.jmist.framework.Observer;
import org.jmist.framework.RayShader;
import org.jmist.framework.ScatterResult;
import org.jmist.framework.SpectralEstimator;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.RandomUtil;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Tuple;
import org.jmist.toolkit.Vector3;
import org.jmist.util.ArrayUtil;
import org.jmist.util.MathUtil;

/**
 * @author bkimmel
 *
 */
public final class BidirectionalPathShader implements RayShader {

	/**
	 * Creates a new <code>PathShader</code>.
	 * @param geometry
	 * @param light
	 * @param observer
	 */
	public BidirectionalPathShader(Geometry geometry, Light light, Observer observer) {
		this.geometry = geometry;
		this.light = light;
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
			Path lightPath = generateLightPath(wavelengths);
			Path eyePath = generateEyePath(this.ray, wavelengths);

			return computeEstimate(wavelengths, eyePath, lightPath, responses);
		}

		private final Ray3 ray;

	}

	private class Path {

		public Path(Ray3 initialRay, double[] initialWeights) {
			this.initialRay = initialRay;
			this.initialWeights = initialWeights;
		}

		public List<Node> nodes() {
			return this.nodes;
		}

		public void addNode(Intersection intersection, ScatterResult scattering) {
			this.nodes.add(new Node(intersection, scattering));
		}

		private final List<Node> nodes = new ArrayList<Node>();
		private final Ray3 initialRay;
		private final double[] initialWeights;

		public class Node {

			public Node(Intersection intersection, ScatterResult scattering) {
				this.intersection = intersection;
				this.scattering = scattering;
			}

			public Intersection intersection() {
				return this.intersection;
			}

			public ScatterResult scattering() {
				return this.scattering;
			}

			private final Intersection intersection;
			private final ScatterResult scattering;
		}

	}

	private Path generateLightPath(Tuple wavelengths) {

		RandomScatterRecorder scattering = new RandomScatterRecorder();
		//this.light.emit(wavelengths, scattering);

		ScatterResult sr = scattering.getScatterResult();

		return sr != null ? generatePath(sr.scatteredRay(), wavelengths, sr.weights()) : null;

	}

	private Path generateEyePath(Ray3 ray, Tuple wavelengths) {
		return generatePath(ray, wavelengths, ArrayUtil.setAll(new double[wavelengths.size()], 1.0));
	}

	private Path generatePath(Ray3 ray, Tuple wavelengths, double[] initialWeights) {

		double[] importance = ArrayUtil.setRange(new double[wavelengths.size()], 0, initialWeights);
		Path path = new Path(ray, initialWeights);

		RandomScatterRecorder scattering = new RandomScatterRecorder();
		ScatterResult sr;

		do {

			Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, this.geometry);

			if (x == null) {
				break;
			}

			Material material = x.material();

			scattering.reset(RandomUtil.categorical(importance));
			material.scatter(x, wavelengths, scattering);

			sr = scattering.getScatterResult();

			path.addNode(x, sr);

			MathUtil.modulate(importance, sr.weights());
			ray = sr.scatteredRay();
			wavelengths = sr.wavelengths();

			if (sr.dispersionIndex() >= 0) {
				// TODO: implement dispersion in the light path.
				throw new java.lang.UnsupportedOperationException();
			}

		} while (sr != null);

		return path;

	}

	private double[] computeEstimate(Tuple wavelengths, Path eyePath, Path lightPath,
			double[] responses) {

		double[] importance = ArrayUtil.setRange(new double[wavelengths.size()], 0, eyePath.initialWeights);
		double[] radiance = new double[wavelengths.size()];
		double[] initialRadiance = lightPath.initialWeights;
		double[] sample = null;

		responses = ArrayUtil.initialize(responses, wavelengths.size());

		for (Path.Node eyeNode : eyePath.nodes()) {

			Intersection eyeX = eyeNode.intersection();
			Material eyeNodeMaterial = eyeX.material();

			IlluminationTarget target = new IlluminationTarget(eyeX, wavelengths, importance, sample);
			this.light.illuminate(eyeX, this.geometry, target);

			ArrayUtil.setRange(radiance, 0, initialRadiance);

			for (Path.Node lightNode : lightPath.nodes()) {

				Intersection lightX = lightNode.intersection();

				if (this.geometry.visibility(lightX.location(), eyeX.location())) {

					Material lightNodeMaterial = lightX.material();

					Vector3 forward = eyeX.location().vectorTo(lightX.location()).unit();
					Vector3 backward = forward.opposite();
					double squaredDistance = eyeX.location().squaredDistanceTo(lightX.location());
					double ndotf = lightX.microfacetNormal().dot(forward);
					double ndotb = eyeX.microfacetNormal().dot(backward);

					sample = ArrayUtil.setRange(sample, 0, radiance);

					eyeNodeMaterial.scattering(eyeX, forward).modulate(wavelengths, sample);
					lightNodeMaterial.scattering(lightX, backward).modulate(wavelengths, sample);
					sample = MathUtil.scale(sample, Math.abs(ndotf * ndotb) / squaredDistance);

					sample = MathUtil.modulate(sample, importance);
					responses = MathUtil.add(responses, sample);

				}

				MathUtil.modulate(radiance, lightNode.scattering().weights());

			}

			MathUtil.modulate(importance, eyeNode.scattering().weights());

		}

		return responses;

	}

	private final class IlluminationTarget implements Illuminable {

		/**
		 * @param x
		 * @param wavelengths
		 * @param importance
		 * @param responses
		 */
		public IlluminationTarget(Intersection x, Tuple wavelengths, double[] importance, double[] responses) {
			this.x = x;
			this.wavelengths = wavelengths;
			this.importance = importance;
			this.responses = responses;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.Illuminable#illuminate(org.jmist.toolkit.Vector3, org.jmist.framework.Spectrum)
		 */
		@Override
		public void illuminate(Vector3 from, Spectrum radiance) {

			Material	material = x.material();
			Vector3		n = x.microfacetNormal();
			double		ndotv = n.dot(from);

			Spectrum	scattering = material.scattering(x, from);

			if (scattering != null) {

				sample = radiance.sample(wavelengths, sample);
				scattering.modulate(wavelengths, sample);
				MathUtil.modulate(sample, importance);
				MathUtil.scale(sample, Math.abs(ndotv));
				MathUtil.add(responses, sample);

			}

		}

		private final Intersection x;
		private final Tuple wavelengths;
		private final double[] importance;
		private final double[] responses;
		private double[] sample;

	}

	private final Geometry geometry;
	private final Light light;
	private final Observer observer;

}
