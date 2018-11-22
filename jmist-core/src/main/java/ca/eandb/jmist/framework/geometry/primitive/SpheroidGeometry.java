package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.math.*;

public final class SpheroidGeometry extends PrimitiveGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3401239873145509127L;

  /**The <code>Spheroid</code> describing this <code>SceneElement</code>. */
  private final Spheroid spheroid;

  /**
  * Creates a new <code>SpheroidGeometry</code>.
  * @param spheroid The <code>Spheroid</code> describing to be rendered.
  */
  public SpheroidGeometry(Spheroid spheroid) {
    this.spheroid = spheroid;
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    Interval I = spheroid.intersect(ray);
    if (!I.isEmpty()) {
      recorder.record(super.newIntersection(ray, I.minimum(),true));
      recorder.record(super.newIntersection(ray, I.maximum(),false));
    }
  }

  @Override
  protected Basis3 getBasis(AbstractGeometry.GeometryIntersection x) {
    return Basis3.fromWV(x.getNormal(), spheroid.basis().w());
  }

  @Override
  protected Vector3 getNormal(AbstractGeometry.GeometryIntersection x) {
    return this.spheroid.normalAt(x.getPosition());
  }

  /**
   * Texture taken from sphere, so a sphereoid is a distorted sphere.
   * If a and c are equal it looks like a sphere.
   */
  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Vector3 n = x.getNormal();
    SphericalCoordinates sc = SphericalCoordinates.fromCartesian(new Vector3(n.x(), -n.z(), n.y()));
    return new Point2(
        (Math.PI + sc.azimuthal()) / (2.0 * Math.PI),
        sc.polar() / Math.PI
    );
  }

  @Override
  public Box3 boundingBox() {
    return this.spheroid.boundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    return new Sphere(spheroid.center(), Math.max(spheroid.a(), spheroid.c()));
  }

  @Override
  public double getSurfaceArea() {
    return spheroid.surfaceArea();
  }
}
