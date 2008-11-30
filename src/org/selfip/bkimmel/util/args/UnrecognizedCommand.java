/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public final class UnrecognizedCommand implements Command<Object> {

	private static UnrecognizedCommand instance;

	/**
	 *
	 */
	private UnrecognizedCommand() {
		/* nothing to do */
	}

	public static Command<Object> getInstance() {
		if (instance == null) {
			instance = new UnrecognizedCommand();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.Command#process(java.util.Queue, java.lang.Object)
	 */
	public void process(Queue<String> argq, Object state) {
		String command = argq.peek();
		if (command != null) {
			System.err.print("Unrecognzied command: ");
			System.err.println(command);
		}
	}

}
