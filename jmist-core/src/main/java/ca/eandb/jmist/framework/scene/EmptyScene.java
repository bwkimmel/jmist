/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
 * An empty <code>Scene</code>.
 * @author Brad Kimmel
 */
public final class EmptyScene implements Scene {

  /** Serialization version ID. */
  private static final long serialVersionUID = -2855797477543563321L;

  public static final Scene INSTANCE = new EmptyScene();

  private EmptyScene() {
    /* nothing to do. */
  }

  @Override
  public Lens getLens() {
    return Lens.NULL;
  }

  @Override
  public Light getLight() {
    return Light.NULL;
  }

  @Override
  public SceneElement getRoot() {
    return NullSceneElement.INSTANCE;
  }

  @Override
  public Box3 boundingBox() {
    return Box3.EMPTY;
  }

  @Override
  public Sphere boundingSphere() {
    return Sphere.EMPTY;
  }

  @Override
  public Animator getAnimator() {
    return Animator.STATIC;
  }

}
