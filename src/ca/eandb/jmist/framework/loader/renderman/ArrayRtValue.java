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
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realValue()
	 */
	@Override
	public double realValue() {
		return values.size() == 1 ? values.get(0).realValue() : super.realValue();
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
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#intValue()
	 */
	@Override
	public int intValue() {
		return values.size() == 1 ? values.get(0).intValue() : super.intValue();
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
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringValue()
	 */
	@Override
	public String stringValue() {
		return values.size() == 1 ? values.get(0).stringValue() : super.stringValue();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");
		for (int i = 0, n = values.size(); i < n; i++) {
			if (i > 0) {
				s.append(" ");
			}
			s.append(values.get(i));
		}
		s.append("]");
		return s.toString();
	}

}
