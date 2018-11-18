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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

public final class SurfaceNode extends AbstractScatteringNode {

  private final SurfacePoint surf;

  public SurfaceNode(PathNode parent, ScatteredRay sr, SurfacePoint surf, double ru, double rv, double rj) {
    super(parent, sr, ru, rv, rj);
    this.surf = surf;
  }

  @Override
  public double getSourcePDF() {
    PathInfo path = getPathInfo();
    Scene scene = path.getScene();
    Light light = scene.getLight();
    return light.getSamplePDF(surf, path);
  }

  @Override
  public double getSourcePDF(Vector3 v) {
    PathInfo path = getPathInfo();
    Material material = surf.getMaterial();
    WavelengthPacket lambda = path.getWavelengthPacket();
    return material.getEmissionPDF(surf, v, lambda);
  }

  @Override
  public Color getSourceRadiance() {
    PathInfo path = getPathInfo();
    Material material = surf.getMaterial();
    WavelengthPacket lambda = path.getWavelengthPacket();
    Vector3 out = PathUtil.getDirection(this, getParent());
    return material.emission(surf, out, lambda);
  }

  @Override
  public boolean isOnLightSource() {
    return surf.getMaterial().isEmissive();
  }

  @Override
  public ScatteredRay sample(double ru, double rv, double rj) {
    PathInfo path = getPathInfo();
    WavelengthPacket lambda = path.getWavelengthPacket();
    Vector3 v = PathUtil.getDirection(getParent(), this);
    Material material = surf.getMaterial();
    return material.scatter(surf, v, isOnEyePath(), lambda, ru, rv, rj);
  }

  @Override
  public double getCosine(Vector3 v) {
    Vector3 n = surf.getShadingNormal();
    return Math.abs(v.unit().dot(n));
  }

  @Override
  public HPoint3 getPosition() {
    return surf.getPosition();
  }

  @Override
  public PathNode reverse(PathNode newParent, PathNode grandChild) {
    if (newParent != null) {
      PathNode child = (grandChild != null) ? grandChild.getParent() : null;
      if (grandChild != null) {
        if (!PathUtil.isSameNode(child, newParent)) {
          throw new IllegalArgumentException(
              "newParent and grandChild.getParent() are different.");
        } else if (child.getParent() != this) {
          throw new IllegalArgumentException(
              "grandChild is not a grandchild of this node.");
        } else if (!PathUtil.isSameNode(newParent.getParent(),
            grandChild)) {
          throw new IllegalArgumentException(
              "grandChild and newParent.getParent() are different.");
        }
      }

      Vector3 v = PathUtil.getDirection(newParent, this);
      Point3 origin = newParent.isAtInfinity()
          ? surf.getPosition().minus(v)
          : newParent.getPosition().toPoint3();
      Ray3 ray = new Ray3(origin, v);
      ScatteredRay sr;

      if (grandChild != null) {
        double rpdf = grandChild.getReversePDF();
        double pdf = grandChild.getPDF();
        Color color = grandChild.getCumulativeWeight()
            .divide(child.getCumulativeWeight())
            .times(pdf / rpdf);
        sr = grandChild.isSpecular()
            ? ScatteredRay.specular(ray, color, rpdf)
            : ScatteredRay.diffuse(ray, color, rpdf);
      } else { // grandChild == null
        double pdf = newParent.getPDF(v);
        Color color = newParent.scatter(v).divide(pdf);
        sr = ScatteredRay.diffuse(ray, color, pdf);
      }

      return new SurfaceNode(newParent, sr, surf, getRU(), getRV(), getRJ());
    } else { // newParent == null
      if (grandChild != null) {
        throw new IllegalArgumentException(
            "newParent == null && grandChild != null");
      }

      PathInfo pi = getPathInfo();
      Scene scene = pi.getScene();
      Light light = scene.getLight();
      double pdf = light.getSamplePDF(surf, pi);

      return ScaledLightNode.create(pdf, new SurfaceLightNode(pi, surf, getRU(), getRV(), getRJ()), getRJ());
    }
  }

  @Override
  public Color scatter(Vector3 v) {
    PathInfo path = getPathInfo();
    PathNode parent = getParent();
    Material material = surf.getMaterial();
    WavelengthPacket lambda = path.getWavelengthPacket();
    Vector3 in, out;
    if (isOnLightPath()) {
      in = PathUtil.getDirection(parent, this);
      out = v;
    } else { // isOnEyePath()
      in = v.opposite();
      out = PathUtil.getDirection(this, parent);
    }
    return material.bsdf(surf, in, out, lambda);
  }

  @Override
  public double getPDF(Vector3 out) {
    PathInfo path = getPathInfo();
    PathNode parent = getParent();
    Material material = surf.getMaterial();
    WavelengthPacket lambda = path.getWavelengthPacket();
    boolean adjoint = isOnEyePath();
    Vector3 in = PathUtil.getDirection(parent, this);
    return material.getScatteringPDF(surf, in, out, adjoint, lambda);
  }

  @Override
  public double getReversePDF(Vector3 in) {
    PathInfo path = getPathInfo();
    PathNode parent = getParent();
    Material material = surf.getMaterial();
    WavelengthPacket lambda = path.getWavelengthPacket();
    boolean adjoint = isOnLightPath();
    Vector3 out = PathUtil.getDirection(this, parent);
    return material.getScatteringPDF(surf, in, out, adjoint, lambda);
  }

//  @Override
//  public PathNode reverse(PathNode newParent, PathNode grandChild) {
//    if (newParent != null) {
//      if (grandChild != null) {
//        if (grandChild.getParent() == null
//            || grandChild.getParent().getParent() != this) {
//          throw new IllegalArgumentException(
//              "grandChild.getParent().getParent() != this");
//        }
//        if (!PathUtil.isSameNode(newParent, grandChild.getParent())) {
//          throw new IllegalArgumentException(
//              "grandChild.getParent() and newParent are different nodes.");
//        }
//      }
//      ScatteredRay sr;
//      if (grandChild.isSpecular()) {
//        Color color = grandChild.getCumulativeWeight().divide(grandChild.getParent().getCumulativeWeight());
//        double pdf = grandChild.getReversePDF();
//        color = color.times(grandChild.getPDF() / pdf);
//        Point3 pos = newParent.getPosition().toPoint3();
//        Vector3 v = PathUtil.getDirection(newParent, this);
//        Ray3 ray = new Ray3(pos, v);
//        sr = ScatteredRay.specular(ray, color, pdf);
//      }
//    } else { // newParent == null
//      if (grandChild != null) {
//        throw new IllegalArgumentException("newParent == null && grandChild != null");
//      }
//
//    }
//    // TODO Auto-generated method stub
//    return null;
//  }

}
