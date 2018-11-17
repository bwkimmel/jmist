package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

public class EnvironmentLight extends AbstractLight {
  private final RayShader env;
  private final boolean shadows;

  public EnvironmentLight(RayShader env, boolean shadows) {
    this.env = env;
    this.shadows = shadows;
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target) {
    Vector3 v = RandomUtil.uniformOnSphere(rnd).toCartesian();
    if (x.getNormal().dot(v) < 0.0) {
      v = v.opposite();
    }
    Ray3 ray = new Ray3(x.getPosition(), v);
    Color color = env.shadeRay(ray, lambda).times(0.5);
    target.addLightSample(new DirectionalLightSample(x, v, color, shadows));
  }
}
