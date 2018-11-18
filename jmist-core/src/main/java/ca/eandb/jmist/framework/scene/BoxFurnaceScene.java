/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.geometry.InsideOutGeometry;
import ca.eandb.jmist.framework.geometry.primitive.BoxGeometry;
import ca.eandb.jmist.framework.lens.FisheyeLens;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.math.Box3;

/**
 * A <code>Scene</code> representing the inside of a box in which all surfaces
 * are Lambertian surface that emit and reflect uniformly.  A correct rendering
 * of this scene should result in a uniform, flat color, image. regardless of
 * the view point or view direction, or of the actual emission or reflectances
 * of the surfaces.
 * @author Brad Kimmel
 */
public final class BoxFurnaceScene extends AbstractScene {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8454473677375672984L;

  private static final Lens lens = new FisheyeLens();

  private final SceneElement root;

  public BoxFurnaceScene(Spectrum reflectance, Spectrum emittance) {
    root = new MaterialSceneElement(
        new LambertianMaterial(reflectance, emittance),
        new InsideOutGeometry(new BoxGeometry(new Box3(-1, -1, -1, 1, 1, 1))));
  }

  @Override
  public Lens getLens() {
    return lens;
  }

  @Override
  public Light getLight() {
    return root.createLight();
  }

  @Override
  public SceneElement getRoot() {
    return root;
  }

}
