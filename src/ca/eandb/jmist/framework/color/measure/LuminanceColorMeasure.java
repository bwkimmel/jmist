/**
 * 
 */
package ca.eandb.jmist.framework.color.measure;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorMeasure;

/**
 * @author Brad
 *
 */
public final class LuminanceColorMeasure implements ColorMeasure {

	/** Serialization version ID. */
	private static final long serialVersionUID = 4367491421730020935L;
	
	private static final ColorMeasure INSTANCE = new LuminanceColorMeasure();
	
	private LuminanceColorMeasure() {
		/* nothing to do. */
	}
	
	public static ColorMeasure getInstance() {
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorMeasure#evaluate(ca.eandb.jmist.framework.color.Color)
	 */
	public double evaluate(Color c) {
		return c != null ? c.luminance() : 0.0;
	}

}
