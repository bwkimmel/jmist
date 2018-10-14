package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

public class TransformedSurfacePoint implements SurfacePoint {

  private final AffineTransformation3 t = new AffineTransformation3();

  private final SurfacePoint inner;

  public TransformedSurfacePoint(AffineMatrix3 T, SurfacePoint inner) {
    this.inner = inner;
    this.t.transform(T);
  }

  @Override
  public Material getMaterial() {
    return inner.getMaterial();
  }

  @Override
  public Medium getAmbientMedium() {
    return inner.getAmbientMedium();
  }

  @Override
  public Point3 getPosition() {
    return t.apply(inner.getPosition());
  }

  @Override
  public Vector3 getNormal() {
    return getBasis().w();
  }

  @Override
  public Basis3 getBasis() {
    Basis3 basis = inner.getBasis();
    basis = Basis3.fromUVW(t.apply(basis.u()), t.apply(basis.v()), t.apply(basis.w()));
    return basis;
  }

  @Override
  public Vector3 getShadingNormal() {
    return getShadingBasis().w();
  }

  @Override
  public Basis3 getShadingBasis() {
    Basis3 basis = inner.getShadingBasis();
    basis = Basis3.fromUVW(t.apply(basis.u()), t.apply(basis.v()), t.apply(basis.w()));
    return basis;
  }

  @Override
  public Vector3 getTangent() {
    return t.apply(inner.getTangent()).unit();
  }

  @Override
  public Point2 getUV() {
    return inner.getUV();
  }

  @Override
  public int getPrimitiveIndex() {
    return inner.getPrimitiveIndex();
  }
}
