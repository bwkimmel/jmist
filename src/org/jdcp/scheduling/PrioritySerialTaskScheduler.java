/**
 *
 */
package org.jdcp.scheduling;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.jdcp.job.TaskDescription;
import org.jdcp.remote.JobService;
import org.jmist.toolkit.RandomUtil;

/**
 * @author brad
 *
 */
public final class PrioritySerialTaskScheduler implements TaskScheduler {

	private Map<UUID, JobInfo> jobs = new HashMap<UUID, JobInfo>();
	private PriorityQueue<UUID> jobQueue = new PriorityQueue<UUID>(11, new JobIdComparator());
	private int nextOrder = 0;
	private final Random rand = new Random();

	private final class JobInfo implements Comparable<JobInfo> {

		public final UUID id;
		private int priority = JobService.DEFAULT_PRIORITY;
		private final int order = nextOrder++;
		private int nextTaskId = 0;
		private Map<Integer, TaskDescription> tasks = new HashMap<Integer, TaskDescription>();
		private final LinkedList<Integer> taskQueue = new LinkedList<Integer>();

		public JobInfo(UUID id) {
			this.id = id;
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(JobInfo other) {
			if (priority > other.priority) {
				return -1;
			} else if (priority < other.priority) {
				return 1;
			} else if (order < other.order) {
				return -1;
			} else if (order > other.order) {
				return 1;
			} else {
				return 0;
			}
		}

		private int generateTaskId() {
			int taskId;
			do {
				taskId = rand.nextInt();
			} while (tasks.containsKey(taskId));
			return taskId;
		}

		public int addTask(Object task) {
			int taskId = generateTaskId();
			TaskDescription desc = new TaskDescription(id, taskId, task);
			tasks.put(taskId, desc);
			taskQueue.addFirst(taskId);
			return taskId;
		}

		public TaskDescription getNextTask() {
			if (taskQueue.isEmpty()) {
				return null;
			}
			int taskId = taskQueue.remove();
			return tasks.get(taskId);
		}

		public Object removeTask(int taskId) {
			taskQueue.remove((Object) new Integer(taskId));
			TaskDescription desc = tasks.remove(taskId);
			return desc.getTask().get();
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

	}

	private final class JobIdComparator implements Comparator<UUID> {

		@Override
		public int compare(UUID id1, UUID id2) {
			JobInfo job1 = jobs.get(id1);
			JobInfo job2 = jobs.get(id2);
			if (job1 == null || job2 == null) {
				throw new IllegalArgumentException("Either id1 or id2 represent a non-existant job.");
			}
			return job1.compareTo(job2);
		}

	}

	private JobInfo getJob(UUID jobId) {
		JobInfo job = jobs.get(jobId);
		if (job == null) {
			job = new JobInfo(jobId);
			jobs.put(jobId, job);
			jobQueue.add(jobId);
		}
		return job;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#add(java.util.UUID, java.lang.Object)
	 */
	public int add(UUID jobId, Object task) {
		JobInfo job = getJob(jobId);
		if (!jobQueue.contains(jobId)) {
			jobQueue.add(jobId);
		}

		return job.addTask(task);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#getNextTask()
	 */
	public TaskDescription getNextTask() {
		TaskDescription desc = null;

		while (true) {
			UUID jobId = jobQueue.peek();
			if (jobId == null) {
				break;
			}

			JobInfo job = getJob(jobId);
			desc = job.getNextTask();
			if (desc == null) {
				jobQueue.remove();
			} else {
				break;
			}
		}

		return desc;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#remove(java.util.UUID, int)
	 */
	public Object remove(UUID jobId, int taskId) {
		JobInfo job = jobs.get(jobId);
		return job.removeTask(taskId);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.scheduling.TaskScheduler#setJobPriority(java.util.UUID, int)
	 */
	public void setJobPriority(UUID jobId, int priority) {
		JobInfo job = jobs.get(jobId);
		jobQueue.remove(jobId);
		job.setPriority(priority);
		jobQueue.add(jobId);
	}

	public void removeJob(UUID jobId) {
		jobQueue.remove(jobId);
		jobs.remove(jobId);
	}

}
