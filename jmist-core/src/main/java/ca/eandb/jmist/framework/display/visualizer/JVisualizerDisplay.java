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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.BitSet;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;

/**
 * A <code>Display</code> that shows the image on a <code>JComponent</code>.
 * @author Brad Kimmel
 */
public final class JVisualizerDisplay extends JComponent implements Display,
    Scrollable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8576499442928553047L;

  /**
   * The default fraction of pixels that need to be changed to trigger a
   * regeneration of the <code>ColorVisualizer</code>'s visualization
   * function.
   * @see #visualizerAgeThresholdFraction
   */
  private static final double DEFAULT_VISUALIZER_AGE_THRESHOLD = 0.05;
  
  /**
   * The <code>ColorVisualizer</code> to use to convert <code>Color</code>s
   * to <code>RGB</code> triplets.
   */
  private final ColorVisualizer visualizer;
  
  /** The <code>ColorModel</code> used by the image. */
  private ColorModel colorModel = null;
  
  /** A <code>BitSet</code> indicating which pixels have been recorded. */
  private BitSet recorded = null;
  
  /** The original, high dynamic range pixel values recorded. */
  private Raster rawImage = null;

  /** The low dynamic range image. */
  private BufferedImage ldrImage = null;

  /**
   * The fraction of the pixels that need to change to trigger the
   * <code>ColorVisualizer</code> to be regenerated.
   */
  private final double visualizerAgeThresholdFraction;

  /**
   * The number of pixels that need to change to trigger the
   * <code>ColorVisualizer</code> to be regenerated.
   */
  private int visualizerAgeThreshold;

  /**
   * The number of pixels that have been changed since the last time the
   * <code>ColorVisualizer</code> was regenerated.
   */
  private int visualizerAge = 0;
  
  private final class PixelIterator implements Iterable<Color>, Iterator<Color> {

    private int index = 0;
    
    public PixelIterator() {
      index = recorded.nextSetBit(0);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Color> iterator() {
      return this;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
      return index >= 0;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public Color next() {
      int w = rawImage.getWidth();
      int x = index % w;
      int y = index / w;
      index = recorded.nextSetBit(index + 1);
      return rawImage.getPixel(x, y);
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
  };

  /**
   * Creates a new <code>JComponentDisplay</code>.
   */
  public JVisualizerDisplay() {
    this(ColorVisualizer.DEFAULT, DEFAULT_VISUALIZER_AGE_THRESHOLD);
  }

  /**
   * Creates a new <code>JComponentDisplay</code>.
   */
  public JVisualizerDisplay(ColorVisualizer visualizer) {
    this(visualizer, DEFAULT_VISUALIZER_AGE_THRESHOLD);
  }

  /**
   * Creates a new <code>JComponentDisplay</code>.
   * @param visualizer The <code>ColorVisualizer</code> to use to convert
   *     <code>Color</code> objects to <code>RGB</code> triplets.
   * @param visualizerAgeThresholdFraction The fraction of pixels that need
   *     to change to trigger a regeneration of the <code>ColorVisualizer</code>.
   */
  public JVisualizerDisplay(ColorVisualizer visualizer, double visualizerAgeThresholdFraction) {
    this.visualizer =  visualizer;
    this.visualizerAgeThresholdFraction = visualizerAgeThresholdFraction;
    
    visualizer.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        visualizer_OnStateChanged(e);
      }
    });
  }
  
  public void export(Display display) {
    if (rawImage == null || colorModel == null) {
      throw new IllegalStateException("Display not initialized");
    }
    int w = rawImage.getWidth();
    int h = rawImage.getHeight();
    display.initialize(w, h, colorModel);
    display.setPixels(0, 0, rawImage);
    display.finish();
  }
  
  public RenderedImage getRenderedImage() {
    if (ldrImage == null) {
      throw new IllegalStateException("Display not initialized");
    }
    return ldrImage;
  }

  /**
   * Responds to a state-change event by the <code>ColorVisualizer</code>.
   * @param e The <code>ChangeEvent</code> object describing the event.
   */
  private void visualizer_OnStateChanged(ChangeEvent e) {
    regenerateVisualizer(true);
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  public void paint(Graphics g) {
//    super.paintComponent(g);
    super.paint(g);
    if (ldrImage != null) {
      Dimension d = getSize();
      g.drawImage(ldrImage, 0, 0, d.width, d.height, null);
    }
  }

  /**
   * Regenerates the <code>ColorVisualizer</code> if it has not been
   * regenerated since the last <code>visualizerAgeThreshold</code> pixels
   * have been recorded.
   * @param samples The number of pixels being changed right now.
   * @return A value indicating if the visualization function has changed.
   */
  private boolean prepareVisualizer(int samples) {
    visualizerAge += samples;
    if (visualizerAge >= visualizerAgeThreshold) {
      return regenerateVisualizer(false);
    }
    return false;
  }
  
  /**
   * Regenerates the <code>ColorVisualizer</code>.
   * @param forceReapply Forces the display to reapply the visualization
   *     function even if {@link ColorVisualizer#analyze(Iterable)} reports
   *     no changes.
   * @return A value indicating whether the visualization function has
   *     changed.
   */
  private boolean regenerateVisualizer(boolean forceReapply) {
    visualizerAge = 0;
    if (visualizer.analyze(new PixelIterator()) || forceReapply) {
      reapplyVisualizer();
      return true;
    }
    return false;
  }

  /**
   * Regenerates the low dynamic range image.
   */
  private void reapplyVisualizer() {
    int w = ldrImage.getWidth();
    for (int i = recorded.nextSetBit(0); i >= 0; i = recorded.nextSetBit(i + 1)) {
      int x = i % w;
      int y = i / w;
      Color pixel = rawImage.getPixel(x, y);
      int rgb = visualizer.visualize(pixel).toR8G8B8();
      ldrImage.setRGB(x, y, rgb);
    }
    super.repaint();
  }
  
  /**
   * Repaints the specified rectangular portion of the image.
   * @param x The x-coordinate of the upper left pixel in the image to
   *     repaint.
   * @param y The y-coordinate of the upper left pixel in the image to
   *     repaint.
   * @param w The width of the rectangular portion of the image to
   *     repaint.
   * @param h The height of the rectangular portion of the image to
   *     repaint.
   */
  private void repaintPartialImage(int x, int y, int w, int h) {
    Dimension d = getSize();
    int imageW = ldrImage.getWidth();
    int imageH = ldrImage.getHeight();
    double sx = (double) d.width / (double) imageW;
    double sy = (double) d.height / (double) imageH;
    
    int x0 = (int) Math.floor(sx * (double) x);
    int y0 = (int) Math.floor(sy * (double) y);
    int x1 = (int) Math.ceil(sx * (double) (x + w - 1));
    int y1 = (int) Math.ceil(sy * (double) (y + h - 1));
    int dx = x1 - x0 + 1;
    int dy = y1 - y0 + 1;
    
    super.repaint(x0, y0, dx, dy);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
   */
  public void fill(int x, int y, int w, int h, Color color) {
    int iw = rawImage.getWidth();
    rawImage.setPixel(x, y, color);
    for (int cy = y; cy < y + h; cy++) {
      recorded.set(cy * iw + x, cy * iw + (x + w));
    }
    
    if (!prepareVisualizer(w * h)) {
      int rgb = visualizer.visualize(color).toR8G8B8();

      BufferedImage tile = ldrImage.getSubimage(x, y, w, h);
      for (int ry = 0; ry < h; ry++) {
        for (int rx = 0; rx < w; rx++) {
          tile.setRGB(rx, ry, rgb);
        }
      }
      repaintPartialImage(x, y, w, h);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#finish()
   */
  public void finish() {
    if (visualizerAge > 0) {
      visualizerAge = 0;
      if (visualizer.analyze(new PixelIterator())) {
        reapplyVisualizer();
      }
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
   */
  public void initialize(int w, int h, ColorModel colorModel) {
    this.colorModel = colorModel;
    recorded = new BitSet(w * h);
    rawImage = colorModel.createRaster(w, h);
    ldrImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    visualizerAgeThreshold = (int) Math
        .floor(visualizerAgeThresholdFraction * (double) (w * h));
    super.setSize(w, h);
    super.setPreferredSize(new Dimension(w, h));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
   */
  public void setPixel(int x, int y, Color pixel) {
    rawImage.setPixel(x, y, pixel);
    recorded.set(y * rawImage.getWidth() + x);
    if (!prepareVisualizer(1)) {
      int rgb = visualizer.visualize(pixel).toR8G8B8();
      ldrImage.setRGB(x, y, rgb);
      repaintPartialImage(x, y, 1, 1);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
   */
  public void setPixels(int x, int y, Raster pixels) {
    int w = pixels.getWidth();
    int h = pixels.getHeight();
    int iw = rawImage.getWidth();
    
    for (int dy = 0; dy < h; dy++) {
      for (int dx = 0; dx < w; dx++) {
        rawImage.setPixel(x + dx, y + dy, pixels.getPixel(dx, dy));
      }

      recorded.set((y + dy) * iw + x, (y + dy) * iw + (x + w));
    }

    if (!prepareVisualizer(w * h)) {
      BufferedImage ldrTile = ldrImage.getSubimage(x, y, w, h);
      for (int ry = 0; ry < h; ry++) {
        for (int rx = 0; rx < w; rx++) {
          Color pixel = pixels.getPixel(rx, ry);
          int rgb = visualizer.visualize(pixel).toR8G8B8();
          ldrTile.setRGB(rx, ry, rgb);
        }
      }
      repaintPartialImage(x, y, w, h);
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
