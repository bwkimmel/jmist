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
package ca.eandb.jmist.framework.function;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Function1;

/**
 * A <code>Function1</code> that is made up of other spectra.
 * @author Brad Kimmel
 */
public abstract class CompositeFunction1 implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5931608814801153372L;

  /** The component spectra. */
  private final List<Function1> children = new ArrayList<>();

  /**
   * Adds a <code>Function1</code> to this <code>CompositeFunction1</code>.
   * @param child The child <code>Function1</code> to add.
   * @return A reference to this <code>CompositeFunction1</code> so that calls
   *     to this method may be chained.
   */
  public CompositeFunction1 addChild(Function1 child) {
    this.children.add(child);
    return this;
  }

  /**
   * Gets the list of child spectra.
   * @return The <code>List</code> of child spectra.
   */
  protected final List<Function1> children() {
    return this.children;
  }

}
