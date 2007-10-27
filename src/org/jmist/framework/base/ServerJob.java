/**
 *
 */
package org.jmist.framework.base;

import org.jmist.framework.Communicator;
import org.jmist.framework.InboundMessage;
import org.jmist.framework.Job;
import org.jmist.framework.ProgressMonitor;
import org.jmist.framework.Switchboard;

/**
 * @author bkimmel
 *
 */
public abstract class ServerJob implements Job {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	@Override
	public final void go(ProgressMonitor monitor) {

		this.onInitialize(switchboard);

		while (true) {

			Communicator comm = switchboard.listen();

			if (comm.peek() != null) {

				InboundMessage msg = comm.receive();

				if (!this.onMessageReceived(msg, comm, this.monitor)) {

					System.err.printf("Unrecognized message (tag=%d).\n", msg.tag());

				}

			}

		}

	}

	/**
	 * Handles an incoming message.
	 * @param msg The incoming message.
	 * @param comm The communicator on which the message was received.
	 * @param monitor The progress monitor to report the progress of the
	 * 		message handling to.
	 * @return A value that indicates whether the message was handled.
	 */
	protected abstract boolean onMessageReceived(InboundMessage msg, Communicator comm, ProgressMonitor monitor);

	protected void onInitialize(Switchboard switchboard) {
		// no default behavior
	}

	protected void onShutdown() {
		// no default behavior.
	}

	private Switchboard switchboard;
	private ProgressMonitor monitor;

}
