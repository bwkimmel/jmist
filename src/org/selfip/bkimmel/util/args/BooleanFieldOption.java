/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public class BooleanFieldOption<T> extends AbstractFieldOption<T> {

	public BooleanFieldOption(String fieldName) {
		super(fieldName);
	}

	@Override
	protected Object getOptionValue(Queue<String> argq) {
		return true;
	}

}
