/**
 * 
 */
package ca.eandb.jmist.framework.display.uber;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
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
		
		setLayout(new BorderLayout());
		add(toneMapperComboBox, BorderLayout.NORTH);
		add(settingsContainerPanel, BorderLayout.CENTER);
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
