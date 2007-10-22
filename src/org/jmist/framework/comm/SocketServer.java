/**
 *
 */
package org.jmist.framework.comm;

import java.io.IOException;
import java.net.*;
import java.nio.channels.*;

import org.jmist.framework.event.*;

/**
 * @author bkimmel
 *
 */
public class SocketServer {

	public final IEvent<EventArgs>			onInitialize			= new Event<EventArgs>();
	public final IEvent<EventArgs>			onShutdown				= new Event<EventArgs>();
	public final IEvent<SocketEventArgs>	onConnectionOpened		= new Event<SocketEventArgs>();
	public final IEvent<SocketEventArgs>	onConnectionClosed		= new Event<SocketEventArgs>();
	public final IEvent<SocketEventArgs>	onReadyReceive			= new Event<SocketEventArgs>();
	public final IEvent<SocketEventArgs>	onReadySend				= new Event<SocketEventArgs>();

	public SocketServer() {
		this.auto = true;
	}

	public SocketServer(boolean auto) {
		this.auto = auto;
	}

	public final boolean auto() {
		return this.auto;
	}

	public final boolean ready() {
		return (this.selector != null);
	}

	public final void start() throws IOException {

		this.selector = Selector.open();
		this.raiseInitialize();

		if (this.auto) {
			while (this.step());
		}

	}

	public final void stop() {
		this.raiseShutdown();
	}

	public final void connect(InetSocketAddress address) {

	}

	public final void addListener(InetSocketAddress address) {

	}

	public final boolean step() {
		return this.step(0);
	}

	public final boolean step(long timeout) {
		return true;
	}

	private void raiseInitialize() {
		this.onInitialize();
		((Event<EventArgs>) this.onInitialize).raise(this, new EventArgs());
	}

	protected void onInitialize() {
		// no default behavior.
	}

	private void raiseShutdown() {
		this.onShutdown();
		((Event<EventArgs>) this.onShutdown).raise(this, new EventArgs());
	}

	protected void onShutdown() {
		// no default behavior.
	}

	private void raiseConnectionOpened(Socket socket) {
		this.onConnectionOpened(socket);
		((Event<SocketEventArgs>) this.onConnectionOpened).raise(this, new SocketEventArgs(socket));
	}

	protected void onConnectionOpened(Socket socket) {
		// no default behavior.
	}

	private void raiseConnectionClosed(Socket socket) {
		this.onConnectionOpened(socket);
		((Event<SocketEventArgs>) this.onConnectionClosed).raise(this, new SocketEventArgs(socket));
	}

	protected void onConnectionClosed(Socket socket) {
		// no default behavior.
	}

	private void raiseReadyReceive(Socket socket) {
		this.onConnectionOpened(socket);
		((Event<SocketEventArgs>) this.onReadyReceive).raise(this, new SocketEventArgs(socket));
	}

	protected void onReadyReceive(Socket socket) {
		// no default behavior.
	}

	private void raiseReadySend(Socket socket) {
		this.onConnectionOpened(socket);
		((Event<SocketEventArgs>) this.onReadySend).raise(this, new SocketEventArgs(socket));
	}

	protected void onReadySend(Socket socket) {
		// no default behavior.
	}

	private final boolean auto;

	private Selector selector = null;

}
