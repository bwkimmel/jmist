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
package ca.eandb.jmist.framework.display;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapperFactory;
import ca.eandb.jmist.framework.tone.swing.JToneMapperPanel;
import ca.eandb.jmist.math.Array2;

/**
 * A <code>Display</code> that shows the image on a <code>JComponent</code>.
 * @author Brad Kimmel
 */
public final class JComponentDisplay extends JComponent implements Display,
    Scrollable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8576499442928553047L;

  /**
   * The default fraction of pixels that need to be changed to trigger a
   * regeneration of the <code>ToneMapper</code>.
   * @see #toneMapperAgeThresholdFraction
   */
  private static final double DEFAULT_TONE_MAPPER_AGE_THRESHOLD = 0.05;

  /**
   * The <code>ToneMapperFactory</code> to use to convert high dynamic range
   * <code>CIEXYZ</code> values to displayable colours.
   */
  private final ToneMapperFactory toneMapperFactory;

  /**
   * The current <code>ToneMapper</code>.  This is regenerated as more of
   * the image is written.
   */
  private ToneMapper toneMapper = null;

  /** The high dynamic range pixel values. */
  private Array2<CIEXYZ> hdrImage = null;

  /** The low dynamic range image. */
  private BufferedImage ldrImage = null;

  /**
   * The fraction of the pixels that need to change to trigger the
   * <code>ToneMapper</code> to be regenerated.
   */
  private final double toneMapperAgeThresholdFraction;

  /**
   * The number of pixels that need to change to trigger the
   * <code>ToneMapper</code> to be regenerated.
   */
  private int toneMapperAgeThreshold;

  /**
   * The number of pixels that have been changed since the last time the
   * <code>ToneMapper</code> was regenerated.
   */
  private int toneMapperAge = 0;

  /**
   * Creates a new <code>JComponentDisplay</code>.
   */
  public JComponentDisplay() {
    this(ToneMapperFactory.IDENTITY_FACTORY, DEFAULT_TONE_MAPPER_AGE_THRESHOLD);
  }

  /**
   * Creates a new <code>JComponentDisplay</code>.
   * @param toneMapperFactory The <code>ToneMapperFactory</code> to use to
   *     create <code>ToneMapper</code>s based on the content of this display.
   */
  public JComponentDisplay(ToneMapperFactory toneMapperFactory) {
    this(toneMapperFactory, DEFAULT_TONE_MAPPER_AGE_THRESHOLD);
  }

  /**
   * Creates a new <code>JComponentDisplay</code>.
   * @param toneMapperFactory The <code>ToneMapperFactory</code> to use to
   *     map high dynamic range pixels to values displayable in a low
   *     dynamic range image.
   * @param toneMapperAgeThresholdFraction The fraction of pixels that need
   *     to change to trigger a regeneration of the <code>ToneMapper</code>.
   */
  public JComponentDisplay(ToneMapperFactory toneMapperFactory, double toneMapperAgeThresholdFraction) {
    this.toneMapperFactory =  toneMapperFactory;
    this.toneMapperAgeThresholdFraction = toneMapperAgeThresholdFraction;

    if (toneMapperFactory instanceof JToneMapperPanel) {
      ((JToneMapperPanel) toneMapperFactory).addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          toneMapperFactory_OnStateChanged(e);
        }
      });
    }
  }

  /**
   * Responds to a state-change event by the <code>ToneMapperFactory</code>
   * if it is an instance of a <code>JToneMapperPanel</code>.
   * @param e The <code>ChangeEvent</code> object describing the event.
   * @see ca.eandb.jmist.framework.tone.swing.JToneMapperPanel
   */
  private void toneMapperFactory_OnStateChanged(ChangeEvent e) {
    regenerateToneMapper();
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (ldrImage != null) {
      g.drawImage(ldrImage, 0, 0, null);
    }
  }

  /**
   * Regenerates the <code>ToneMapper</code> if no <code>ToneMapper</code> is
   * present or if the current <code>ToneMapper</code> is beyond the age
   * threshold.
   * @param samples The number of pixels being changed right now.
   * @return A value indicating if the <code>ToneMapper</code> was
   *     regenerated.
   */
  private boolean prepareToneMapper(int samples) {
    toneMapperAge += samples;
    if (toneMapper == null || toneMapperAge >= toneMapperAgeThreshold) {
      regenerateToneMapper();
      return true;
    }
    return false;
  }

  /**
   * Regenerates the <code>ToneMapper</code>.
   */
  private void regenerateToneMapper() {
    toneMapper = toneMapperFactory.createToneMapper(hdrImage);
    toneMapperAge = 0;
    reapplyToneMapper();
  }

  /**
   * Regenerates the low dynamic range image using the current
   * <code>ToneMapper</code>.
   */
  private void reapplyToneMapper() {
    int w = ldrImage.getWidth();
    int h = ldrImage.getHeight();
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        CIEXYZ xyz = hdrImage.get(x, y);
        if (xyz != null) {
          xyz = toneMapper.apply(xyz);
          int rgb = xyz.toRGB().toR8G8B8();
          ldrImage.setRGB(x, y, rgb);
        }
      }
    }
    super.repaint();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
   */
  public void fill(int x, int y, int w, int h, Color color) {
    CIEXYZ xyz = color.toXYZ();
    hdrImage.slice(x, y, w, h).setAll(xyz);

    if (!prepareToneMapper(w * h)) {
      int rgb = toneMapper.apply(xyz).toRGB().toR8G8B8();

      BufferedImage tile = ldrImage.getSubimage(x, y, w, h);
      for (int ry = 0; ry < h; ry++) {
        for (int rx = 0; rx < w; rx++) {
          tile.setRGB(rx, ry, rgb);
        }
      }
      super.repaint(x, y, w, h);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#finish()
   */
  public void finish() {
    if (toneMapperAge > 0) {
      toneMapper = toneMapperFactory.createToneMapper(hdrImage);
      toneMapperAge = 0;
      reapplyToneMapper();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
   */
  public void initialize(int w, int h, ColorModel colorModel) {
    hdrImage = new Array2<CIEXYZ>(w, h);
    ldrImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    toneMapper = null;
    toneMapperAgeThreshold = (int) Math
        .floor(toneMapperAgeThresholdFraction * (double) (w * h));
    super.setSize(w, h);
    super.setPreferredSize(new Dimension(w, h));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
   */
  public void setPixel(int x, int y, Color pixel) {
    CIEXYZ xyz = pixel.toXYZ();
    hdrImage.set(x, y, xyz);
    if (!prepareToneMapper(1)) {
      int rgb = toneMapper.apply(xyz).toRGB().toR8G8B8();
      ldrImage.setRGB(x, y, rgb);
      super.repaint(x, y, 1, 1);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
   */
  public void setPixels(int x, int y, Raster pixels) {
    int w = pixels.getWidth();
    int h = pixels.getHeight();
    Array2<CIEXYZ> hdrTile = hdrImage.slice(x, y, w, h);
    for (int ry = 0; ry < h; ry++) {
      for (int rx = 0; rx < w; rx++) {
        CIEXYZ xyz = pixels.getPixel(rx, ry).toXYZ();
        hdrTile.set(rx, ry, xyz);
      }
    }
    if (!prepareToneMapper(hdrTile.size())) {
      BufferedImage ldrTile = ldrImage.getSubimage(x, y, w, h);
      for (int ry = 0; ry < h; ry++) {
        for (int rx = 0; rx < w; rx++) {
          CIEXYZ xyz = hdrTile.get(rx, ry);
          int rgb = toneMapper.apply(xyz).toRGB().toR8G8B8();
          ldrTile.setRGB(rx, ry, rgb);
        }
      }
      super.repaint(x, y, w, h);
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
   */
  public Dimension getPreferredScrollableViewportSize() {
    return getPreferredSize();
  }

  /* (non-Javadoc)
   * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
   */
  public int getScrollableBlockIncrement(Rectangle visibleRect,
      int orientation, int direction) {
    return 1;
  }

  /* (non-Javadoc)
   * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
   */
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }

  /* (non-Javadoc)
   * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
   */
  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  /* (non-Javadoc)
   * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
   */
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - 1;
        } else {
            return visibleRect.height - 1;
        }
  }

}
