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

package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class UniformSceneObject extends AbstractSceneObject {

	private final Geometry geometry;

	private Material material;

	private Medium ambientMedium;

	private Shader shader;

	private Modifier modifier;

	private double epsilon = MathUtil.EPSILON;

	/**
	 * @param geometry
	 */
	public UniformSceneObject(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * @param material the material to set
	 */
	public final UniformSceneObject setMaterial(Material material) {
		this.material = material;
		return this;
	}

	/**
	 * @param ambientMedium the ambientMedium to set
	 */
	public final UniformSceneObject setAmbientMedium(Medium ambientMedium) {
		this.ambientMedium = ambientMedium;
		return this;
	}

	/**
	 * @param shader the shader to set
	 */
	public final UniformSceneObject setShader(Shader shader) {
		this.shader = shader;
		return this;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public final UniformSceneObject setModifier(Modifier modifier) {
		this.modifier = modifier;
		return this;
	}

	/**
	 * @param epsilon the epsilon to set
	 */
	public final UniformSceneObject setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		return this;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	@Override
	public Box3 boundingBox() {
		return geometry.boundingBox();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	@Override
	public Sphere boundingSphere() {
		return geometry.boundingSphere();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#createLight()
	 */
	@Override
	public Light createLight() {
		return new Light() {
			public void illuminate(Intersection x, Illuminable target) {
				SurfacePoint sp = geometry.generateRandomSurfacePoint();
				Point3 p = sp.getPosition();
				Vector3 v = p.unitVectorTo(x.getPosition());
				Color c = material.emission(sp, v);
				target.addLightSample(new PointLightSample(x, p, c));
			}
		};
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#intersect(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Intersection intersect(Ray3 ray) {
		return NearestIntersectionRecorder.computeNearestIntersection(ray, geometry);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return (material != null) && material.isEmissive();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneObject#prepareShadingContext(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public void prepareShadingContext(ShadingContext context) {
		context.setMaterial(material);
		context.setAmbientMedium(ambientMedium);
		context.setShader(shader);
		context.setModifier(modifier);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(Ray3 ray, double maximumDistance) {
		return geometry.visibility(ray.advance(epsilon), maximumDistance - MathUtil.EPSILON);
	}

}
