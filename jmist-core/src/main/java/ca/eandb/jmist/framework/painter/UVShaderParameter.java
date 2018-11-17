package ca.eandb.jmist.framework.painter;

import java.io.Serializable;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.ShaderParameter;
import ca.eandb.jmist.framework.SurfacePoint;

public final class UVShaderParameter implements ShaderParameter, Serializable {
  private final Mask2 mask;

  public UVShaderParameter(Mask2 mask) {
    this.mask = mask;
  }

  @Override
  public double evaluate(SurfacePoint p) {
    return mask.opacity(p.getUV());
  }
}
