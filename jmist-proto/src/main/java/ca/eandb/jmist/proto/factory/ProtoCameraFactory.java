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

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.lens.OrthographicLens;
import ca.eandb.jmist.framework.lens.PinholeLens;
import ca.eandb.jmist.framework.lens.TransformableLens;
import ca.eandb.jmist.proto.CameraProtos;
import ca.eandb.jmist.proto.CameraProtos.OrthographicCamera;
import ca.eandb.jmist.proto.CameraProtos.PinholeCamera;

/**
 *
 */
public final class ProtoCameraFactory {

  private final ProtoCoreFactory coreFactory;

  public ProtoCameraFactory(ProtoCoreFactory coreFactory) {
    this.coreFactory = coreFactory;
  }

  public Lens createLens(CameraProtos.Camera camera) {
    Lens lens;
    switch (camera.getType()) {
      case PINHOLE:
        lens = createPinholeLens(camera.getPinholeCamera());
        break;
      case ORTHOGRAPHIC:
        lens = createOrthographicLens(camera.getOrthographicCamera());
        break;
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized camera type: %d.", camera.getType().getNumber()));
    }

    if (camera.getWorldToViewCount() > 0) {
      TransformableLens transformable = new TransformableLens(lens);
      transformable.transform(coreFactory.createAffineMatrix3(
          camera.getWorldToViewList()));
      lens = transformable;
    }

    return lens;
  }

  private Lens createPinholeLens(PinholeCamera pinholeCamera) {
    return PinholeLens.fromFieldOfView(pinholeCamera.getAngleX(),
                                       pinholeCamera.getAngleY());
  }

  private Lens createOrthographicLens(OrthographicCamera orthographicCamera) {
    return new OrthographicLens(orthographicCamera.getScaleX(),
                                orthographicCamera.getScaleY());
  }

}
