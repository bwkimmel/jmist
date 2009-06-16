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

package ca.eandb.jmist.framework.shader.ray;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Stack;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.SceneObject;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class SceneRayShader implements RayShader {

	private final Light light;

	private final RayCaster caster;

	private final RayShader background;

	/**
	 * @param caster
	 * @param light
	 * @param background
	 */
	public SceneRayShader(RayCaster caster, Light light, RayShader background) {
		this.caster = caster;
		this.light = light;
		this.background = background;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Color shadeRay(Ray3 ray) {
		Context context = new Context();
		return context.castPrimaryRay(ray);
	}

	private class Context implements ShadingContext, Illuminable {

		private final Stack<Intersection> intersections = new Stack<Intersection>();
		private final EnumMap<ScatteredRay.Type, Integer> depth = new EnumMap<ScatteredRay.Type, Integer>(ScatteredRay.Type.class);
		private final Stack<Color> colors = new Stack<Color>();
		private final Stack<ScatteredRays> rays = new Stack<ScatteredRays>();
		private final Stack<List<LightSample>> lightSamples = new Stack<List<LightSample>>();
		private int totalDepth = 0;

		public Color castPrimaryRay(Ray3 ray) {
			Intersection x = caster.castRay(ray);

			if (x != null) {
				rays.push(new ScatteredRays(x));
				colors.push(getColorModel().getWhite());
				lightSamples.push(null);
				intersections.push(x);
				Color color = shade();
				intersections.pop();
				lightSamples.pop();
				colors.pop();
				rays.pop();
				return color;
			} else {
				return background.shadeRay(ray);
			}
		}

		@Override
		public Color castRay(ScatteredRay sr) {
			ScatteredRay.Type type = sr.getType();
			Ray3 ray = sr.getRay();
			Intersection x = caster.castRay(ray);

			if (x != null) {
				totalDepth++;
				depth.put(type, depth.get(type) + 1);
				rays.push(new ScatteredRays(x));
				colors.push(sr.getColor().times(colors.peek()));
				lightSamples.push(null);
				intersections.push(x);
				Color color = shade();
				intersections.pop();
				lightSamples.pop();
				colors.pop();
				rays.pop();
				depth.put(type, depth.get(type) - 1);
				totalDepth--;
				return color;
			} else {
				return background.shadeRay(ray);
			}
		}

		@Override
		public ColorModel getColorModel() {
			return ColorModel.getInstance();
		}

		@Override
		public Color getImportance() {
			return colors.peek();
		}

		@Override
		public Iterable<LightSample> getLightSamples() {
			List<LightSample> samples = lightSamples.peek();
			if (samples == null) {
				samples = new ArrayList<LightSample>();
				lightSamples.pop();
				lightSamples.push(samples);
				light.illuminate(intersections.peek(), this);
			}
			return samples;
		}

		@Override
		public int getPathDepth() {
			return totalDepth;
		}

		@Override
		public int getPathDepthByType(Type type) {
			return depth.get(type);
		}

		@Override
		public ScatteredRays getScatteredRays() {
			return rays.peek();
		}

		@Override
		public boolean isEyePath() {
			return true;
		}

		@Override
		public boolean isLightPath() {
			return false;
		}

		@Override
		public Color shade() {
			return shader().shade(this);
		}

		@Override
		public double getDistance() {
			return intersections.peek().getDistance();
		}

		@Override
		public Vector3 getIncident() {
			return intersections.peek().getIncident();
		}

		@Override
		public boolean isFront() {
			return intersections.peek().isFront();
		}

		@Override
		public Basis3 getBasis() {
			return intersections.peek().getBasis();
		}

		@Override
		public Vector3 getNormal() {
			return intersections.peek().getNormal();
		}

		@Override
		public Point3 getPosition() {
			return intersections.peek().getPosition();
		}

		@Override
		public Basis3 getShadingBasis() {
			return intersections.peek().getShadingBasis();
		}

		@Override
		public Vector3 getShadingNormal() {
			return intersections.peek().getShadingNormal();
		}

		@Override
		public Vector3 getTangent() {
			return intersections.peek().getTangent();
		}

		@Override
		public Point2 getUV() {
			return intersections.peek().getUV();
		}

		@Override
		public boolean isSurfaceClosed() {
			return intersections.peek().isSurfaceClosed();
		}

		@Override
		public Medium ambientMedium() {
			return intersections.peek().ambientMedium();
		}

		@Override
		public Material material() {
			return intersections.peek().material();
		}

		@Override
		public SceneObject sceneObject() {
			return intersections.peek().sceneObject();
		}

		@Override
		public Shader shader() {
			return intersections.peek().shader();
		}

		@Override
		public boolean visibility(Ray3 ray, Interval I) {
			return caster.visibility(ray, I);
		}

		@Override
		public boolean visibility(Ray3 ray) {
			return caster.visibility(ray);
		}

		@Override
		public boolean visibility(Point3 p, Point3 q) {
			return caster.visibility(p, q);
		}

		@Override
		public void addLightSample(LightSample sample) {
			List<LightSample> samples = lightSamples.peek();
			assert(samples != null);
			samples.add(sample);
		}

	}

}
