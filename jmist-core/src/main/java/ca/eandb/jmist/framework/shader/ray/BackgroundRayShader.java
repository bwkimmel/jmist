package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ShaderParameter;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Ray3;

public class BackgroundRayShader implements RayShader {

  private final Painter color;

  private final ShaderParameter strength;

  public BackgroundRayShader(Painter color, ShaderParameter strength) {
    this.color = color;
    this.strength = strength;
  }

  @Override
  public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
    MinimalShadingContext sc = new MinimalShadingContext();
    sc.setPosition(ray.origin());
    sc.setNormal(ray.direction());
    sc.setMaterial(Material.BLACK);
    sc.setAmbientMedium(Material.VACUUM);
    return color.getColor(sc, lambda).times(strength.evaluate(sc));
  }
}
