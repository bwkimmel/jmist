/**
 * 
 */
package ca.eandb.jmist.framework.display.visualizer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		super(new BorderLayout());
		this.inner = inner;
		this.inner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				inner_OnStateChanged(e);
			}
		});
		
		JPanel settingsPanel = new JPanel(new GridLayout(1, 4));
		
		highlightNegativeCheckBox = new JCheckBox("Negative");
		highlightNegativeCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highlightCheckBox_OnActionPerformed(e);
			}
		});
		
		highlightOverCheckBox = new JCheckBox("Overexposed");
		highlightOverCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highlightCheckBox_OnActionPerformed(e);
			}
		});

		highlightUnderCheckBox = new JCheckBox("Underexposed");
		highlightUnderCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highlightCheckBox_OnActionPerformed(e);
			}
		});

		settingsPanel.add(new JLabel("Highlight: "));
		settingsPanel.add(highlightOverCheckBox);
		settingsPanel.add(highlightUnderCheckBox);
		settingsPanel.add(highlightNegativeCheckBox);
		
		add(settingsPanel, BorderLayout.NORTH);
		add(inner, BorderLayout.CENTER);
	}
	
	protected void highlightCheckBox_OnActionPerformed(ActionEvent e) {
		fireStateChanged();
	}

	private void inner_OnStateChanged(ChangeEvent e) {
		fireStateChanged();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.JColorVisualizerPanel#analyze(java.lang.Iterable)
	 */
	@Override
	public boolean analyze(Iterable<Color> samples) {
		return inner.analyze(samples);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.visualizer.ColorVisualizer#visualize(ca.eandb.jmist.framework.color.Color)
	 */
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
