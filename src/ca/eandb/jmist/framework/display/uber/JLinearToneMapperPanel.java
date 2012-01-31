/**
 * 
 */
package ca.eandb.jmist.framework.display.uber;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.tone.LinearToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.math.MathUtil;

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
	private static final int STEPS_PER_STOP = 100;
	private static final int MIN_STEP = (int) Math.round(MIN_STOP * (double) STEPS_PER_STOP);
	private static final int MAX_STEP = (int) Math.round(MAX_STOP * (double) STEPS_PER_STOP);
	
	private final JCheckBox autoCheckBox;
	private final JSlider whiteLuminanceSlider;
	private final JSlider whiteXChromaticitySlider;
	private final JSlider whiteYChromaticitySlider;
	
	private boolean suspendChangeEvents = false;

	/**
	 * 
	 */
	public JLinearToneMapperPanel() {
		autoCheckBox = new JCheckBox("Automatic", true);
		whiteLuminanceSlider = new JSlider(MIN_STEP, MAX_STEP, 0);
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
		
		setLayout(new GridLayout(4,2));
		add(new JLabel(""));
		add(autoCheckBox);
		add(new JLabel("White Y"));
		add(whiteLuminanceSlider);
		add(new JLabel("White x"));
		add(whiteXChromaticitySlider);
		add(new JLabel("White y"));
		add(whiteYChromaticitySlider);
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
		fireStateChanged();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.display.uber.JToneMapperPanel#fireStateChanged()
	 */
	@Override
	protected void fireStateChanged() {
		if (!suspendChangeEvents) {
			super.fireStateChanged();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.tone.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
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
			
			int ySliderValue = MathUtil.clamp(
					(int) Math.round(((double) STEPS_PER_STOP)
							* Math.log(white.Y()) / Math.log(2.0)), MIN_STEP,
					MAX_STEP);
	
			suspendChangeEvents = true;
			whiteLuminanceSlider.setValue(ySliderValue);
			whiteXChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE / 3);
			whiteYChromaticitySlider.setValue(MAX_CHROMATICITY_SLIDER_VALUE / 3);
			suspendChangeEvents = false;
		} else {
			white = new CIExyY(
					((double) whiteXChromaticitySlider.getValue()) / (double) MAX_CHROMATICITY_SLIDER_VALUE,
					((double) whiteYChromaticitySlider.getValue()) / (double) MAX_CHROMATICITY_SLIDER_VALUE,
					Math.pow(2.0, ((double) whiteLuminanceSlider.getValue()) / (double) STEPS_PER_STOP));
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
