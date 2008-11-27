/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public class IntegerFieldOption<T> extends AbstractFieldOption<T> {

	/**
	 * @param fieldName
	 */
	public IntegerFieldOption(String fieldName) {
		super(fieldName);
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.AbstractFieldOption#getOptionValue(java.util.Queue)
	 */
	@Override
	protected Object getOptionValue(Queue<String> argq) {
		return Integer.parseInt(argq.remove());
	}

}
