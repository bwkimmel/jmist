/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

public abstract class AbstractShadingContext implements ShadingContext {

  private Medium ambientMedium;
  private Basis3 basis;
  private Material material;
  private Modifier modifier;
  private Point3 position;
  private int primitiveIndex;
  private Shader shader;
  private Basis3 shadingBasis;
  private Point2 uv;

  @Override
  public Color castRay(ScatteredRay ray) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Color getAmbientLight() {
    WavelengthPacket lambda = getWavelengthPacket();
    ColorModel colorModel = lambda.getColorModel();
    return colorModel.getBlack(lambda);
  }

  @Override
  public ColorModel getColorModel() {
    return getWavelengthPacket().getColorModel();
  }

  @Override
  public double getDistance() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Color getImportance() {
    return getColorModel().getWhite(getWavelengthPacket());
  }

  @Override
  public Vector3 getIncident() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Iterable<LightSample> getLightSamples() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Modifier getModifier() {
    return modifier != null ? modifier : Modifier.IDENTITY;
  }

  @Override
  public int getPathDepth() {
    return 0;
  }

  @Override
  public int getPathDepthByType(Type type) {
    return 0;
  }

  @Override
  public Ray3 getRay() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ScatteredRay getScatteredRay() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Shader getShader() {
    return shader != null ? shader : Shader.BLACK;
  }

  @Override
  public boolean isFront() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setAmbientMedium(Medium medium) {
    this.ambientMedium = medium;
  }

  @Override
  public void setBasis(Basis3 basis) {
    this.basis = basis;
  }

  @Override
  public void setMaterial(Material material) {
    this.material = material;
  }

  @Override
  public void setModifier(Modifier modifier) {
    this.modifier = modifier;
  }

  @Override
  public void setNormal(Vector3 normal) {
    this.basis = Basis3.fromW(normal);
  }

  @Override
  public void setPosition(Point3 position) {
    this.position = position;
  }

  @Override
  public void setPrimitiveIndex(int index) {
    this.primitiveIndex = index;
  }

  @Override
  public void setShader(Shader shader) {
    this.shader = shader;
  }

  @Override
  public void setShadingBasis(Basis3 basis) {
    this.shadingBasis = basis;
  }

  @Override
  public void setShadingNormal(Vector3 normal) {
    this.shadingBasis = (basis != null) ? Basis3.fromWUV(normal, basis.u(),
        basis.v()) : Basis3.fromW(normal);
  }

  @Override
  public void setUV(Point2 uv) {
    this.uv = uv;
  }

  @Override
  public WavelengthPacket getWavelengthPacket() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Color shade() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Medium getAmbientMedium() {
    return ambientMedium != null ? ambientMedium : Medium.VACUUM;
  }

  @Override
  public Basis3 getBasis() {
    return basis != null ? basis : Basis3.STANDARD;
  }

  @Override
  public Material getMaterial() {
    return material != null ? material : Material.BLACK;
  }

  @Override
  public Vector3 getNormal() {
    return basis != null ? basis.w() : Vector3.K;
  }

  @Override
  public Point3 getPosition() {
    return position != null ? position : Point3.ORIGIN;
  }

  @Override
  public int getPrimitiveIndex() {
    return primitiveIndex;
  }

  @Override
  public Basis3 getShadingBasis() {
    return shadingBasis != null ? shadingBasis : (basis != null ? basis : Basis3.STANDARD);
  }

  @Override
  public Vector3 getShadingNormal() {
    return shadingBasis != null ? shadingBasis.w() : (basis != null ? basis.w() : Vector3.K);
  }

  @Override
  public Vector3 getTangent() {
    return basis != null ? basis.u() : Vector3.I;
  }

  @Override
  public Point2 getUV() {
    return uv != null ? uv : Point2.ORIGIN;
  }

  @Override
  public boolean visibility(Ray3 ray) {
    throw new UnsupportedOperationException();
  }

}
