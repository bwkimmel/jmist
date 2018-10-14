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
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.tone.LinearToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.util.ui.JNumberLine;

/**
 * @author brad
 *
 */
public final class JLinearToneMapperPanel extends JToneMapperPanel {

  /** Serialization version ID. */
  private static final long serialVersionUID = -376526192816345718L;

  private static final double DELTA = 1.0;

  private static final int MAX_CHROMATICITY_SLIDER_VALUE = 120;

  private static final double MIN_STOP = -10.0;
  private static final double MAX_STOP = 10.0;

  private final JCheckBox autoCheckBox;
  private final JNumberLine whiteLuminanceSlider;
  private final JSlider whiteXChromaticitySlider;
  private final JSlider whiteYChromaticitySlider;

  private boolean suspendChangeEvents = false;

  /**
   *
   */
  public JLinearToneMapperPanel() {
    autoCheckBox = new JCheckBox("Automatic", true);
    whiteLuminanceSlider = new JNumberLine(MIN_STOP, MAX_STOP, 0);
    whiteXChromaticitySlider = new JSlider(0, MAX_CHROMATICITY_SLIDER_VALUE, MAX_CHROMATICITY_SLIDER_VALUE / 3);
    whiteYChromaticitySlider = new JSlider(0, MAX_CHROMATICITY_SLIDER_VALUE, MAX_CHROMATICITY_SLIDER_VALUE / 3);

    whiteLuminanceSlider.setEnabled(false);
    whiteXChromaticitySlider.setEnabled(false);
    whiteYChromaticitySlider.setEnabled(false);

    autoCheckBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        autoCheckBox_OnActionPerformed(e);
      }
    });

    whiteLuminanceSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        whiteLuminanceSlider_OnStateChanged(e);
      }
    });

    whiteXChromaticitySlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        whiteXChromaticitySlider_OnStateChanged(e);
      }
    });

    whiteYChromaticitySlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        whiteYChromaticitySlider_OnStateChanged(e);
      }
    });

    JLabel label;

    setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
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
    label = new JLabel("White Y");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 1;
    c.gridx = 1;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(whiteLuminanceSlider, c);

    c = new GridBagConstraints();
    c.gridy = 2;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    label = new JLabel("White x");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 2;
    c.gridx = 1;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(whiteXChromaticitySlider, c);

    c = new GridBagConstraints();
    c.gridy = 3;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    label = new JLabel("White y");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 3;
    c.gridx = 1;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(whiteYChromaticitySlider, c);

    c = new GridBagConstraints();
    c.gridy = 4;
    c.gridx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(0, 0));
    add(panel, c);
  }

  private void whiteLuminanceSlider_OnStateChanged(ChangeEvent e) {
    if (!whiteLuminanceSlider.getValueIsAdjusting()) {
      fireStateChanged();
    }
  }

  private void whiteYChromaticitySlider_OnStateChanged(ChangeEvent e) {
    int x = whiteXChromaticitySlider.getValue();
    int y = whiteYChromaticitySlider.getValue();
    if (x + y > MAX_CHROMATICITY_SLIDER_VALUE) {
      suspendChangeEvents = true;
      whiteXChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE - y);
      suspendChangeEvents = false;
    }
    if (!whiteYChromaticitySlider.getValueIsAdjusting()) {
      fireStateChanged();
    }
  }

  private void whiteXChromaticitySlider_OnStateChanged(ChangeEvent e) {
    int x = whiteXChromaticitySlider.getValue();
    int y = whiteYChromaticitySlider.getValue();
    if (x + y > MAX_CHROMATICITY_SLIDER_VALUE) {
      suspendChangeEvents = true;
      whiteYChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE - x);
      suspendChangeEvents = false;
    }
    if (!whiteXChromaticitySlider.getValueIsAdjusting()) {
      fireStateChanged();
    }
  }

  private void autoCheckBox_OnActionPerformed(ActionEvent e) {
    boolean custom = !autoCheckBox.isSelected();
    whiteLuminanceSlider.setEnabled(custom);
    whiteXChromaticitySlider.setEnabled(custom);
    whiteYChromaticitySlider.setEnabled(custom);
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
    CIExyY white;

    if (autoCheckBox.isSelected()) {
      double Yavg = 0.0;
      double Ymax = 0.0;
      int n = 0;
      for (CIEXYZ sample : samples) {
        if (sample != null) {
          double Y = Math.abs(sample.Y());
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
      white = new CIExyY(1.0 / 3.0, 1.0 / 3.0, Yavg / Ymid);

      double ySliderValue = Math.log(white.Y()) / Math.log(2.0);

      suspendChangeEvents = true;
      whiteLuminanceSlider.setValue(ySliderValue);
      whiteXChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE / 3);
      whiteYChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE / 3);
      suspendChangeEvents = false;
    } else {
      white = new CIExyY(
          ((double) whiteXChromaticitySlider.getValue()) / (double) MAX_CHROMATICITY_SLIDER_VALUE,
          ((double) whiteYChromaticitySlider.getValue()) / (double) MAX_CHROMATICITY_SLIDER_VALUE,
          Math.pow(2.0, whiteLuminanceSlider.getValue()));
    }

    return new LinearToneMapper(white.toXYZ());
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JLinearToneMapperPanel factory = new JLinearToneMapperPanel();
    frame.add(factory);
    frame.pack();
    frame.setVisible(true);

  }

}
