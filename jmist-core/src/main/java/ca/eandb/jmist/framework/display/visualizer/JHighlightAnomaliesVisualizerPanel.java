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
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;

/**
 * A <code>ColorVisualizer</code> that highlights anomalous pixels (overexposed,
 * underexposed, or pixels with negative channels).
 * @author Brad Kimmel
 */
public final class JHighlightAnomaliesVisualizerPanel extends
    JColorVisualizerPanel {

  /** Serialization version ID. */
  private static final long serialVersionUID = -4644160032022137365L;

  private static final double EPSILON = 1.0 / 256.0;

  private final JColorVisualizerPanel inner;

  private final JCheckBox highlightNegativeCheckBox;

  private final JCheckBox highlightOverCheckBox;

  private final JCheckBox highlightUnderCheckBox;

  private RGB negative = RGB.RED;

  private RGB overexposed = RGB.BLUE;

  private RGB underexposed = RGB.GREEN;

  public JHighlightAnomaliesVisualizerPanel(JColorVisualizerPanel inner) {
    super(new GridBagLayout());
    this.inner = inner;
    this.inner.addChangeListener(this::inner_OnStateChanged);

    JLabel label;
    GridBagConstraints c;

    highlightNegativeCheckBox = new JCheckBox("Negative");
    highlightNegativeCheckBox.addActionListener(this::highlightCheckBox_OnActionPerformed);

    highlightOverCheckBox = new JCheckBox("Overexposed");
    highlightOverCheckBox.addActionListener(this::highlightCheckBox_OnActionPerformed);

    highlightUnderCheckBox = new JCheckBox("Underexposed");
    highlightUnderCheckBox.addActionListener(this::highlightCheckBox_OnActionPerformed);

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    label = new JLabel("Highlight");
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 1;
    c.anchor = GridBagConstraints.LINE_START;
    highlightOverCheckBox.setPreferredSize(new Dimension(150, 25));
    add(highlightOverCheckBox, c);

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 2;
    c.anchor = GridBagConstraints.LINE_START;
    highlightUnderCheckBox.setPreferredSize(new Dimension(150, 25));
    add(highlightUnderCheckBox, c);

    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 3;
    c.weightx = 1.0;
    c.anchor = GridBagConstraints.LINE_START;
    highlightNegativeCheckBox.setPreferredSize(new Dimension(150, 25));
    add(highlightNegativeCheckBox, c);

    c = new GridBagConstraints();
    c.gridy = 1;
    c.gridx = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    add(inner, c);
  }

  protected void highlightCheckBox_OnActionPerformed(ActionEvent e) {
    fireStateChanged();
  }

  private void inner_OnStateChanged(ChangeEvent e) {
    fireStateChanged();
  }

  @Override
  public boolean analyze(Iterable<Color> samples) {
    return inner.analyze(samples);
  }

  @Override
  public RGB visualize(Color color) {
    RGB rgb = inner.visualize(color);
    if (highlightNegativeCheckBox.isSelected()
        && (rgb.r() < 0.0 || rgb.g() < 0.0 || rgb.b() < 0.0)) {
      return negative;
    } else if (highlightUnderCheckBox.isSelected()
        && (rgb.r() < EPSILON && rgb.g() < EPSILON && rgb.b() < EPSILON)) {
      return underexposed;
    } else if (highlightOverCheckBox.isSelected()
        && (rgb.r() >= 1.0 - EPSILON && rgb.g() >= 1.0 - EPSILON && rgb
            .b() >= 1.0 - EPSILON)) {
      return overexposed;
    }
    return rgb;
  }

}
