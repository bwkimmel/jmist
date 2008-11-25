/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author Erin
 *
 */
public final class StringFieldOption<T> extends AbstractFieldOption<T> {

	public StringFieldOption(String fieldName) {
		super(fieldName);
	}

	@Override
	protected Object getOptionValue(Queue<String> argq) {
		return argq.remove();
	}

}
