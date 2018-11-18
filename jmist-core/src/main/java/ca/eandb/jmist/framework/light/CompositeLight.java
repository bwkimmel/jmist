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
package ca.eandb.jmist.framework.light;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.eandb.jmist.framework.Light;

public abstract class CompositeLight extends AbstractLight {

  /** Serialization version ID. */
  private static final long serialVersionUID = -741001548686087987L;

  /** The child lights. */
  private final List<Light> children = new ArrayList<>();

  /**
   * Initializes this <code>CompositeLight</code> with no light sources.
   */
  protected CompositeLight() {
    /* nothing to do. */
  }

  /**
   * Initializes this <code>CompositeLight</code> with the specified child
   * light sources.
   * @param children The <code>Collection</code> of lights making up this light.
   */
  protected CompositeLight(Collection<? extends Light> children) {
    this.children.addAll(children);
  }

  /**
   * Adds a child <code>Light</code> to this <code>CompositeLight</code>.
   * @param child The child <code>Light</code> to add.
   * @return A reference to this <code>CompositeLight</code> so that calls to
   *     this method may be chained.
   */
  public CompositeLight addChild(Light child) {
    this.children.add(child);
    return this;
  }

  /**
   * Gets the list of child lights.
   * @return The <code>List</code> of child lights.
   */
  protected final List<Light> children() {
    return this.children;
  }

}
