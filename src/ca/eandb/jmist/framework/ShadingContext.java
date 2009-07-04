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
 * @author brad
 *
 */
public interface ShadingContext extends SurfacePoint, VisibilityFunction3 {

	WavelengthPacket getWavelengthPacket();

	Ray3 getRay();

	Vector3 getIncident();

	double getDistance();

	boolean isFront();

	int getPathDepth();

	Color getImportance();

	boolean isEyePath();

	boolean isLightPath();

	int getPathDepthByType(ScatteredRay.Type type);

	ColorModel getColorModel();

	ScatteredRays getScatteredRays();

	Color castRay(ScatteredRay ray);

	Color shade();

	Iterable<LightSample> getLightSamples();

	Shader getShader();

	Modifier getModifier();

	Color getAmbientLight();

	Random getRandom();

	void setPosition(Point3 position);

	void setNormal(Vector3 normal);

	void setBasis(Basis3 basis);

	void setPrimitiveIndex(int index);

	void setShadingBasis(Basis3 basis);

	void setShadingNormal(Vector3 normal);

	void setUV(Point2 uv);

	void setMaterial(Material material);

	void setAmbientMedium(Medium medium);

	void setShader(Shader shader);

	void setModifier(Modifier modifier);

}
