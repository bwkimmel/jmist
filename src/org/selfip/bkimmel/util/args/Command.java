/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.Queue;

/**
 * @author brad
 *
 */
public interface Command<T> {

	void process(Queue<String> argq, T state);

}
