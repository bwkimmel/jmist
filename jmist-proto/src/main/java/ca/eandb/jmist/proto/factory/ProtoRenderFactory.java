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

import ca.eandb.jdcp.job.ParallelizableJob;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.rgb.RGBColorModel;
import ca.eandb.jmist.framework.color.xyz.XYZColorModel;
import ca.eandb.jmist.framework.job.RasterJob;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.modifier.CompositeModifier;
import ca.eandb.jmist.framework.modifier.DefaultMaterialModifier;
import ca.eandb.jmist.framework.modifier.DefaultShaderModifier;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.framework.scene.ModifierSceneElement;
import ca.eandb.jmist.framework.scene.SceneDecorator;
import ca.eandb.jmist.framework.shader.DirectLightingShader;
import ca.eandb.jmist.framework.shader.image.CameraImageShader;
import ca.eandb.jmist.framework.shader.pixel.AveragingPixelShader;
import ca.eandb.jmist.framework.shader.pixel.RandomPixelShader;
import ca.eandb.jmist.framework.shader.ray.SceneRayShader;
import ca.eandb.jmist.proto.RenderProtos;

/**
 *
 */
public final class ProtoRenderFactory {

  public ColorModel createColorModel(RenderProtos.ColorModel colorModelIn) {
    switch (colorModelIn.getType()) {
      case RGB:
        return RGBColorModel.getInstance();
      case XYZ:
        return XYZColorModel.getInstance();
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized color model type: %d",
            colorModelIn.getType().getNumber()));
    }
  }

  private Shader createDefaultShader(RenderProtos.RenderJob jobIn) {
    return new DirectLightingShader();
  }

  private Material createDefaultMaterial(ColorModel colorModel) {
    return new LambertianMaterial(colorModel.getGray(0.8));
  }

  public ParallelizableJob createRenderJob(RenderProtos.RenderJob jobIn,
                                           Display display) {
    ColorModel colorModel = jobIn.hasColorModel() ?
        createColorModel(jobIn.getColorModel()) : RGBColorModel.getInstance();
    ProtoFactories factories = new ProtoFactories(colorModel);
    ProtoSceneFactory sceneFactory = factories.getSceneFactory();
    Scene scene = new SceneDecorator(sceneFactory.createScene(jobIn.getScene())) {
      private static final long serialVersionUID = 1L;
      private final SceneElement root = new ModifierSceneElement(
          new CompositeModifier()
              .addModifier(
                  new DefaultMaterialModifier(
                      createDefaultMaterial(colorModel)))
              .addModifier(
                  new DefaultShaderModifier(createDefaultShader(jobIn))),
          super.getRoot());

      @Override
      public SceneElement getRoot() {
        return root;
      }
    };
    RayShader rayShader = new SceneRayShader(scene);
    ImageShader imageShader = new CameraImageShader(scene.getLens(), rayShader);
    Random pixelRandom = new ThreadLocalRandom(new SimpleRandom());
//        new NRooksRandom(jobIn.getSamplesPerPixel(), 2));
    PixelShader pixelShader = new RandomPixelShader(pixelRandom, imageShader,
                                                    colorModel);
    if (jobIn.getSamplesPerPixel() > 1) {
      pixelShader = new AveragingPixelShader(jobIn.getSamplesPerPixel(),
                                             pixelShader);
    }
    RenderProtos.Size imageSize = jobIn.getImageSize();
    RasterJob.Builder builder = RasterJob.newBuilder()
        .setColorModel(colorModel)
        .setPixelShader(pixelShader)
        .setDisplay(display)
        .setImageSize(imageSize.getX(), imageSize.getY());
    if (jobIn.hasTileSize()) {
      builder.setTileSize(
          jobIn.getTileSize().getX(), jobIn.getTileSize().getY());
    } else {
      builder.setTileCount(10, 10);
    }
    return builder.build();
  }

}
