package ca.eandb.jmist.proto.factory;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.proto.MaterialProtos;

public final class ProtoMaterialFactory {

  private final ProtoColorFactory colorFactory;

  private final ColorModel colorModel;

  public ProtoMaterialFactory(
      ProtoColorFactory colorFactory, ColorModel colorModel) {
    this.colorFactory = colorFactory;
    this.colorModel = colorModel;
  }

  public Material createMaterial(MaterialProtos.Material materialIn) {
    switch (materialIn.getParametersCase()) {
    case LAMBERTIAN_MATERIAL:
      return createLambertianMaterial(materialIn.getLambertianMaterial());
    case PARAMETERS_NOT_SET:
    default:
      return getDefaultMaterial();
    }
  }

  private Material createLambertianMaterial(
      MaterialProtos.LambertianMaterial lambertianMaterialIn) {
    return new LambertianMaterial(
        colorFactory.createSpectrum(lambertianMaterialIn.getReflectance()));
  }

  private Material getDefaultMaterial() {
    return new LambertianMaterial(colorModel.getGray(0.8));
  }

}
