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

		ScheduledJob sched = this.jobLookup.get(jobId);
		return sched != null ? sched.worker : null;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#requestTask()
	 */
	@Override
	public TaskDescription requestTask() {

		AssignedTask task = this.getNextTask();

		if (task == null) {
			return null;
		}

		this.getNewTask(task.sched);

		return new TaskDescription(task.sched.id, task.id, task.info);

	}

	private AssignedTask getNextTask() {

		ScheduledJob sched = this.getTopScheduledJob();

		if (sched != null && sched.tasks != null) {
			sched.tasks = sched.tasks.next;
			return sched.tasks;
		}

		return null;

	}

	private ScheduledJob getTopScheduledJob() {

		while (!this.jobs.isEmpty()) {

			ScheduledJob sched = this.jobs.peek();

			if (sched.tasks != null) {
				return sched;
			}

			this.jobs.remove();
			this.jobLookup.remove(sched.id);

		}

		return null;

	}

	private void getNewTask(ScheduledJob sched) {

		Object info = sched.job.getNextTask();

		if (info != null) {

			AssignedTask task = sched.addTask(info);
			this.taskLookup.put(task.id, task);

		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#submitJob(org.jmist.framework.IParallelizableJob, int)
	 */
	@Override
	public UUID submitJob(IParallelizableJob job, int priority) {

		ScheduledJob sched = new ScheduledJob(job, priority);

		this.jobLookup.put(sched.id, sched);
		this.jobs.add(sched);

		return sched.id;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IJobMasterService#submitTaskResults(java.util.UUID, int, java.lang.Object)
	 */
	@Override
	public void submitTaskResults(UUID jobId, int taskId, Object results) {

		AssignedTask task = this.taskLookup.get(taskId);

		if (task != null) {

			if (jobId != task.sched.id) {

				if (this.verbose) {
					System.err.printf("Job ID mismatch (submitted: `%s', recorded: `%s') for task %d, ignoring.\n", jobId.toString(), task.sched.id.toString(), task.id);
				}

				return;

			}

			this.removeTask(task);

			task.sched.job.submitResults(results);

		} else if (this.verbose) {

			System.err.printf("Task %d not found, ignoring.\n", taskId);

		}

	}

	private void removeTask(AssignedTask task) {

		ScheduledJob sched = task.sched;

		this.taskLookup.remove(task.id);

		if (sched.tasks == task) {
			sched.tasks = task.previous;
		}

		if (sched.tasks == task) {
			sched.tasks = null;
		}

		task.previous.next = task.next;
		task.next.previous = task.previous;

	}

	private class AssignedTask {

		public AssignedTask			next		= null;
		public AssignedTask			previous	= null;
		public int					id;
		public Object				info;
		public ScheduledJob			sched;

		public AssignedTask(Object info, ScheduledJob sched) {

			this.id			= nextTaskId++;
			this.info		= info;
			this.sched		= sched;

		}

	}

	private class ScheduledJob implements Comparable<ScheduledJob> {

		public IParallelizableJob	job;
		public UUID					id;
		public int					priority;
		public int					order;
		public boolean				completed;
		public ITaskWorker			worker;
		public AssignedTask			tasks;

		public ScheduledJob(IParallelizableJob job, int priority) {

			this.job		= job;
			this.id			= UUID.randomUUID();
			this.priority	= priority;
			this.order		= nextOrder++;
			this.completed	= false;
			this.worker		= job.worker();
			this.tasks		= null;

		}

		public AssignedTask addTask(Object info) {

			AssignedTask task	= new AssignedTask(info, this);

			task.next			= this.tasks != null ? this.tasks.next : task;
			task.previous		= this.tasks != null ? this.tasks : task;

			if (this.tasks != null) {
				this.tasks.next.previous	= task;
				this.tasks.next				= task;
			} else {
				this.tasks					= task;
			}

			return task;

		}

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

	private int nextOrder = 0;
	private int nextTaskId = 0;
	private boolean verbose = true;

}
