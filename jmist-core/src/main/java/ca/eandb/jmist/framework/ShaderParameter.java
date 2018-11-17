package ca.eandb.jmist.framework;

import java.io.Serializable;

@FunctionalInterface
public interface ShaderParameter extends Serializable {
  double evaluate(SurfacePoint p);

  static ShaderParameter constant(double value) {
    return p -> value;
  }

  ShaderParameter ZERO = constant(0.0);
}
