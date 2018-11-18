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
package ca.eandb.jmist.framework.loader.ply;

import java.util.LinkedList;
import java.util.List;

/**
 * An <code>ElementListener</code> to that delegates to zero or more
 * other <code>ElementListener</code>s.
 */
public final class CompositeElementListener implements ElementListener {

  /** The <code>List</code> of child <code>ElementListener</code>s. */
  private final List<ElementListener> listeners = new LinkedList<>();

  /** Creates a new <code>CompositeElementListener</code>. */
  public CompositeElementListener() {}

  /**
   * Adds a new child <code>ElementListener</code>.
   * @param listener The <code>ElementListener</code> to add.
   * @return A reference to this <code>CompositeElementListener</code>.
   */
  public CompositeElementListener addListener(ElementListener listener) {
    listeners.add(listener);
    return this;
  }

  @Override
  public void element(PlyElement element) {
    for (ElementListener listener : listeners) {
      listener.element(element);
    }
  }

}
