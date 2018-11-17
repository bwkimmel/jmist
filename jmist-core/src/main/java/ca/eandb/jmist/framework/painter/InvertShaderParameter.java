package ca.eandb.jmist.framework.painter;

import java.io.Serializable;

import ca.eandb.jmist.framework.ShaderParameter;
import ca.eandb.jmist.framework.SurfacePoint;

public final class InvertShaderParameter implements ShaderParameter, Serializable {
  private final ShaderParameter inner;

  public InvertShaderParameter(ShaderParameter inner) {
    this.inner = inner;
  }

  @Override
  public double evaluate(SurfacePoint p) {
    return 1.0 - inner.evaluate(p);
  }
}
