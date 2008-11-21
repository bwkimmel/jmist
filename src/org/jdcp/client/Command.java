/**
 *
 */
package org.jdcp.client;

import java.util.List;

/**
 * @author brad
 *
 */
public interface Command {

	void run(List<String> args, Configuration conf) throws Exception;

}
