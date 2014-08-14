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
package ca.eandb.jmist.framework.painter;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Painter;

/**
 * A <code>Painter</code> that is made up of other painters.
 * @author Brad Kimmel
 */
public abstract class CompositePainter implements Painter {

  /** Serialization version ID. */
  private static final long serialVersionUID = -141503160433974383L;

  /** The component painters. */
  private final List<Painter> children = new ArrayList<Painter>();

  /**
   * Adds a <code>Painter</code> to this <code>CompositePainter</code>.
   * @param child The child <code>Painter</code> to add.
   * @return A reference to this <code>CompositePainter</code> so that calls
   *     to this method may be chained.
   */
  public CompositePainter addChild(Painter child) {
    this.children.add(child);
    return this;
  }

  /**
   * Gets the list of child painters.
   * @return The <code>List</code> of child painters.
   */
  protected final List<Painter> children() {
    return this.children;
  }

}
