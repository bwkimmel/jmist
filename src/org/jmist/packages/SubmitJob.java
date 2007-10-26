/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IDialer;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IJob;
import org.jmist.framework.IOutboundMessage;
import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.IProgressMonitor;

/**
 * A job that submits a parallelizable job to a <code>MasterJob</code>.
 * @author bkimmel
 */
public final class SubmitJob implements IJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJob#go(org.jmist.framework.IProgressMonitor)
	 */
	@Override
	public void go(IProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged(String.format("Connecting to '%s'...", this.masterAddress));

			ICommunicator comm = this.dialer.dial(this.masterAddress);

			monitor.notifyStatusChanged("Connected");

			IOutboundMessage msg = comm.createOutboundMessage();
			ObjectOutputStream contents = new ObjectOutputStream(msg.contents());

			contents.writeInt(this.priority);
			contents.writeObject(this.job);
			contents.flush();

			monitor.notifyStatusChanged("Submitting job...");

			comm.send(msg);

			monitor.notifyStatusChanged("Submitted, waiting for confirmation...");

			IInboundMessage reply = comm.receive();
			DataInputStream replyContents = new DataInputStream(reply.contents());

			UUID jobId = new UUID(replyContents.readLong(), replyContents.readLong());

			monitor.notifyStatusChanged(String.format("Job received (ID=%s).\n", jobId.toString()));
			System.out.printf("Job submitted (%s).\n", jobId.toString());

			monitor.notifyComplete();

		} catch (IOException e) {

			System.err.println("Failed to serialize job.");
			System.err.println(e);

		}

	}

	private IDialer dialer;
	private String masterAddress;
	private IParallelizableJob job;
	private int priority = 0;

}
