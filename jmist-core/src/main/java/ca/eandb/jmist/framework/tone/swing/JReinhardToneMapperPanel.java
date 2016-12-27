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
package ca.eandb.jmist.framework.tone.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.tone.ReinhardToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.util.ui.JNumberLine;

/**
 * @author brad
 *
 */
public final class JReinhardToneMapperPanel extends JToneMapperPanel {

  /** Serialization version ID. */
  private static final long serialVersionUID = 3820623449350636976L;

  private static final double DELTA = 1.0;

  private static final double MIN_STOP = -10.0;
  private static final double MAX_STOP = 10.0;

  private final JCheckBox autoCheckBox;
  private final JNumberLine whiteSlider;
  private final JNumberLine scaleSlider;

  private boolean suspendChangeEvents = false;

  /**
   *
   */
  public JReinhardToneMapperPanel() {
    autoCheckBox = new JCheckBox("Automatic", true);
    whiteSlider = new JNumberLine(MIN_STOP, MAX_STOP, 0);
    scaleSlider = new JNumberLine(MIN_STOP, MAX_STOP, 0);

    whiteSlider.setEnabled(false);
    scaleSlider.setEnabled(false);

    autoCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        autoCheckBox_OnActionPerformed(e);
      }
    });

    whiteSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        whiteSlider_OnStateChanged(e);
      }
    });

    scaleSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        scaleSlider_OnStateChanged(e);
      }
    });


    setLayout(new GridBagLayout());

    JLabel label;
    GridBagConstraints c;

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 0;
    label = new JLabel("");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 1;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.LINE_START;
    add(autoCheckBox, c);

    c = new GridBagConstraints();
    c.gridy = 1;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    label = new JLabel("White");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 1;
    c.gridx = 1;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(whiteSlider, c);

    c = new GridBagConstraints();
    c.gridy = 2;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    label = new JLabel("Scale");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 2;
    c.gridx = 1;
    c.weighty = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(scaleSlider, c);

    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(0, 0));
    c = new GridBagConstraints();
    c.gridy = 3;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 1.0;
    add(panel, c);
  }

  private void whiteSlider_OnStateChanged(ChangeEvent e) {
    if (!whiteSlider.getValueIsAdjusting()) {
      fireStateChanged();
    }
  }

  protected void scaleSlider_OnStateChanged(ChangeEvent e) {
    if (!scaleSlider.getValueIsAdjusting()) {
      fireStateChanged();
    }
  }

  private void autoCheckBox_OnActionPerformed(ActionEvent e) {
    boolean custom = !autoCheckBox.isSelected();
    whiteSlider.setEnabled(custom);
    scaleSlider.setEnabled(custom);
    if (!custom) {
      fireStateChanged();
    }
  }

  @Override
  protected void fireStateChanged() {
    if (!suspendChangeEvents) {
      super.fireStateChanged();
    }
  }

  @Override
  public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
    double yWhite;
    double yScale;

    if (autoCheckBox.isSelected()) {
      double Yavg = 0.0;
      double Ymax = 0.0;
      int n = 0;
      for (CIEXYZ sample : samples) {
        if (sample != null) {
          double Y = sample.Y();
          if (Y > Ymax) {
            Ymax = Y;
          }
          Yavg += Math.log(DELTA + Y);
          n++;
        }
      }
      Yavg /= (double) n;
      Yavg = Math.exp(Yavg) - DELTA;

      double Ymid = 1.03 - 2.0 / (2.0 + Math.log10(Yavg + 1.0));
      yWhite = Ymax;
      yScale = Ymid / Yavg;

      double whiteSliderValue = Math.log(yWhite) / Math.log(2.0);
      double scaleSliderValue = Math.log(yScale) / Math.log(2.0);

      suspendChangeEvents = true;
      whiteSlider.setValue(whiteSliderValue);
      scaleSlider.setValue(scaleSliderValue);
      suspendChangeEvents = false;
    } else {
      yWhite = Math.pow(2.0, whiteSlider.getValue());
      yScale = Math.pow(2.0, scaleSlider.getValue());
    }

    return new ReinhardToneMapper(yScale, yWhite);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JReinhardToneMapperPanel factory = new JReinhardToneMapperPanel();
    frame.add(factory);
    frame.pack();
    frame.setVisible(true);

  }

}
