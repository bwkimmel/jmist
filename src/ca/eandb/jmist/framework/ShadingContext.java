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

package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents the state of the current ray-intersection event.
 * @author Brad Kimmel
 */
public interface ShadingContext extends SurfacePoint, VisibilityFunction3 {

	/**
	 * The <code>WavelengthPacket</code> of the light transmitted along the
	 * path.
	 */
	WavelengthPacket getWavelengthPacket();

	/** The ray from which the intersection was computed. */
	Ray3 getRay();

	/** The incident direction (points <em>toward</em> the intersection). */
	Vector3 getIncident();

	/** The distance along the ray at which the intersection lies. */
	double getDistance();

	/**
	 * A value indicating whether the ray hit from the front side of the
	 * surface.
	 */
	boolean isFront();

	/** The number of ray-scattering events along this path so far. */
	int getPathDepth();

	/** The cumulative product of the colors from each ray-scattering event. */
	Color getImportance();

	/**
	 * Gets the number of ray-scattering events of the specified type
	 * encountered along the path so far.
	 * @param type The type of ray-scattering event to count (diffuse, glossy,
	 * 		specular).
	 */
	int getPathDepthByType(ScatteredRay.Type type);

	/** The <code>ColorModel</code> for the current rendering. */
	ColorModel getColorModel();

	/** The current ray-scatter event. */
	ScatteredRay getScatteredRay();

	/**
	 * Casts a secondary ray and shades it.
	 * @param ray The ray-scatter event.
	 * @return The <code>Color</code> of the shaded ray.
	 */
	Color castRay(ScatteredRay ray);

	/** 
	 * Shades the incident ray using the <code>Shader</code> associated with
	 * this <code>ShadingContext</code>.
	 * @return The <code>Color</code> of the shaded ray.
	 */
	Color shade();

	/** The recorded <code>LightSample</code>s illuminating the surface point. */
	Iterable<LightSample> getLightSamples();

	/** The <code>Shader</code> to use. */
	Shader getShader();

	/** The <code>Modifier</code> to use. */
	Modifier getModifier();

	/** The ambient light term. */
	Color getAmbientLight();

	/** Sets the position of the ray-intersection. */
	void setPosition(Point3 position);

	/** Sets the surface normal. */
	void setNormal(Vector3 normal);

	/** Sets the surface orientation. */
	void setBasis(Basis3 basis);

	/** Sets the ID of the primitive that was intersected. */
	void setPrimitiveIndex(int index);

	/** Sets the micro-surface orientation. */
	void setShadingBasis(Basis3 basis);

	/** Sets the micro-surface normal. */
	void setShadingNormal(Vector3 normal);

	/** Sets the texture coordinates. */
	void setUV(Point2 uv);

	/** Sets the <code>Material</code> to apply to the surface. */
	void setMaterial(Material material);

	/**
	 * Sets the <code>Medium</code> on the upper side of the surface (i.e., the
	 * side toward which the normal points).
	 */
	void setAmbientMedium(Medium medium);

	/** Sets the <code>Shader</code> to use. */
	void setShader(Shader shader);

	/** Sets the <code>Modifier</code> to use. */
	void setModifier(Modifier modifier);

}
