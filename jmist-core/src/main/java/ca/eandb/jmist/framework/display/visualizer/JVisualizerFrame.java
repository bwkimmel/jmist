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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.util.io.FileUtil;

/**
 * @author brad
 *
 */
public final class JVisualizerFrame extends JFrame implements Display {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6053456293463524773L;

  private static final String DEFAULT_TITLE = "jMIST Visualizer";

  private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);

  private static final double[] ZOOM_FACTORS = {
     0.02,  0.05,  0.10,  0.20,  0.33,  0.50,  0.67,  1.00,  2.00,  3.00,
     4.00,  5.00,  6.00,  7.00,  8.00,  9.00, 10.00, 11.00, 12.00, 13.00,
    14.00, 15.00, 16.00, 17.00, 18.00, 19.00, 20.00
  };

  private final JVisualizerDisplay display;

  private final JScrollPane displayScrollPane;

  private final JCheckBoxMenuItem fitInWindowMenuItem;

  private final JCheckBoxMenuItem fillWindowMenuItem;

  private int width;

  private int height;

  private static enum ZoomMode {
    NORMAL,
    FIT,
    FILL
  };

  private ZoomMode zoomMode = ZoomMode.NORMAL;

  private double zoomFactor = 1.0;

  public JVisualizerFrame(JColorVisualizerPanel visualizerPanel) {
    display = new JVisualizerDisplay(visualizerPanel);

    displayScrollPane = new JScrollPane(display);
    displayScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    displayScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    displayScrollPane.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        displayScrollPane_OnComponentResized(e);
      }
    });

    setLayout(new BorderLayout());
    add(displayScrollPane, BorderLayout.CENTER);
    add(visualizerPanel, BorderLayout.SOUTH);

    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    JMenuItem menuItem;

    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_F);

    menuItem = new JMenuItem("Save", KeyEvent.VK_S);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Save the image to a file.");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        save_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);
    menuBar.add(menu);

    menu = new JMenu("View");
    menu.setMnemonic(KeyEvent.VK_V);

    menuItem = new JMenuItem("Zoom In");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, ActionEvent.CTRL_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Enlarge the image");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        zoomIn_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);

    menuItem = new JMenuItem("Zoom Out");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Shrink the image");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        zoomOut_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);

    menuItem = new JMenuItem("Normal Size");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.CTRL_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Show the image at its normal size");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        normalSize_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);

    menuItem = fitInWindowMenuItem = new JCheckBoxMenuItem("Fit in Window");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("Adjust the zoom ratio so that the image becomes fully visible");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fitInWindow_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);

    menuItem = fillWindowMenuItem = new JCheckBoxMenuItem("Fill Window");
    menuItem.getAccessibleContext().setAccessibleDescription("Adjust the zoom ratio so that the image fills the window");
    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fillWindow_OnActionPerformed(e);
      }
    });
    menu.add(menuItem);
    menuBar.add(menu);

    setJMenuBar(menuBar);
    setTitle(DEFAULT_TITLE);
    setMinimumSize(MINIMUM_SIZE);

    pack();
  }

  private void displayScrollPane_OnComponentResized(ComponentEvent e) {
    switch (zoomMode) {
    case FIT:
      fitInWindow();
      break;

    case FILL:
      fillWindow();
      break;
    }
  }

  private void fillWindow_OnActionPerformed(ActionEvent e) {
    setZoomMode(ZoomMode.FILL);
    fillWindow();
  }

  private void fillWindow() {
    Dimension d = displayScrollPane.getViewport().getSize();
    double z = Math.min((double) d.width / (double) width, (double) d.height / (double) height);
    updateZoom(z);
  }

  private void fitInWindow_OnActionPerformed(ActionEvent e) {
    setZoomMode(ZoomMode.FIT);
    fitInWindow();
  }

  private void fitInWindow() {
    Dimension d = displayScrollPane.getViewport().getSize();
    double zoom = Math.min((double) d.width / (double) width, (double) d.height / (double) height);
    if (zoom < 1.0) {
      updateZoom(zoom);
    } else {
      clearZoom();
    }
  }

  private void normalSize_OnActionPerformed(ActionEvent e) {
    setZoomMode(ZoomMode.NORMAL);
    clearZoom();
  }

  private void zoomOut_OnActionPerformed(ActionEvent e) {
    int index = Arrays.binarySearch(ZOOM_FACTORS, zoomFactor);
    index = index < 0 ? -index - 2 : index - 1;
    if (index >= 0) {
      setZoomMode(ZoomMode.NORMAL);
      updateZoom(ZOOM_FACTORS[index]);
    }
  }

  private void zoomIn_OnActionPerformed(ActionEvent e) {
    int index = Arrays.binarySearch(ZOOM_FACTORS, zoomFactor);
    index = index < 0 ? -index - 1 : index + 1;
    if (index < ZOOM_FACTORS.length) {
      setZoomMode(ZoomMode.NORMAL);
      updateZoom(ZOOM_FACTORS[index]);
    }
  }

  private void setZoomMode(ZoomMode mode) {
    zoomMode = mode;
    fitInWindowMenuItem.setSelected(mode == ZoomMode.FIT);
    fillWindowMenuItem.setSelected(mode == ZoomMode.FILL);
  }

  private void clearZoom() {
    display.setPreferredSize(new Dimension(width, height));
    display.setSize(display.getPreferredSize());
    display.repaint();
  }

  private void updateZoom(double zoom) {
    int w = (int) Math.floor(zoom * (double) width);
    int h = (int) Math.floor(zoom * (double) height);
    display.setPreferredSize(new Dimension(w, h));
    display.setSize(display.getPreferredSize());
    display.repaint();
    zoomFactor = zoom;
  }

  private void save_OnActionPerformed(ActionEvent event) {
    JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
          "Image Files", ImageIO.getWriterFileSuffixes());
      chooser.setFileFilter(filter);
    chooser.setAcceptAllFileFilterUsed(false);
      int returnVal = chooser.showSaveDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
      if (file.exists()) {
        String message = String
            .format("The file `%s' already exists.  Do you wish to overwrite this file?",
                file.getName());
        if (JOptionPane.showConfirmDialog(this, message, "File Exists",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
          return;
        }
      }
        String suffix = FileUtil.getExtension(file.getName());
        try {
        boolean foundWriter = ImageIO.write(display.getRenderedImage(),
            suffix, file);
        if (!foundWriter) {
          String message = String.format(
              "Invalid image file format (%s)", suffix);
          JOptionPane.showMessageDialog(this, message,
              "Error saving image", JOptionPane.WARNING_MESSAGE);
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(),
            "Error saving image", JOptionPane.WARNING_MESSAGE);
      }
      }
  }

  @Override
  public void initialize(int w, int h, ColorModel colorModel) {
    width = w;
    height = h;
    display.initialize(w, h, colorModel);
  }

  @Override
  public void fill(int x, int y, int w, int h, Color color) {
    display.fill(x, y, w, h, color);
  }

  @Override
  public void setPixel(int x, int y, Color pixel) {
    display.setPixel(x, y, pixel);
  }

  @Override
  public void setPixels(int x, int y, Raster pixels) {
    display.setPixels(x, y, pixels);
  }

  @Override
  public void finish() {
    display.finish();
  }

}
