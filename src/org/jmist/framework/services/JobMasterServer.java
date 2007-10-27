/**
 *
 */
package org.jmist.framework.services;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

import org.jmist.framework.ParallelizableJob;
import org.jmist.framework.TaskWorker;

/**
 * An implementation of <code>JobMasterService</code>: a remote service for
 * accepting <code>ParallelizableJob</code>s, managing the distribution of
 * tasks to workers, and aggregating the results submitted by workers.
 * @author bkimmel
 */
public final class JobMasterServer implements JobMasterService {

	/**
	 * Initializes the service.
	 */
	public JobMasterServer() {
		this.verbose = true;
	}

	/**
	 * Initializes the service.
	 * @param verbose A value indicating whether to write debugging output
	 * 		to <code>System.err</code>.
	 */
	public JobMasterServer(boolean verbose) {
		this.verbose = verbose;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.JobMasterService#getTaskWorker(java.util.UUID)
	 */
	@Override
	public synchronized TaskWorker getTaskWorker(UUID jobId) {

		ScheduledJob sched = this.jobLookup.get(jobId);
		return sched != null ? sched.worker : null;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.JobMasterService#requestTask()
	 */
	@Override
	public synchronized TaskDescription requestTask() {

		if (this.verbose) {
			System.err.println("Received request for task.");
		}

		// Get the task to assign to the caller.
		AssignedTask task = this.getNextTask();

		if (task == null) {
			return null;
		}

		// Prepare the next task.
		this.getNewTask(task.sched);

		if (this.verbose) {
			System.err.printf("Assigning task %s/%d.\n", task.sched.id.toString(), task.id);
		}

		return new TaskDescription(task.sched.id, task.id, task.info);

	}

	/**
	 * Gets a task to assign to the caller, and pushes that task to the back
	 * of the associated job's task queue.
	 * @return The next task to assign to the caller.
	 */
	private AssignedTask getNextTask() {

		// Get the highest priority job that has not already been completed.
		ScheduledJob sched = this.getTopScheduledJob();

		// Advance to the next task in that job's queue and return it.
		if (sched != null) {
			assert(sched.tasks != null);
			sched.tasks = sched.tasks.next;
			return sched.tasks;
		}

		// There was no task to assign.
		return null;

	}

	/**
	 * Gets the highest priority incomplete job.
	 * @return The highest priority incomplete job.
	 */
	private ScheduledJob getTopScheduledJob() {

		/* Pop job's off the priority queue until we find one that is not
		 * complete.
		 */
		while (!this.jobs.isEmpty()) {

			// Get the next job in order by priority.
			ScheduledJob sched = this.jobs.peek();

			// If the job is not complete, then return it.
			if (sched.tasks != null) {
				return sched;
			}

			// Remove the job.
			this.jobs.remove();
			this.jobLookup.remove(sched.id);

		}

		// No jobs left to process.
		return null;

	}

	/**
	 * Prepares the next task for a job to be served.
	 * @param sched The job to get get the next task for.
	 */
	private void getNewTask(ScheduledJob sched) {

		// Get the next task.
		Object info = sched.job.getNextTask();

		// If the job has a new task available, then add it to the schedule.
		if (info != null) {

			AssignedTask task = sched.addTask(info);
			this.taskLookup.put(task.id, task);

		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.JobMasterService#submitJob(org.jmist.framework.ParallelizableJob, int)
	 */
	@Override
	public synchronized UUID submitJob(ParallelizableJob job, int priority) {

		// Create the job and add it to the schedule.
		ScheduledJob sched = new ScheduledJob(job, priority);

		this.jobLookup.put(sched.id, sched);
		this.jobs.add(sched);

		// Prepare the first task.
		this.getNewTask(sched);

		return sched.id;

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.JobMasterService#submitTaskResults(java.util.UUID, int, java.lang.Object)
	 */
	@Override
	public synchronized void submitTaskResults(UUID jobId, int taskId, Object results) {

		// Find the task.
		AssignedTask task = this.taskLookup.get(taskId);

		if (task != null) {

			// We found the task, check that the job ID matches what we expect.
			if (jobId != task.sched.id) {

				// Job ID is incorrect.
				if (this.verbose) {
					System.err.printf("Job ID mismatch (submitted: `%s', recorded: `%s') for task %d, ignoring.\n", jobId.toString(), task.sched.id.toString(), task.id);
				}

				return;

			}

			/* Job ID is correct -- remove the task and submit the results
			 * to the ParallelizableJob.
			 */
			this.removeTask(task);

			if (this.verbose) {
				System.err.printf("Results from task %s/%d.\n", task.sched.id.toString(), task.id);
			}

			task.sched.job.submitTaskResults(results);

		} else if (this.verbose) { /* task == null */

			/* The task was not found: either the task id was invalid, or that
			 * task was already completed.
			 */
			System.err.printf("Task %d not found, ignoring.\n", taskId);

		}

	}

	/**
	 * Removes a task from the schedule.
	 * @param task The <code>AssignedTask</code> to remove.
	 */
	private void removeTask(AssignedTask task) {

		ScheduledJob sched = task.sched;

		// Remove it from the lookup.
		this.taskLookup.remove(task.id);

		// Unlink it from the ScheduledJob's circularly linked list.
		if (sched.tasks == task) {
			sched.tasks = task.previous;
		}

		if (sched.tasks == task) {
			sched.tasks = null;
		}

		task.previous.next = task.next;
		task.next.previous = task.previous;

	}

	/**
	 * Represents a task for a <code>ParallelizableJob</code> that is
	 * awaiting completion.  Instances of this class are nodes in a circularly
	 * linked list of the <code>AssignedTask</code>s for the associated
	 * <code>ScheduledJob</code>.
	 * @author bkimmel
	 * @see {@link ScheduledJob}.
	 */
	private class AssignedTask {

		/**
		 * The next <code>AssignedTask</code> in the circularly linked list.
		 */
		public AssignedTask			next		= null;

		/**
		 * The previous <code>AssignedTask</code> in the circularly linked
		 * list.
		 */
		public AssignedTask			previous	= null;

		/**
		 * The ID of the task.  This value is unique modulo the owning instance
		 * of <code>JobMasterServer</code>.
		 */
		public int					id;

		/**
		 * The <code>Object</code> describing the task to be performed
		 * (obtained via a call to <code>ParallelizableJob.getNextTask()</code>.
		 * @see {@link org.jmist.framework.ParallelizableJob#getNextTask()}.
		 */
		public Object				info;

		/**
		 * The <code>ScheduledJob</code> associated with this task.
		 */
		public ScheduledJob			sched;

		/**
		 * Initializes the <code>Object</code> describing the task to be
		 * performed and the owning <code>ScheduledJob</code>.
		 * @param info The <code>Object</code> describing the task to be
		 * 		performed.
		 * @param sched The owning <code>ScheduledJob</code>.
		 */
		public AssignedTask(Object info, ScheduledJob sched) {

			// Assign a unique task id.
			this.id			= nextTaskId++;

			this.info		= info;
			this.sched		= sched;

		}

	}

	/**
	 * Represents a <code>ParallelizableJob</code> that has been submitted
	 * to this <code>JobMasterServer</code>.
	 * @author bkimmel
	 */
	private class ScheduledJob implements Comparable<ScheduledJob> {

		/** The <code>ParallelizableJob</code> to be processed. */
		public ParallelizableJob	job;

		/** The <code>UUID</code> identifying the job. */
		public UUID					id;

		/** The priority assigned to the job. */
		public int					priority;

		/**
		 * The order in which this job was submitted (used as a tie-breaker
		 * for evaluating the relative priority:  If two jobs have the same
		 * value for <code>priority</code>, the job submitted first is
		 * processed first).
		 */
		public int					order;

		/** The <code>TaskWorker</code> to use to process tasks for the job. */
		public TaskWorker			worker;

		/**
		 * The head of the circularly linked list of outstanding tasks for the
		 * job.
		 */
		public AssignedTask			tasks;

		/**
		 * Initializes the scheduled job.
		 * @param job The <code>ParallelizableJob</code> to be processed.
		 * @param priority The priority to assign to the job.
		 */
		public ScheduledJob(ParallelizableJob job, int priority) {

			this.job		= job;
			this.id			= UUID.randomUUID();
			this.priority	= priority;
			this.order		= nextOrder++;
			this.worker		= job.worker();
			this.tasks		= null;

		}

		/**
		 * Adds a task to this job.
		 * @param info The <code>Object</code> describing the task to be
		 * 		performed.
		 * @return The new <code>AssignedTask</code> added to this job.
		 */
		public AssignedTask addTask(Object info) {

			// Initialize the assigned task.
			AssignedTask task	= new AssignedTask(info, this);

			// Link it in to this job's circularly linked list of tasks.
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

			// First compare priorities.
			if (this.priority > arg0.priority) {
				return 1;
			}

			if (this.priority < arg0.priority) {
				return -1;
			}

			// The priorities are equal, so the first job submitted wins.
			if (this.order > arg0.order) {
				return -1;
			}

			if (this.order < arg0.order) {
				return 1;
			}

			return 0;

		}

	}

	/**
	 * A <code>PriorityQueue</code> of the jobs submitted to this
	 * <code>JobMasterServer</code>.
	 */
	private final Queue<ScheduledJob> jobs = new PriorityQueue<ScheduledJob>();

	/**
	 * A <code>Map</code> to use to look up submitted job's by their
	 * <code>UUID</code>.
	 */
	private final Map<UUID, ScheduledJob> jobLookup = new HashMap<UUID, ScheduledJob>();

	/**
	 * A <code>Map</code> to use to look up outstanding tasks by their task ID.
	 */
	private final Map<Integer, AssignedTask> taskLookup = new HashMap<Integer, AssignedTask>();

	/**
	 * The order of the next job to be submitted.
	 */
	private int nextOrder = 0;

	/**
	 * The ID of the next task to be assigned.
	 */
	private int nextTaskId = 0;

	/**
	 * A value indicating whether to write debugging output to
	 * <code>System.err</code>.
	 */
	private final boolean verbose;

}
