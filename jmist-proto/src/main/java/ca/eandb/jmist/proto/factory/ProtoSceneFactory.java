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
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.accel.BoundingIntervalHierarchy;
import ca.eandb.jmist.framework.light.CompositeLight;
import ca.eandb.jmist.framework.light.SimpleCompositeLight;
import ca.eandb.jmist.framework.scene.AbstractScene;
import ca.eandb.jmist.framework.scene.MergeSceneElement;
import ca.eandb.jmist.proto.LightProtos;
import ca.eandb.jmist.proto.SceneProtos;
import ca.eandb.util.UnimplementedException;

/**
 *
 */
public final class ProtoSceneFactory {

  private final ProtoCameraFactory cameraFactory;
  private final ProtoLightFactory lightFactory;

  public ProtoSceneFactory(ProtoCameraFactory cameraFactory,
                           ProtoLightFactory lightFactory) {
    this.cameraFactory = cameraFactory;
    this.lightFactory = lightFactory;
  }

  public Scene createScene(final SceneProtos.Scene sceneIn) {
    return new AbstractScene() {
      Lens lens = cameraFactory.createLens(sceneIn.getCamera());
      Light light = createSceneLight(sceneIn);
      SceneElement root = new BoundingIntervalHierarchy(
          createSceneRoot(sceneIn));

      @Override
      public Light getLight() {
        return light;
      }

      @Override
      public SceneElement getRoot() {
        return root;
      }

      @Override
      public Lens getLens() {
        return lens;
      }
    };
  }

  private SceneElement createSceneRoot(SceneProtos.Scene sceneIn) {
    if (sceneIn.getObjectsCount() == 0) {
      throw new UnimplementedException("ProtoSceneFactory.createSceneRoot/n==0");
    } else if (sceneIn.getObjectsCount() == 1) {
      return createObject(sceneIn.getObjects(0));
    } else {
      MergeSceneElement root = new MergeSceneElement();
      for (SceneProtos.Object objectIn : sceneIn.getObjectsList()) {
        root.addChild(createObject(objectIn));
      }
      return root;
    }
  }

  private SceneElement createObject(SceneProtos.Object objectIn) {
    throw new UnimplementedException("ProtoSceneFactory.createObject");
  }

  private Light createSceneLight(SceneProtos.Scene sceneIn) {
    if (sceneIn.getLightsCount() == 0) {
      return Light.NULL;
    } else if (sceneIn.getLightsCount() == 1) {
      return lightFactory.createLight(sceneIn.getLights(0));
    } else {
      CompositeLight light = new SimpleCompositeLight();
      for (LightProtos.Light lightIn : sceneIn.getLightsList()) {
        light.addChild(lightFactory.createLight(lightIn));
      }
      return light;
    }
  }
}
