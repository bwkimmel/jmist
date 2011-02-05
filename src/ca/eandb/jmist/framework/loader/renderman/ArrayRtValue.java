/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.List;

/**
 * @author Brad
 *
 */
final class ArrayRtValue extends AbstractRtValue {
	
	private final List<RtValue> values;
	
	public ArrayRtValue(List<RtValue> values) {
		this.values = values;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		double[] array = new double[values.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = values.get(i).realValue();
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#intArrayValue()
	 */
	@Override
	public int[] intArrayValue() {
		int[] array = new int[values.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = values.get(i).intValue();
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringArrayValue()
	 */
	@Override
	public String[] stringArrayValue() {
		String[] array = new String[values.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = values.get(i).stringValue();
		}
		return array;
	}

}
