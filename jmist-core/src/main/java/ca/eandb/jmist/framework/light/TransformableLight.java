package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.TransformedSurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.LightTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Vector3;

public class TransformableLight extends AbstractLight implements AffineTransformable3, Serializable {

  private final Light inner;

  private final InvertibleAffineTransformation3 t = new InvertibleAffineTransformation3();

  public TransformableLight(Light inner) {
    this.inner = inner;
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target) {
    x = new TransformedSurfacePoint(t.applyInverse(AffineMatrix3.IDENTITY), x);
    inner.illuminate(x, lambda, rnd, new Illuminable() {
      @Override
      public void addLightSample(LightSample sample) {
        target.addLightSample(new TransformedLightSample(t.apply(AffineMatrix3.IDENTITY), sample));
      }
    });
  }

  @Override
  public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
    return new Node(inner.sample(pathInfo, ru, rv, rj));
  }

  @Override
  public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
    Basis3 basis = x.getBasis();
    double su = t.apply(basis.u()).length();
    double sv = t.apply(basis.v()).length();
    return inner.getSamplePDF(x, pathInfo) / (su * sv);
  }

  private final class Node extends LightTerminalNode {
    private final LightNode inner;

    public Node(LightNode inner) {
      super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
      this.inner = inner;
    }

    @Override
    public double getPDF() {
      return inner.getPDF(); // TODO: scale factor
    }

    @Override
    public boolean isSpecular() {
      return inner.isSpecular();
    }

    @Override
    public HPoint3 getPosition() {
      return t.apply(inner.getPosition());
    }

    @Override
    public ScatteredRay sample(double ru, double rv, double rj) {
      return inner.sample(ru, rv, rj).transform(t.apply(AffineMatrix3.IDENTITY));
    }

    @Override
    public Color scatter(Vector3 v) {
      return inner.scatter(t.applyInverse(v));
    }

    @Override
    public double getCosine(Vector3 v) {
      return inner.getCosine(t.applyInverse(v));
    }

    @Override
    public double getPDF(Vector3 v) {
      return inner.getPDF(t.applyInverse(v));
    }
  }

  @Override
  public void transform(AffineMatrix3 T) {
    t.transform(T);
  }

  @Override
  public void stretchX(double cx) {
    t.stretchX(cx);
  }

  @Override
  public void stretchY(double cy) {
    t.stretchY(cy);
  }

  @Override
  public void stretchZ(double cz) {
    t.stretchZ(cz);
  }

  @Override
  public void stretch(double cx, double cy, double cz) {
    t.stretch(cx, cy, cz);
  }

  @Override
  public void transform(LinearMatrix3 T) {
    t.transform(T);
  }

  @Override
  public void rotateX(double angle) {
    t.rotateX(angle);
  }

  @Override
  public void rotateY(double angle) {
    t.rotateY(angle);
  }

  @Override
  public void rotateZ(double angle) {
    t.rotateZ(angle);
  }

  @Override
  public void rotate(Vector3 axis, double angle) {
    t.rotate(axis, angle);
  }

  @Override
  public void stretch(Vector3 axis, double c) {
    t.stretch(axis, c);
  }

  @Override
  public void scale(double c) {
    t.scale(c);
  }

  @Override
  public void translate(Vector3 v) {
    t.translate(v);
  }
}
