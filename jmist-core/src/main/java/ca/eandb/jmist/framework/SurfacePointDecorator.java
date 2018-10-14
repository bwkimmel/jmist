package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

public abstract class SurfacePointDecorator implements SurfacePoint {
  private final SurfacePoint inner;

  protected SurfacePointDecorator(SurfacePoint inner) {
    this.inner = inner;
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
    return inner.getPosition();
  }

  @Override
  public Vector3 getNormal() {
    return inner.getNormal();
  }

  @Override
  public Basis3 getBasis() {
    return inner.getBasis();
  }

  @Override
  public Vector3 getShadingNormal() {
    return inner.getShadingNormal();
  }

  @Override
  public Basis3 getShadingBasis() {
    return inner.getShadingBasis();
  }

  @Override
  public Vector3 getTangent() {
    return inner.getTangent();
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
