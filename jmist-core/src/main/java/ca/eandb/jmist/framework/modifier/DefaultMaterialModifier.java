package ca.eandb.jmist.framework.modifier;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.ShadingContext;

public final class DefaultMaterialModifier implements Modifier {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5091302488495277751L;

  private final Material material;

  public DefaultMaterialModifier(Material material) {
    if (material.isEmissive()) {
      throw new IllegalArgumentException(
          "Default material may not be emissive");
    }
    this.material = material;
  }

  @Override
  public void modify(ShadingContext context) {
    if (context.getMaterial() == null) {
      context.setMaterial(material);
    }
  }

}
