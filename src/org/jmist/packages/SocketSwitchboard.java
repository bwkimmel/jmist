/**
 *
 */
package org.jmist.packages;

import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
		SocketCommunicator comm = new SocketCommunicator(args.socket);
		this.readyq.add(comm);
		this.communicators.put(args.socket, comm);
	}

	private void serverOnConnectionClosed(SocketEventArgs args) {
		this.communicators.remove(args.socket);
	}

	private void serverOnReadyReceive(SocketEventArgs args) {

		SocketCommunicator comm = this.communicators.get(args.socket);
		assert(comm != null);

		if (comm.peek() != null)
			this.readyq.add(comm);

	}

	private void serverOnReadySend(SocketEventArgs args) {

		SocketCommunicator comm = this.communicators.get(args.socket);
		assert(comm != null);

		try {
			comm.writeSocket();
		} catch (java.io.IOException e) {
			// TODO do something other than just print out the exception.
			System.err.println(e);
		}

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

		this.server.onReadyReceive.subscribe(new IEventHandler<SocketEventArgs>() {
			public void notify(Object sender, SocketEventArgs args) {
				serverOnReadyReceive(args);
			}
		});

		this.server.onReadySend.subscribe(new IEventHandler<SocketEventArgs>() {
			public void notify(Object sender, SocketEventArgs args) {
				serverOnReadySend(args);
			}
		});

	}

	private final SocketServer server = new SocketServer();

	private final Map<Socket, SocketCommunicator> communicators = new HashMap<Socket, SocketCommunicator>();

	private final Queue<SocketCommunicator> readyq = new LinkedList<SocketCommunicator>();

	private static final int SERVER_PORT = 6478;

}
