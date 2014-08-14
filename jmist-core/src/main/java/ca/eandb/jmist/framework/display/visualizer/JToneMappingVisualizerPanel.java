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

import java.awt.BorderLayout;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.framework.tone.swing.JToneMapperPanel;

/**
 * @author brad
 *
 */
public final class JToneMappingVisualizerPanel extends JColorVisualizerPanel {

  /** Serialization version ID. */
  private static final long serialVersionUID = 719638646469519578L;

  private final JToneMapperPanel toneMapperPanel;
  
  private ToneMapper toneMapper = ToneMapper.IDENTITY;
  
  public JToneMappingVisualizerPanel(JToneMapperPanel toneMapperPanel) {
    super(new BorderLayout());
    this.toneMapperPanel = toneMapperPanel;
    
    add(toneMapperPanel, BorderLayout.CENTER);
    
    toneMapperPanel.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        toneMapperPanel_OnStateChanged(e);
      }
    });
  }
  
  private void toneMapperPanel_OnStateChanged(ChangeEvent e) {
    fireStateChanged();
  }
  
  private static final class SampleIterator implements Iterator<CIEXYZ> {
    
    private final Iterator<Color> inner;
    
    public SampleIterator(Iterator<Color> inner) {
      this.inner = inner;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
      return inner.hasNext();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public CIEXYZ next() {
      return inner.next().toXYZ();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
      inner.remove();
    }
    
  }
  
  private static final class SampleList implements Iterable<CIEXYZ> {
    
    private final Iterable<Color> inner;
    
    public SampleList(Iterable<Color> inner) {
      this.inner = inner;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<CIEXYZ> iterator() {
      return new SampleIterator(inner.iterator());
    }
    
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#analyze(java.lang.Iterable)
   */
  @Override
  public boolean analyze(Iterable<Color> samples) {
    ToneMapper newToneMapper = toneMapperPanel.createToneMapper(new SampleList(samples));
    if (toneMapper != newToneMapper) {
      toneMapper = newToneMapper;
      return true;
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public RGB visualize(Color color) {
    return toneMapper.apply(color.toXYZ()).toRGB();
  }

}
