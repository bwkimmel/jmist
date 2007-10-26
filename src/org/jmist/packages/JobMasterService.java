/**
 *
 */
package org.jmist.packages;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import org.jmist.framework.IJobMasterService;
import org.jmist.framework.IOutboundMessage;
import org.jmist.framework.IParallelizableJob;
import org.jmist.framework.ITaskWorker;
import org.jmist.framework.TaskDescription;

/**
 * @author bkimmel
 *
 */
public final class JobMasterService implements IJobMasterService {

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#getTaskWorker(java.util.UUID)
	 */
	@Override
	public ITaskWorker getTaskWorker(UUID jobId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#requestTask()
	 */
	@Override
	public TaskDescription requestTask() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#submitJob(org.jmist.framework.IParallelizableJob, int)
	 */
	@Override
	public UUID submitJob(IParallelizableJob job, int priority) {
		return null;
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#submitTaskResults(java.util.UUID, int, java.lang.Object)
	 */
	@Override
	public void submitTaskResults(UUID jobId, int taskId, Object results) {
		// TODO Auto-generated method stub

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
