/**
 *
 */
package org.jmist.packages;

import java.net.*;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.ISwitchboard;
import org.jmist.framework.comm.SocketServer;
import org.jmist.framework.comm.SocketEventArgs;
import org.jmist.framework.event.EventArgs;
import org.jmist.framework.event.IEventHandler;

/**
 * @author bkimmel
 *
 */
public final class SocketSwitchboard implements ISwitchboard {

	public SocketSwitchboard() {
		this.initialize();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISwitchboard#listen(long)
	 */
	@Override
	public ICommunicator listen(long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISwitchboard#listen()
	 */
	@Override
	public ICommunicator listen() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IDialer#dial(java.lang.String)
	 */
	@Override
	public ICommunicator dial(String to) {
		// TODO Auto-generated method stub
		return null;
	}

	private void serverOnInitialize() {
		InetSocketAddress isa = new InetSocketAddress(SERVER_PORT);
		this.server.addListener(isa);
	}

	private void serverOnShutdown() {
		// nothing to do.
	}

	private void serverOnConnectionOpened(SocketEventArgs args) {

	}

	private void serverOnConnectionClosed(SocketEventArgs args) {

	}

	private void serverOnReceive(SocketEventArgs args) {

	}

	private void initialize() {

		// Wire up event handlers.
		this.server.onInitialize.subscribe(new IEventHandler<EventArgs>() {
			public void notify(Object sender, EventArgs args) {
				serverOnInitialize();
			}
		});

		this.server.onShutdown.subscribe(new IEventHandler<EventArgs>() {
			public void notify(Object sender, EventArgs args) {
				serverOnShutdown();
			}
		});

		this.server.onConnectionOpened.subscribe(new IEventHandler<SocketEventArgs>() {
			public void notify(Object sender, SocketEventArgs args) {
				serverOnConnectionOpened(args);
			}
		});

		this.server.onConnectionClosed.subscribe(new IEventHandler<SocketEventArgs>() {
			public void notify(Object sender, SocketEventArgs args) {
				serverOnConnectionClosed(args);
			}
		});

		this.server.onReceive.subscribe(new IEventHandler<SocketEventArgs>() {
			public void notify(Object sender, SocketEventArgs args) {
				serverOnReceive(args);
			}
		});

	}

	private final SocketServer server = new SocketServer();

	private static final int SERVER_PORT = 6478;

}
