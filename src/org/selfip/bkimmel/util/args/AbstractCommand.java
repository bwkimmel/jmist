/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public abstract class AbstractCommand<T> implements Command<T> {

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.Command#process(java.util.Queue, java.lang.Object)
	 */
	public final void process(Queue<String> argq, T state) {
		String[] args = argq.toArray(new String[argq.size()]);
		argq.clear();
		run(args, state);
	}

	/**
	 *
	 * @param args
	 * @param state
	 */
	protected abstract void run(String[] args, T state);

}
