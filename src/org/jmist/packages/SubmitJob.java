/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.Communicator;
import org.jmist.framework.Dialer;
import org.jmist.framework.InboundMessage;
import org.jmist.framework.Job;
import org.jmist.framework.OutboundMessage;
import org.jmist.framework.ParallelizableJob;
import org.jmist.framework.ProgressMonitor;

/**
 * A job that submits a parallelizable job to a <code>MasterJob</code>.
 * @author bkimmel
 */
public final class SubmitJob implements Job {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	@Override
	public void go(ProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged(String.format("Connecting to '%s'...", this.masterAddress));

			Communicator comm = this.dialer.dial(this.masterAddress);

			monitor.notifyStatusChanged("Connected");

			OutboundMessage msg = comm.createOutboundMessage();
			ObjectOutputStream contents = new ObjectOutputStream(msg.contents());

			contents.writeInt(this.priority);
			contents.writeObject(this.job);
			contents.flush();

			monitor.notifyStatusChanged("Submitting job...");

			comm.send(msg);

			monitor.notifyStatusChanged("Submitted, waiting for confirmation...");

			InboundMessage reply = comm.receive();
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

	private Dialer dialer;
	private String masterAddress;
	private ParallelizableJob job;
	private int priority = 0;

}
