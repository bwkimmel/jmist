package ca.eandb.jmist.framework.modifier;

import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;

public final class DefaultShaderModifier implements Modifier {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1626026548675479671L;

  private final Shader shader;

  public DefaultShaderModifier(Shader shader) {
    this.shader = shader;
  }

  @Override
  public void modify(ShadingContext context) {
    if (context.getShader() == null) {
      context.setShader(shader);
    }
  }

}
