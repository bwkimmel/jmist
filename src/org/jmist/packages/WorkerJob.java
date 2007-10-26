/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IDialer;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IJob;
import org.jmist.framework.IOutboundMessage;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.ITaskWorker;
import org.jmist.framework.serialization.MistObjectInputStream;

/**
 * A job that processes tasks for a parallelizable job.
 * @author bkimmel
 */
public final class WorkerJob implements IJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJob#go(org.jmist.framework.IProgressMonitor)
	 */
	@Override
	public void go(IProgressMonitor monitor) {

		ICommunicator comm = this.dialer.dial(this.masterAddress);
		boolean repeat = true;

		try {

			// main loop.
			while (repeat) {

				// Prepare a request for a task.
				IOutboundMessage request = comm.createOutboundMessage();
				DataOutputStream contents = new DataOutputStream(request.contents());

				request.tag(ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK);
				contents.writeLong(this.jobId.getMostSignificantBits());
				contents.writeLong(this.jobId.getLeastSignificantBits());
				contents.flush();

				// Send the request.
				comm.send(request);

				// Wait for the reply.
				IInboundMessage reply = comm.receive();

				// Process the reply.
				switch (reply.tag()) {

					case ParallelJobCommon.MESSAGE_TAG_ASSIGN_TASK:
						repeat = this.processTask(reply, comm, monitor);
						break;

					case ParallelJobCommon.MESSAGE_TAG_IDLE:
						repeat = this.processIdle(reply, comm, monitor);
						break;

				}

			} // end main loop.

		} catch (IOException e) {

			// TODO do something other than just print the error.
			System.err.println(e);

		}

	}

	private boolean processTask(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) throws IOException {

		MistObjectInputStream contents = new MistObjectInputStream(msg.contents());

		UUID msgJobId = new UUID(contents.readLong(), contents.readLong());
		int taskId = contents.readInt();
		boolean taskWorkerIncluded = contents.readBoolean();

		try {

			if (taskWorkerIncluded) {

				this.worker = (ITaskWorker) contents.readObject();

			} else if (this.jobId.compareTo(msgJobId) != 0) {

				IOutboundMessage taskRequest = comm.createOutboundMessage();
				ObjectOutputStream taskRequestContents = new ObjectOutputStream(taskRequest.contents());

				taskRequest.tag(ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK_WORKER);
				taskRequestContents.writeLong(msgJobId.getMostSignificantBits());
				taskRequestContents.writeLong(msgJobId.getLeastSignificantBits());
				taskRequestContents.flush();

				comm.send(taskRequest);

				IInboundMessage taskResponse = comm.receive();
				assert(taskResponse.tag() == ParallelJobCommon.MESSAGE_TAG_PROVIDE_TASK_WORKER);

				MistObjectInputStream taskResponseContents = new MistObjectInputStream(taskResponse.contents());
				UUID workerJobId = new UUID(taskResponseContents.readLong(), taskResponseContents.readLong());

				assert(workerJobId.compareTo(msgJobId) == 0);

				this.worker = (ITaskWorker) contents.readObject();

			}

		} catch (ClassNotFoundException e) {

			System.err.println("Cannot create task worker.");
			System.err.println(e);
			return false;

		}

		this.jobId = msgJobId;

		IOutboundMessage reply = comm.createOutboundMessage();
		ObjectOutputStream replyContents = new ObjectOutputStream(reply.contents());

		reply.tag(ParallelJobCommon.MESSAGE_TAG_SUBMIT_TASK_RESULTS);
		replyContents.writeObject(this.jobId.getMostSignificantBits());
		replyContents.writeObject(this.jobId.getLeastSignificantBits());
		replyContents.writeInt(taskId);

		this.worker.performTask(contents, replyContents, monitor);

		replyContents.flush();

		comm.send(reply);

		return true;

	}

	private boolean processIdle(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) throws IOException {

		DataInputStream	contents = new DataInputStream(msg.contents());
		long idleTime = contents.readLong();

		monitor.notifyStatusChanged(String.format("Idling for %dms.", idleTime));

		if (!monitor.notifyIndeterminantProgress()) {
			return false;
		}

		try {
			Thread.sleep(idleTime);
		} catch (InterruptedException e) {
			// nothing to do.
		}

		return true;

	}

	private IDialer dialer;
	private String masterAddress;
	private ITaskWorker worker;
	private UUID jobId = new UUID(0, 0);

}
