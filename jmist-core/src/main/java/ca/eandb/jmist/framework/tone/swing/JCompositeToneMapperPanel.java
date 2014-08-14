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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.tone.ToneMapper;

/**
 * @author brad
 *
 */
public final class JCompositeToneMapperPanel extends JToneMapperPanel {
  
  /** Serialization version ID. */
  private static final long serialVersionUID = 3141927300066901460L;
  
  private static final String DEFAULT_COMBOBOX_LABEL = "Operator";
  
  private final List<JToneMapperPanel> settingsPanels = new ArrayList<JToneMapperPanel>();
  private final JComboBox toneMapperComboBox;
  private final JPanel settingsContainerPanel;
  private final CardLayout settingsContainerLayout;
  
  private final ChangeListener settingsPanelChangeListener = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      settingsPanel_OnStateChanged(e);
    }
  };
  
  /**
   * 
   */
  public JCompositeToneMapperPanel() {
    toneMapperComboBox = new JComboBox();
    toneMapperComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        toneMapperComboBox_OnActionPerformed(e);
      }
    });
    
    settingsContainerLayout = new CardLayout();
    settingsContainerPanel = new JPanel(settingsContainerLayout);
    
    setLayout(new GridBagLayout());
    
    GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 0;
    c.anchor = GridBagConstraints.LINE_START;
    
    JLabel label = new JLabel(DEFAULT_COMBOBOX_LABEL);
    label.setPreferredSize(new Dimension(100, 25));
    add(label, c);
    
    c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 1;
    c.weightx = 1.0;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(toneMapperComboBox, c);
    
    c = new GridBagConstraints();
    c.gridy = 1;
    c.gridx = 0;
    c.gridwidth = 2;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    add(settingsContainerPanel, c);
  }
  
  public JCompositeToneMapperPanel addChild(String name, JToneMapperPanel panel) {
    toneMapperComboBox.addItem(name);
    settingsContainerPanel.add(panel, name);
    settingsPanels.add(panel);
    panel.addChangeListener(settingsPanelChangeListener);
    return this;
  }
  
  private void settingsPanel_OnStateChanged(ChangeEvent e) {
    int index = toneMapperComboBox.getSelectedIndex();
    if (e.getSource() == settingsPanels.get(index)) {
      fireStateChanged();
    }
  }

  private void toneMapperComboBox_OnActionPerformed(ActionEvent e) {
    String name = (String) toneMapperComboBox.getSelectedItem();
    settingsContainerLayout.show(settingsContainerPanel, name);
    fireStateChanged();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.tone.ToneMapperFactory#createToneMapper(java.lang.Iterable)
   */
  @Override
  public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
    int index = toneMapperComboBox.getSelectedIndex();
    JToneMapperPanel settingsPanel = settingsPanels.get(index);
    return settingsPanel.createToneMapper(samples);
  }
  
  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JCompositeToneMapperPanel factory = new JCompositeToneMapperPanel()
      .addChild("None", new JIdentityToneMapperPanel())
      .addChild("Linear", new JLinearToneMapperPanel())
      .addChild("Reinhard", new JReinhardToneMapperPanel())
      ;
    factory.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        System.out.println("fireStateChanged()");
      }
    });
    frame.add(factory);
    frame.pack();
    frame.setVisible(true);
  }

}
