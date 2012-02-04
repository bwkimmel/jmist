/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.RGB;

/**
 * @author brad
 *
 */
public final class JChannelVisualizerPanel extends JColorVisualizerPanel {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 5281037549254812907L;

	private final JComboBox channelComboBox;
	
	private ColorVisualizer visualizer = null;
	
	public JChannelVisualizerPanel(ColorModel cm) {
		super(new GridBagLayout());
		
		channelComboBox = new JComboBox();
		for (int i = 0, n = cm.getNumChannels(); i < n; i++) {
			String title = String.format("%d: %s", i, cm.getChannelName(i));
			channelComboBox.addItem(title);
		}
		
		channelComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				channelComboBox_OnActionPerformed(e);
			}
		});
		
		visualizer = new AutomaticLinearChannelVisualizer(0, false);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_START;
		JLabel label = new JLabel("Channel");
		label.setPreferredSize(new Dimension(100, 25));
		add(label, c);
		
		c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(channelComboBox, c);
		
		c = new GridBagConstraints();
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 0));
		add(panel, c);
	}

	private void channelComboBox_OnActionPerformed(ActionEvent e) {
		visualizer = new AutomaticLinearChannelVisualizer(channelComboBox.getSelectedIndex(), false);
		fireStateChanged();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.JColorVisualizerPanel#analyze(java.lang.Iterable)
	 */
	@Override
	public boolean analyze(Iterable<Color> samples) {
		return visualizer.analyze(samples);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public RGB visualize(Color color) {
		return visualizer.visualize(color);
	}

}
