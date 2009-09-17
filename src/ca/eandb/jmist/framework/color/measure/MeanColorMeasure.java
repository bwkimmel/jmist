/**
 * 
 */
package ca.eandb.jmist.framework.color.measure;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorMeasure;
import ca.eandb.jmist.framework.color.ColorUtil;

/**
 * @author Brad
 *
 */
public final class MeanColorMeasure implements ColorMeasure {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 7098237997827522394L;
	
	private static final ColorMeasure INSTANCE = new MeanColorMeasure();
	
	private MeanColorMeasure() {
		/* nothing to do. */
	}
	
	public static ColorMeasure getInstance() {
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorMeasure#evaluate(ca.eandb.jmist.framework.color.Color)
	 */
	@Override
	public double evaluate(Color c) {
		return ColorUtil.getMeanChannelValue(c);
	}

}
