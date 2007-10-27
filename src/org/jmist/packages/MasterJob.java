/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.Communicator;
import org.jmist.framework.InboundMessage;
import org.jmist.framework.OutboundMessage;
import org.jmist.framework.ParallelizableJob;
import org.jmist.framework.ProgressMonitor;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.base.ServerJob;
import org.jmist.framework.serialization.MistObjectInputStream;
import org.jmist.framework.services.JobMasterService;
import org.jmist.framework.services.JobMasterServer;
import org.jmist.framework.services.TaskDescription;

/**
 * A job that processes manages the distribution of tasks for parallelizable
 * jobs.
 * @author bkimmel
 */
public final class MasterJob extends ServerJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.base.NetworkJob#onMessageReceived(org.jmist.framework.InboundMessage, org.jmist.framework.Communicator, org.jmist.framework.ProgressMonitor)
	 */
	@Override
	protected boolean onMessageReceived(InboundMessage msg, Communicator comm,
			ProgressMonitor monitor) {

		switch (msg.tag()) {

		case ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK:
			this.processRequestTask(msg, comm, monitor);
			break;

		case ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK_WORKER:
			this.processRequestTaskWorker(msg, comm, monitor);
			break;

		case ParallelJobCommon.MESSAGE_TAG_SUBMIT_JOB:
			this.processSubmitJob(msg, comm, monitor);
			break;

		case ParallelJobCommon.MESSAGE_TAG_SUBMIT_TASK_RESULTS:
			this.processSubmitTaskResults(msg, comm, monitor);
			break;

		default:
			return false;

		}

		return true;

	}

	private void processRequestTask(InboundMessage msg, Communicator comm, ProgressMonitor monitor) {

		try {

			TaskDescription taskDesc = this.service.requestTask();
			OutboundMessage reply = comm.createOutboundMessage();
			ObjectOutputStream contents = new ObjectOutputStream(reply.contents());

			if (taskDesc != null) {

				reply.tag(ParallelJobCommon.MESSAGE_TAG_ASSIGN_TASK);
				contents.writeLong(taskDesc.getJobId().getMostSignificantBits());
				contents.writeLong(taskDesc.getJobId().getLeastSignificantBits());
				contents.writeInt(taskDesc.getTaskId());
				contents.writeObject(taskDesc.getTaskId());

			} else { /* taskDesc == null */

				reply.tag(ParallelJobCommon.MESSAGE_TAG_IDLE);
				contents.writeLong(this.idleTime);

			}

			contents.flush();

			comm.queue(reply);

		} catch (IOException e) {

			System.err.println("Failed to send task to requestor.");
			System.err.println(e);

		}

	}

	private void processRequestTaskWorker(InboundMessage msg, Communicator comm, ProgressMonitor monitor) {

		try {

			DataInputStream contents = new DataInputStream(msg.contents());
			UUID jobId = new UUID(contents.readLong(), contents.readLong());
			TaskWorker worker = this.service.getTaskWorker(jobId);

			OutboundMessage reply = comm.createOutboundMessage();
			ObjectOutputStream replyContents = new ObjectOutputStream(reply.contents());

			reply.tag(ParallelJobCommon.MESSAGE_TAG_PROVIDE_TASK_WORKER);
			replyContents.writeLong(jobId.getMostSignificantBits());
			replyContents.writeLong(jobId.getLeastSignificantBits());
			replyContents.writeObject(worker);
			replyContents.flush();

			comm.queue(reply);

		} catch (IOException e) {

			System.err.println("Failed to send task worker to requestor.");
			System.err.println(e);

		}

	}

	private void processSubmitTaskResults(InboundMessage msg, Communicator comm, ProgressMonitor monitor) {

		try {

			MistObjectInputStream contents = new MistObjectInputStream(msg.contents());
			UUID jobId = new UUID(contents.readLong(), contents.readLong());
			int taskId = contents.readInt();
			Object results = contents.readObject();

			this.service.submitTaskResults(jobId, taskId, results);

		} catch (IOException e) {

			System.err.println("Failed to obtain task results.");
			System.err.println(e);

		} catch (ClassNotFoundException e) {

			System.err.println("Failed to deserialize task results.");
			System.err.println(e);

		}

	}

	private void processSubmitJob(InboundMessage msg, Communicator comm, ProgressMonitor monitor) {

		try {

			OutboundMessage reply = comm.createOutboundMessage();
			ObjectOutputStream replyContents = new ObjectOutputStream(reply.contents());

			try {

				MistObjectInputStream contents = new MistObjectInputStream(msg.contents());
				int priority = contents.readInt();
				ParallelizableJob job = (ParallelizableJob) contents.readObject();

				UUID jobId = this.service.submitJob(job, priority);

				if (jobId != null) {
					replyContents.writeLong(jobId.getMostSignificantBits());
					replyContents.writeLong(jobId.getLeastSignificantBits());
				} else { /* jobId == null */
					replyContents.writeLong(0);
					replyContents.writeLong(0);
				}

			} catch (IOException e) {

				System.err.println("Failed to send task worker to requestor.");
				System.err.println(e);

				replyContents.writeLong(0);
				replyContents.writeLong(0);
				replyContents.writeUTF(e.getMessage());

			} catch (ClassNotFoundException e) {

				System.err.println("Failed to deserialize job.");
				System.err.println(e);

				replyContents.writeLong(0);
				replyContents.writeLong(0);
				replyContents.writeUTF(e.getMessage());

			} finally {

				reply.tag(ParallelJobCommon.MESSAGE_TAG_SUBMIT_JOB_ACK);
				replyContents.flush();
				comm.queue(reply);

			}

		} catch (IOException e) {

			System.err.println("Failed to prepare response to submitJob request.");
			System.err.println(e);

		}

	}

	private final JobMasterService service = new JobMasterServer();
	private long idleTime;

}
