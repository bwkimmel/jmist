/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.selfip.bkimmel.util.ArrayQueue;

/**
 * @author brad
 *
 */
public final class ArgumentProcessor<T> {

	private final Map<Character, Command<? super T>> handlersByShortKey = new HashMap<Character, Command<? super T>>();
	private final Map<String, Command<? super T>> handlersByKey = new HashMap<String, Command<? super T>>();
	private final Map<String, Command<? super T>> commands = new HashMap<String, Command<? super T>>();

	private Command<? super T> defaultCommand = null;

	public void addOption(String key, char shortKey,
			Command<? super T> handler) {
		handlersByShortKey.put(shortKey, handler);
		handlersByKey.put(key, handler);
	}

	public void addCommand(String key, Command<? super T> handler) {
		commands.put(key, handler);
	}

	public void setDefaultCommand(Command<? super T> command) {
		defaultCommand = command;
	}

	public void process(String[] args, T state) {
		process(new ArrayQueue<String>(args), state);
	}

	public void process(Queue<String> argq, T state) {
		while (!argq.isEmpty()) {
			String nextArg = argq.peek();
			if (nextArg.startsWith("--")) {
				argq.remove();
				String key = nextArg.substring(2);
				Command<? super T> command = handlersByKey.get(key);
				if (command != null) {
					command.process(argq, state);
				}
			} else if (nextArg.startsWith("-")) {
				argq.remove();
				for (int i = 1; i < nextArg.length(); i++) {
					char key = nextArg.charAt(i);
					Command<? super T> command = handlersByShortKey.get(key);
					if (command != null) {
						command.process(argq, state);
					}
				}
			} else {
				Command<? super T> command = commands.get(nextArg);
				if (command != null) {
					argq.remove();
					command.process(argq, state);
					return;
				}
				break;
			}
		}
		if (defaultCommand != null) {
			defaultCommand.process(argq, state);
		}
	}

}
