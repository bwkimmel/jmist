/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public class DoubleFieldOption<T> extends AbstractFieldOption<T> {

	/**
	 * @param fieldName
	 */
	public DoubleFieldOption(String fieldName) {
		super(fieldName);
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.AbstractFieldOption#getOptionValue(java.util.Queue)
	 */
	@Override
	protected Object getOptionValue(Queue<String> argq) {
		return Double.parseDouble(argq.remove());
	}

}
