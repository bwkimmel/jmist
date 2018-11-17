package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

public class SphericalLight extends AbstractLight implements Serializable {
  private final Sphere sphere;

  private final Spectrum power;

  private final boolean shadows;

  public SphericalLight(Sphere sphere, Spectrum power) {
    this(sphere, power, true);
  }

  public SphericalLight(Sphere sphere, Spectrum power, boolean shadows) {
    this.sphere = sphere;
    this.power = power;
    this.shadows = shadows;
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target) {
    // See P. Shirley, C. Wang, K. Zimmerman, "Monte Carlo Techniques for Direct Lighting Calculations",
    // ACM Transactions on Graphics 15(1):1-36, Jan. 1996, Sec. 3.2.2, "Sampling uniformly in directional
    // space."

    double xi1 = rnd.next();
    double xi2 = rnd.next();

    Point3 p = x.getPosition();
    Vector3 w = p.vectorTo(sphere.center());
    double d2 = w.squaredLength();
    double d = Math.sqrt(d2);
    double r = sphere.radius();
    if (d < r) {
      return;
    }

    double rd2 = (r / d) * (r / d);
    double cosThetaMax = Math.sqrt(1.0 - rd2);
    SphericalCoordinates sc = new SphericalCoordinates(
        FastMath.acos(1.0 - xi1 + xi1 * cosThetaMax),
        2.0 * Math.PI * xi2);
    Basis3 basis = Basis3.fromWU(w, x.getNormal());

    Ray3 ray = new Ray3(p, sc.toCartesian(basis));
    Interval interval = sphere.intersect(ray);
    if (interval.isEmpty()) {
      return;
    }

    double t = interval.minimum();
    Point3 pos = ray.pointAt(t);
    Vector3 w1 = ray.direction().opposite();
    Vector3 n1 = sphere.center().unitVectorTo(pos);
    double w1dotn1 = w1.dot(n1);
    if (w1dotn1 < 0.0) {
      return;
    }
    double ndotl = x.getShadingNormal().dot(ray.direction());
    double attenuation = 0.5 * Math.abs(ndotl) * (1.0 - cosThetaMax) / (Math.PI * r * r);
    Color intensity = power.sample(lambda).times(attenuation);

    target.addLightSample(new PointLightSample(x, pos, intensity, shadows));
  }
}
