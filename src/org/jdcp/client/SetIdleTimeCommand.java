/**
 *
 */
package org.jdcp.client;

import java.util.List;

/**
 * @author Erin
 *
 */
public class SetIdleTimeCommand implements Command {

	/* (non-Javadoc)
	 * @see org.jdcp.client.Command#run(java.util.List, org.jdcp.client.Configuration)
	 */
	public void run(List<String> args, Configuration conf) throws Exception {
		int seconds = Integer.parseInt(args.get(0));
		conf.getJobService().setIdleTime(seconds);
	}

}
