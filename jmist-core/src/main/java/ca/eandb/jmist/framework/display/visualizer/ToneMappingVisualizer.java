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

import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.tone.ConstantToneMapperFactory;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapperFactory;
import ca.eandb.jmist.framework.tone.swing.JToneMapperPanel;

/**
 * @author brad
 *
 */
public final class ToneMappingVisualizer extends VolatileColorVisualizer {
  
  /** Serialization version ID. */
  private static final long serialVersionUID = 5354262221703551711L;

  private final ToneMapperFactory factory;
  
  private ToneMapper toneMapper = ToneMapper.IDENTITY;
  
  public ToneMappingVisualizer(ToneMapperFactory factory) {
    this.factory = factory;
    
    if (factory instanceof JToneMapperPanel) {
      ((JToneMapperPanel) factory).addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          factory_OnStateChanged(e);
        }
      });
    }
  }

  public ToneMappingVisualizer(ToneMapper toneMapper) {
    this.factory = new ConstantToneMapperFactory(toneMapper);
    this.toneMapper = toneMapper;
  }
  
  private void factory_OnStateChanged(ChangeEvent e) {
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
    ToneMapper newToneMapper = factory.createToneMapper(new SampleList(samples));
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
