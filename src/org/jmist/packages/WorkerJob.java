/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.Communicator;
import org.jmist.framework.Dialer;
import org.jmist.framework.InboundMessage;
import org.jmist.framework.Job;
import org.jmist.framework.OutboundMessage;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;
import org.jmist.framework.serialization.MistObjectInputStream;

/**
 * A job that processes tasks for a parallelizable job.
 * @author bkimmel
 */
public final class WorkerJob implements Job {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	@Override
	public void go(ProgressMonitor monitor) {

		Communicator comm = this.dialer.dial(this.masterAddress);
		boolean repeat = true;

		try {

			// main loop.
			while (repeat) {

				// Prepare a request for a task.
				OutboundMessage request = comm.createOutboundMessage();
				DataOutputStream contents = new DataOutputStream(request.contents());

				request.tag(ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK);
				contents.writeLong(this.jobId.getMostSignificantBits());
				contents.writeLong(this.jobId.getLeastSignificantBits());
				contents.flush();

				// Send the request.
				comm.send(request);

				// Wait for the reply.
				InboundMessage reply = comm.receive();

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

	private boolean processTask(InboundMessage msg, Communicator comm, ProgressMonitor monitor) throws IOException {

		MistObjectInputStream contents = new MistObjectInputStream(msg.contents());
		UUID msgJobId = new UUID(contents.readLong(), contents.readLong());
		int taskId = contents.readInt();
		Object task;

		try {

			task = contents.readObject();

		} catch (ClassNotFoundException e) {

			System.err.println("Failed to deserialize task.");
			System.err.println(e);
			return false;

		}

		if (this.jobId.compareTo(msgJobId) != 0) {

			OutboundMessage taskRequest = comm.createOutboundMessage();
			ObjectOutputStream taskRequestContents = new ObjectOutputStream(taskRequest.contents());

			taskRequest.tag(ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK_WORKER);
			taskRequestContents.writeLong(msgJobId.getMostSignificantBits());
			taskRequestContents.writeLong(msgJobId.getLeastSignificantBits());
			taskRequestContents.flush();

			comm.send(taskRequest);

			InboundMessage taskResponse = comm.receive();
			assert(taskResponse.tag() == ParallelJobCommon.MESSAGE_TAG_PROVIDE_TASK_WORKER);

			MistObjectInputStream taskResponseContents = new MistObjectInputStream(taskResponse.contents());
			UUID workerJobId = new UUID(taskResponseContents.readLong(), taskResponseContents.readLong());

			assert(workerJobId.compareTo(msgJobId) == 0);

			try {

				this.worker = (TaskWorker) contents.readObject();

			} catch (ClassNotFoundException e) {

				System.err.println("Cannot create task worker.");
				System.err.println(e);
				return false;

			}

		}

		this.jobId = msgJobId;

		Object results = this.worker.performTask(task, monitor);
		OutboundMessage reply = comm.createOutboundMessage();
		ObjectOutputStream replyContents = new ObjectOutputStream(reply.contents());

		reply.tag(ParallelJobCommon.MESSAGE_TAG_SUBMIT_TASK_RESULTS);
		replyContents.writeLong(this.jobId.getMostSignificantBits());
		replyContents.writeLong(this.jobId.getLeastSignificantBits());
		replyContents.writeInt(taskId);
		replyContents.writeObject(results);
		replyContents.flush();

		comm.send(reply);

		return true;

	}

	private boolean processIdle(InboundMessage msg, Communicator comm, ProgressMonitor monitor) throws IOException {

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

	private Dialer dialer;
	private String masterAddress;
	private TaskWorker worker;
	private UUID jobId = new UUID(0, 0);

}
