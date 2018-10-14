package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Vector3;

public class TransformedLightSample implements LightSample {
  private final InvertibleAffineTransformation3 t = new InvertibleAffineTransformation3();

  private final LightSample inner;

  public TransformedLightSample(AffineMatrix3 T, LightSample inner) {
    this.inner = inner;
    t.transform(T);
  }

  @Override
  public Vector3 getDirToLight() {
    return t.apply(inner.getDirToLight());
  }

  @Override
  public Color getRadiantIntensity() {
    return inner.getRadiantIntensity();
  }

  @Override
  public boolean castShadowRay(VisibilityFunction3 vf) {
    return inner.castShadowRay(ray -> vf.visibility(t.apply(ray)));
  }
}
