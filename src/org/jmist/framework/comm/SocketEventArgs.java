/**
 * 
 */
package org.jmist.framework.comm;

import java.net.Socket;

import org.jmist.framework.event.EventArgs;

/**
 * @author bkimmel
 *
 */
public final class SocketEventArgs extends EventArgs {
	
	public SocketEventArgs(Socket socket) {
		this.socket = socket;
	}
	
	public final Socket socket;

}
