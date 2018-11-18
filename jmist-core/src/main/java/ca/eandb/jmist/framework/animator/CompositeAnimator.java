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
package ca.eandb.jmist.framework.animator;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Animator;

/**
 * An <code>Animator</code> that composes multiple child <code>Animator</code>s
 * and allows them be treated as a single <code>Animator</code>.
 * @author Brad Kimmel
 */
public final class CompositeAnimator implements Animator {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1091240735157061218L;

  /** The <code>List</code> of child <code>Animator</code>s. */
  private final List<Animator> children = new ArrayList<>();

  /**
   * Adds a child <code>Animator</code>.
   * @param child The <code>Animator</code> to append.
   * @return A reference to this <code>CompositeAnimator</code>.
   */
  public CompositeAnimator addChild(Animator child) {
    children.add(child);
    return this;
  }

  @Override
  public void setTime(double time) {
    for (Animator child : children) {
      child.setTime(time);
    }
  }

}
