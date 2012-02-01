/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;

/**
 * @author brad
 *
 */
public final class JCompositeVisualizerPanel extends JColorVisualizerPanel {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -232961900921714759L;
	
	private final List<JColorVisualizerPanel> settingsPanels = new ArrayList<JColorVisualizerPanel>();
	private final JComboBox visualizerComboBox;
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
	public JCompositeVisualizerPanel() {
		super(new BorderLayout());
		visualizerComboBox = new JComboBox();
		visualizerComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toneMapperComboBox_OnActionPerformed(e);
			}
		});
		
		settingsContainerLayout = new CardLayout();
		settingsContainerPanel = new JPanel(settingsContainerLayout);
		
		add(visualizerComboBox, BorderLayout.NORTH);
		add(settingsContainerPanel, BorderLayout.CENTER);
	}
	
	public JCompositeVisualizerPanel addChild(String name, JColorVisualizerPanel panel) {
		visualizerComboBox.addItem(name);
		settingsContainerPanel.add(panel, name);
		settingsPanels.add(panel);
		panel.addChangeListener(settingsPanelChangeListener);
		return this;
	}
	
	private void settingsPanel_OnStateChanged(ChangeEvent e) {
		int index = visualizerComboBox.getSelectedIndex();
		if (e.getSource() == settingsPanels.get(index)) {
			fireStateChanged();
		}
	}

	private void toneMapperComboBox_OnActionPerformed(ActionEvent e) {
		String name = (String) visualizerComboBox.getSelectedItem();
		settingsContainerLayout.show(settingsContainerPanel, name);
		fireStateChanged();
	}

	private JColorVisualizerPanel getSelectedVisualizer() {
		int index = visualizerComboBox.getSelectedIndex();
		JColorVisualizerPanel settingsPanel = settingsPanels.get(index);
		return settingsPanel;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.JColorVisualizerPanel#analyze(java.lang.Iterable)
	 */
	@Override
	public synchronized boolean analyze(Iterable<Color> samples) {
		JColorVisualizerPanel settingsPanel = getSelectedVisualizer();
		return settingsPanel.analyze(samples);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public RGB visualize(Color color) {
		JColorVisualizerPanel settingsPanel = getSelectedVisualizer();
		return settingsPanel.visualize(color);
	}

}
