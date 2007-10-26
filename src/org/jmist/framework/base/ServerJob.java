/**
 *
 */
package org.jmist.framework.base;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IJob;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.ISwitchboard;

/**
 * @author bkimmel
 *
 */
public abstract class ServerJob implements IJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJob#go(org.jmist.framework.IProgressMonitor)
	 */
	@Override
	public final void go(IProgressMonitor monitor) {

		this.onInitialize(switchboard);

		while (true) {

			ICommunicator comm = switchboard.listen();

			if (comm.peek() != null) {

				IInboundMessage msg = comm.receive();

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
	protected abstract boolean onMessageReceived(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor);

	protected void onInitialize(ISwitchboard switchboard) {
		// no default behavior
	}

	protected void onShutdown() {
		// no default behavior.
	}

	private ISwitchboard switchboard;
	private IProgressMonitor monitor;

}
