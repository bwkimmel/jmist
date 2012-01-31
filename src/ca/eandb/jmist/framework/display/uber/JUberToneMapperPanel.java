/**
 * 
 */
package ca.eandb.jmist.framework.display.uber;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
public final class JUberToneMapperPanel extends JToneMapperPanel {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -588386788937985315L;

	private static final String[] TONE_MAPPER_NAMES = { "None", "Linear", "Reinhard" };
	
	private final JToneMapperPanel[] toneMapperPanel = {
		new JIdentityToneMapperPanel(),
		new JLinearToneMapperPanel(),
		new JReinhardToneMapperPanel()
	};
	
	private final JComboBox toneMapperComboBox;
	private final JPanel settingsContainerPanel;
	private final CardLayout settingsContainerLayout;
	
	/**
	 * 
	 */
	public JUberToneMapperPanel() {
		toneMapperComboBox = new JComboBox(TONE_MAPPER_NAMES);
		toneMapperComboBox.setSelectedIndex(0);
		
		settingsContainerLayout = new CardLayout();
		settingsContainerPanel = new JPanel(settingsContainerLayout);
		
		ChangeListener toneMapperPanelChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				toneMapperPanel_OnStateChanged(e);
			}
		};
		
		for (int i = 0; i < TONE_MAPPER_NAMES.length; i++) {
			toneMapperPanel[i].addChangeListener(toneMapperPanelChangeListener);
			settingsContainerPanel.add(toneMapperPanel[i], TONE_MAPPER_NAMES[i]);
		}
		
		toneMapperComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				toneMapperComboBox_OnItemStateChanged(e);
			}
		});
		
		setLayout(new BorderLayout());
		add(toneMapperComboBox, BorderLayout.NORTH);
		add(settingsContainerPanel, BorderLayout.CENTER);
	}
	
	private void toneMapperPanel_OnStateChanged(ChangeEvent e) {
		int index = toneMapperComboBox.getSelectedIndex();
		if (e.getSource() == toneMapperPanel[index]) {
			fireStateChanged();
		}
	}

	private void toneMapperComboBox_OnItemStateChanged(ItemEvent e) {
		String name = (String) e.getItem();
		settingsContainerLayout.show(settingsContainerPanel, name);
		fireStateChanged();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.tone.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
	@Override
	public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
		int index = toneMapperComboBox.getSelectedIndex();
		JToneMapperPanel settingsPanel = toneMapperPanel[index];
		return settingsPanel.createToneMapper(samples);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JUberToneMapperPanel factory = new JUberToneMapperPanel();
		frame.add(factory);
		frame.pack();
		frame.setVisible(true);
	}

}
