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

import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.tone.swing.JToneMapperPanel;

/**
 * @author brad
 *
 */
public abstract class JColorVisualizerPanel extends JPanel implements
    ColorVisualizer {
  
  /** Serialization version ID. */
  private static final long serialVersionUID = 4463169883690282424L;
  
  /**
   * The <code>List</code> of <code>ChangeListener</code>s to be notified
   * when the visualization function changes.
   */
  private final List<ChangeListener> listeners = new ArrayList<ChangeListener>(); 

  /**
   * 
   */
  public JColorVisualizerPanel() {
    super();
  }

  /**
   * @param isDoubleBuffered
   */
  public JColorVisualizerPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
  }

  /**
   * @param layout
   * @param isDoubleBuffered
   */
  public JColorVisualizerPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
  }

  /**
   * @param layout
   */
  public JColorVisualizerPanel(LayoutManager layout) {
    super(layout);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#addChangeListener(javax.swing.event.ChangeListener)
   */
  @Override
  public final void addChangeListener(ChangeListener l) {
    listeners.add(l);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#removeChangeListener(javax.swing.event.ChangeListener)
   */
  @Override
  public final void removeChangeListener(ChangeListener l) {
    listeners.remove(l);
  }
  
  /**
   * Notifies all registered <code>ChangeListener</code>s that the
   * visualization function has changed.
   */
  protected final void fireStateChanged() {
    for (ChangeListener l : listeners) {
      l.stateChanged(new ChangeEvent(this));
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
   */
  @Override
  public boolean analyze(Iterable<Color> samples) {
    return false;
  }

  /**
   * Creates a <code>JColorVisualizerPanel</code> that allows for switching
   * between all visualizer types.
   * @param cm The <code>ColorModel</code> to use.
   */
  public static JColorVisualizerPanel allColorVisualizers(ColorModel cm) {
    return new JCompositeVisualizerPanel()
        .addChild("Default",
            new JHighlightAnomaliesVisualizerPanel(
                new JDefaultVisualizerPanel()))
        .addChild("Tone Mapped",
            new JHighlightAnomaliesVisualizerPanel(
                new JToneMappingVisualizerPanel(
                    JToneMapperPanel.allToneMappers())))
        .addChild("Channels",
            new JHighlightAnomaliesVisualizerPanel(
                new JChannelVisualizerPanel(cm)))
        ;
  }

}
