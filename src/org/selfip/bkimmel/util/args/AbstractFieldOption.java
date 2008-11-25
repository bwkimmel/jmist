/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.lang.reflect.Field;
import java.util.Queue;

import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public abstract class AbstractFieldOption<T> implements Command<T> {

	private final String fieldName;

	public AbstractFieldOption(String fieldName) {
		this.fieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.Command#process(java.util.Queue, java.lang.Object)
	 */
	public final void process(Queue<String> argq, T state) {
		try {
			Field field = state.getClass().getField(fieldName);
			Object value = getOptionValue(argq);
			field.set(state, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnexpectedException(e);
		}
	}

	protected abstract Object getOptionValue(Queue<String> args);

}
