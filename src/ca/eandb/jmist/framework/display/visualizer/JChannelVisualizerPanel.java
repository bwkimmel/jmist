/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

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
		super(new GridLayout(1, 2));
		
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
		
		add(new JLabel("Channel"));
		add(channelComboBox);
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
