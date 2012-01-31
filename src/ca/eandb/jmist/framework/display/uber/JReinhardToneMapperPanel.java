/**
 * 
 */
package ca.eandb.jmist.framework.display.uber;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.tone.ReinhardToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author brad
 *
 */
public final class JReinhardToneMapperPanel extends JToneMapperPanel {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 3820623449350636976L;

	private static final double DELTA = 1.0;
	
	private static final double MIN_STOP = -10.0;
	private static final double MAX_STOP = 10.0;
	private static final int STEPS_PER_STOP = 100;
	private static final int MIN_STEP = (int) Math.round(MIN_STOP * (double) STEPS_PER_STOP);
	private static final int MAX_STEP = (int) Math.round(MAX_STOP * (double) STEPS_PER_STOP);
	
	private final JCheckBox autoCheckBox;
	private final JSlider whiteSlider;
	private final JSlider scaleSlider;
	
	private boolean suspendChangeEvents = false;

	/**
	 * 
	 */
	public JReinhardToneMapperPanel() {
		autoCheckBox = new JCheckBox("Automatic", true);
		whiteSlider = new JSlider(MIN_STEP, MAX_STEP, 0);
		scaleSlider = new JSlider(MIN_STEP, MAX_STEP, 0);
		
		whiteSlider.setEnabled(false);
		scaleSlider.setEnabled(false);
		
		autoCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				autoCheckBox_OnStateChanged(e);
			}
		});
		
		whiteSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				whiteSlider_OnStateChanged(e);
			}
		});
		
		scaleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scaleSlider_OnStateChanged(e);
			}
		});
		
		setLayout(new GridLayout(3,2));
		add(new JLabel(""));
		add(autoCheckBox);
		add(new JLabel("White"));
		add(whiteSlider);
		add(new JLabel("Scale"));
		add(scaleSlider);
	}

	private void whiteSlider_OnStateChanged(ChangeEvent e) {
		fireStateChanged();
	}
	
	protected void scaleSlider_OnStateChanged(ChangeEvent e) {
		fireStateChanged();
	}

	private void autoCheckBox_OnStateChanged(ChangeEvent e) {
		boolean custom = !autoCheckBox.isSelected();
		whiteSlider.setEnabled(custom);
		scaleSlider.setEnabled(custom);
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
		double yWhite;
		double yScale;
		
		if (autoCheckBox.isSelected()) {
			double Yavg = 0.0;
			double Ymax = 0.0;
			int n = 0;
			for (CIEXYZ sample : samples) {
				if (sample != null) {
					double Y = sample.Y();
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
			yWhite = Ymax;
			yScale = Ymid / Yavg;
			
			int whiteSliderValue = MathUtil.clamp(
					(int) Math.round(((double) STEPS_PER_STOP)
							* Math.log(yWhite) / Math.log(2.0)), MIN_STEP,
					MAX_STEP);
			int scaleSliderValue = MathUtil.clamp(
					(int) Math.round(((double) STEPS_PER_STOP)
							* Math.log(yScale) / Math.log(2.0)), MIN_STEP,
					MAX_STEP);
			
			suspendChangeEvents = true;
			whiteSlider.setValue(whiteSliderValue);
			scaleSlider.setValue(scaleSliderValue);
			suspendChangeEvents = false;
		} else {
			yWhite = Math.pow(2.0, ((double) whiteSlider.getValue()) / (double) STEPS_PER_STOP);
			yScale = Math.pow(2.0, ((double) scaleSlider.getValue()) / (double) STEPS_PER_STOP);
		}
		
		return new ReinhardToneMapper(yScale, yWhite);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JReinhardToneMapperPanel factory = new JReinhardToneMapperPanel();
		frame.add(factory);
		frame.pack();
		frame.setVisible(true);

	}

}
