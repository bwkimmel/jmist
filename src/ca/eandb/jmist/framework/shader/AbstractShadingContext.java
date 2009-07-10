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

package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.ScatteredRay.Type;
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
public abstract class AbstractShadingContext implements ShadingContext {

	private Medium		ambientMedium;
	private Basis3		basis;
	private Material	material;
	private Modifier	modifier;
	private Point3		position;
	private int			primitiveIndex;
	private Shader		shader;
	private Basis3		shadingBasis;
	private Point2		uv;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#castRay(ca.eandb.jmist.framework.ScatteredRay)
	 */
	@Override
	public Color castRay(ScatteredRay ray) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getAmbientLight()
	 */
	@Override
	public Color getAmbientLight() {
		WavelengthPacket lambda = getWavelengthPacket();
		ColorModel colorModel = lambda.getColorModel();
		return colorModel.getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getColorModel()
	 */
	@Override
	public ColorModel getColorModel() {
		return getWavelengthPacket().getColorModel();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getDistance()
	 */
	@Override
	public double getDistance() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getImportance()
	 */
	@Override
	public Color getImportance() {
		return getColorModel().getWhite(getWavelengthPacket());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getIncident()
	 */
	@Override
	public Vector3 getIncident() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getLightSamples()
	 */
	@Override
	public Iterable<LightSample> getLightSamples() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getModifier()
	 */
	@Override
	public Modifier getModifier() {
		return modifier != null ? modifier : Modifier.IDENTITY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getPathDepth()
	 */
	@Override
	public int getPathDepth() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getPathDepthByType(ca.eandb.jmist.framework.ScatteredRay.Type)
	 */
	@Override
	public int getPathDepthByType(Type type) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getRay()
	 */
	@Override
	public Ray3 getRay() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getScatteredRays()
	 */
	@Override
	public ScatteredRays getScatteredRays() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getShader()
	 */
	@Override
	public Shader getShader() {
		return shader != null ? shader : Shader.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#isEyePath()
	 */
	@Override
	public boolean isEyePath() {
		return !isLightPath();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#isFront()
	 */
	@Override
	public boolean isFront() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#isLightPath()
	 */
	@Override
	public boolean isLightPath() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setAmbientMedium(ca.eandb.jmist.framework.Medium)
	 */
	@Override
	public void setAmbientMedium(Medium medium) {
		this.ambientMedium = medium;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setBasis(ca.eandb.jmist.math.Basis3)
	 */
	@Override
	public void setBasis(Basis3 basis) {
		this.basis = basis;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setMaterial(ca.eandb.jmist.framework.Material)
	 */
	@Override
	public void setMaterial(Material material) {
		this.material = material;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setModifier(ca.eandb.jmist.framework.Modifier)
	 */
	@Override
	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setNormal(ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public void setNormal(Vector3 normal) {
		this.basis = Basis3.fromW(normal);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setPosition(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public void setPosition(Point3 position) {
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setPrimitiveIndex(int)
	 */
	@Override
	public void setPrimitiveIndex(int index) {
		this.primitiveIndex = index;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setShader(ca.eandb.jmist.framework.Shader)
	 */
	@Override
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setShadingBasis(ca.eandb.jmist.math.Basis3)
	 */
	@Override
	public void setShadingBasis(Basis3 basis) {
		this.shadingBasis = basis;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setShadingNormal(ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public void setShadingNormal(Vector3 normal) {
		this.shadingBasis = (basis != null) ? Basis3.fromWUV(normal, basis.u(),
				basis.v()) : Basis3.fromW(normal);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#setUV(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public void setUV(Point2 uv) {
		this.uv = uv;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#getWavelengthPacket()
	 */
	@Override
	public WavelengthPacket getWavelengthPacket() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ShadingContext#shade()
	 */
	@Override
	public Color shade() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getAmbientMedium()
	 */
	@Override
	public Medium getAmbientMedium() {
		return ambientMedium != null ? ambientMedium : Medium.VACUUM;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getBasis()
	 */
	@Override
	public Basis3 getBasis() {
		return basis != null ? basis : Basis3.STANDARD;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getMaterial()
	 */
	@Override
	public Material getMaterial() {
		return material != null ? material : Material.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getNormal()
	 */
	@Override
	public Vector3 getNormal() {
		return basis != null ? basis.w() : Vector3.K;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getPosition()
	 */
	@Override
	public Point3 getPosition() {
		return position != null ? position : Point3.ORIGIN;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getPrimitiveIndex()
	 */
	@Override
	public int getPrimitiveIndex() {
		return primitiveIndex;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getShadingBasis()
	 */
	@Override
	public Basis3 getShadingBasis() {
		return shadingBasis != null ? shadingBasis : (basis != null ? basis
				: Basis3.STANDARD);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getShadingNormal()
	 */
	@Override
	public Vector3 getShadingNormal() {
		return shadingBasis != null ? shadingBasis.w() : (basis != null ? basis
				.w() : Vector3.K);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getTangent()
	 */
	@Override
	public Vector3 getTangent() {
		return basis != null ? basis.u() : Vector3.I;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SurfacePoint#getUV()
	 */
	@Override
	public Point2 getUV() {
		return uv != null ? uv : Point2.ORIGIN;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public boolean visibility(Ray3 ray, double maximumDistance) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public boolean visibility(Ray3 ray) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {
		throw new UnsupportedOperationException();
	}

}
