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
package ca.eandb.jmist.framework.display.visualizer;

import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;

/**
 * A <code>ColorVisualizer</code> that does not change its visualization
 * function.
 * @author Brad Kimmel
 */
public abstract class StaticColorVisualizer implements ColorVisualizer {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3346813094887603261L;

  @Override
  public final void addChangeListener(ChangeListener l) {
    /* nothing to do. */
  }

  @Override
  public final void removeChangeListener(ChangeListener l) {
    /* nothing to do. */
  }

  @Override
  public final boolean analyze(Iterable<Color> samples) {
    return false;
  }

}
