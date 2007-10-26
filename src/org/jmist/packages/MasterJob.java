/**
 *
 */
package org.jmist.packages;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IJobMasterService;
import org.jmist.framework.IOutboundMessage;
import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.ITaskWorker;
import org.jmist.framework.TaskDescription;
import org.jmist.framework.base.ServerJob;
import org.jmist.framework.serialization.MistObjectInputStream;

/**
 * A job that processes manages the distribution of tasks for parallelizable
 * jobs.
 * @author bkimmel
 */
public final class MasterJob extends ServerJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.base.NetworkJob#onMessageReceived(org.jmist.framework.IInboundMessage, org.jmist.framework.ICommunicator, org.jmist.framework.IProgressMonitor)
	 */
	@Override
	protected boolean onMessageReceived(IInboundMessage msg, ICommunicator comm,
			IProgressMonitor monitor) {

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

	private void processRequestTask(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

		try {

			TaskDescription taskDesc = this.service.requestTask();
			IOutboundMessage reply = comm.createOutboundMessage();
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

	private void processRequestTaskWorker(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

		try {

			DataInputStream contents = new DataInputStream(msg.contents());
			UUID jobId = new UUID(contents.readLong(), contents.readLong());
			ITaskWorker worker = this.service.getTaskWorker(jobId);

			IOutboundMessage reply = comm.createOutboundMessage();
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

	private void processSubmitTaskResults(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

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

	private void processSubmitJob(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

		try {

			IOutboundMessage reply = comm.createOutboundMessage();
			ObjectOutputStream replyContents = new ObjectOutputStream(reply.contents());

			try {

				MistObjectInputStream contents = new MistObjectInputStream(msg.contents());
				int priority = contents.readInt();
				IParallelizableJob job = (IParallelizableJob) contents.readObject();

				UUID jobId = this.service.submitJob(job, priority);

				replyContents.writeLong(jobId.getMostSignificantBits());
				replyContents.writeLong(jobId.getLeastSignificantBits());

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

	private final IJobMasterService service = new JobMasterService();
	private long idleTime;

}
