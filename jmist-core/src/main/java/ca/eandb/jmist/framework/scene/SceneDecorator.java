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

import ca.eandb.jmist.framework.Animator;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>Scene</code> that is based on another scene.
 * @author Brad Kimmel
 */
public abstract class SceneDecorator implements Scene {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5875837932212956822L;

  /** The <code>Scene</code> that this scene is to be based on. */
  private final Scene inner;

  /**
   * Initializes this <code>SceneDecorator</code>.
   * @param inner The <code>Scene</code> that this scene is to be based on.
   */
  protected SceneDecorator(Scene inner) {
    this.inner = inner;
  }

  @Override
  public Lens getLens() {
    return inner.getLens();
  }

  @Override
  public Light getLight() {
    return inner.getLight();
  }

  @Override
  public SceneElement getRoot() {
    return inner.getRoot();
  }

  @Override
  public Box3 boundingBox() {
    return inner.boundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    return inner.boundingSphere();
  }

  @Override
  public Animator getAnimator() {
    return inner.getAnimator();
  }

}
