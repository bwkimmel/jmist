/* Copyright (c) 2014 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.proto.factory;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.geometry.primitive.RectangleGeometry;
import ca.eandb.jmist.framework.light.DirectionalLight;
import ca.eandb.jmist.framework.light.PointLight;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.scene.MaterialSceneElement;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.proto.LightProtos;

/**
 *
 */
public final class ProtoLightFactory {

  private final ProtoColorFactory colorFactory;
  private final ProtoCoreFactory coreFactory;
  private final ColorModel colorModel;

  public ProtoLightFactory(ProtoColorFactory colorFactory,
                           ProtoCoreFactory coreFactory,
                           ColorModel colorModel) {
    this.colorFactory = colorFactory;
    this.coreFactory = coreFactory;
    this.colorModel = colorModel;
  }

  public Light createLight(LightProtos.Light lightIn) {
    switch (lightIn.getType()) {
      case POINT:
        return createPointLight(lightIn);
      case AREA:
        return createAreaLight(lightIn);
      case DIRECTIONAL:
        return createDirectionalLight(lightIn);
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized light type: %d.", lightIn.getType().getNumber()));
    }
  }

  private Light createPointLight(LightProtos.Light lightIn) {
    return new PointLight(
        coreFactory.createPoint3(lightIn.getPointLight().getPosition()),
        colorFactory.createSpectrum(lightIn.getColor(), lightIn.getEnergy()),
        lightIn.getShadow());
  }

  private Light createDirectionalLight(LightProtos.Light lightIn) {
    return new DirectionalLight(
        coreFactory.createVector3(lightIn.getDirectionalLight().getDirection()),
        colorFactory.createSpectrum(lightIn.getColor(), lightIn.getEnergy()),
        lightIn.getShadow());
  }

  private Light createAreaLight(LightProtos.Light lightIn) {
    Material material = new LambertianMaterial(
        colorModel.getBlack(),
        colorFactory.createSpectrum(lightIn.getColor(), lightIn.getEnergy()));
    LightProtos.AreaLight areaLight = lightIn.getAreaLight();
    SceneElement geometry = new RectangleGeometry(
        coreFactory.createPoint3(areaLight.getCenter()),
        Basis3.fromUV(
            coreFactory.createVector3(areaLight.getU()),
            coreFactory.createVector3(areaLight.getV())),
        areaLight.getSizeU(), areaLight.getSizeV(),
        false);
    SceneElement object = new MaterialSceneElement(material, geometry);
    return object.createLight();
  }

}
