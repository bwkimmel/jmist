/**
 *
 */
package org.jdcp.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author brad
 *
 */
public final class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * Commands:
		 *
		 * verify <classpath>
		 *
		 */

		Configuration conf = new Configuration();

		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				if (args[i].length() > 1) {
					if (args[i].charAt(1) == '-') {
						String arg = args[i].substring(2);
						setOption(conf, arg);
					} else {
						for (int j = 1; j < args[i].length(); j++) {
							setOption(conf, args[i].charAt(j));
						}
					}
				}
			} else {
				Command command = getCommand(args[i]);
				if (command == null) {
					System.err.print("Unrecognized command: ");
					System.err.println(args[i]);
				}

				List<String> cmdArgs = new ArrayList<String>(args.length - (i + 1));
				for (int j = i + 1; j < args.length; j++) {
					cmdArgs.add(args[j]);
				}

				try {
					command.run(cmdArgs, conf);
				} catch (Exception e) {
					System.err.println("An error occurred while processing the command:");
					e.printStackTrace();
				}
				break;
			}
		}

	}

	private static void setOption(Configuration conf, String arg) {
		if (arg.equals("quiet")) {
			conf.verbose = false;
		} else if (arg.equals("verbose")) {
			conf.verbose = true;
		}
	}

	private static void setOption(Configuration conf, char arg) {
		if (arg == 'q') {
			conf.verbose = false;
		} else if (arg == 'V') {
			conf.verbose = true;
		}
	}

	private static Command getCommand(String commandName) {
		if (commandName.equals("verify")) {
			return new VerifyCommand();
		} else if (commandName.equals("sync")) {
			return new SynchronizeCommand();
		}
		return null;
	}

}
