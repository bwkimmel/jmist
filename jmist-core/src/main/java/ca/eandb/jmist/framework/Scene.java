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
package ca.eandb.jmist.framework;

import java.io.Serializable;


/**
 * A scene to be rendered.
 * @author Brad Kimmel
 */
public interface Scene extends Bounded3, Serializable {

  /**
   * The <code>Light</code> sources in the scene.
   * @return The <code>Light</code> sources in the scene.
   */
  Light getLight();

  /**
   * The scene description (geometry, materials, shaders, etc.).
   * @return The scene description (geometry, materials, shaders, etc.).
   */
  SceneElement getRoot();

  /**
   * The <code>Lens</code> from which to view the scene.
   * @return The <code>Lens</code> from which to view the scene.
   */
  Lens getLens();

  /**
   * An <code>Animator</code> to adjust the time within the scene.
   * @return An <code>Animator</code> to adjust the time within the scene.
   */
  Animator getAnimator();

}
