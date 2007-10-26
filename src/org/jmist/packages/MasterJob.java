/**
 *
 */
package org.jmist.packages;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IOutboundMessage;
import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.base.ServerJob;

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

	}

	private void processRequestTaskWorker(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

	}

	private void processSubmitTaskResults(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

	}

	private void processSubmitJob(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {

	}

	private class AssignedTask {

		public AssignedTask			next;
		public AssignedTask			previous;
		public int					id;
		public IOutboundMessage		info;
		public ScheduledJob			sched;

	}

	private class ScheduledJob implements Comparable<ScheduledJob> {

		public IParallelizableJob	job;
		public UUID					id;
		public int					priority;
		public int					order;
		public boolean				completed;
		public IOutboundMessage		workerDef;
		public AssignedTask			tasks;

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(ScheduledJob arg0) {

			if (this.priority > arg0.priority) {
				return 1;
			}

			if (this.priority < arg0.priority) {
				return -1;
			}

			if (this.order > arg0.order) {
				return -1;
			}

			if (this.order < arg0.order) {
				return 1;
			}

			return 0;

		}

	}


	private final Queue<ScheduledJob> jobs = new PriorityQueue<ScheduledJob>();
	private final Map<UUID, ScheduledJob> jobLookup = new HashMap<UUID, ScheduledJob>();
	private final Map<Integer, AssignedTask> taskLookup = new HashMap<Integer, AssignedTask>();

	private int nextOrder;
	private long idleTime;

}
