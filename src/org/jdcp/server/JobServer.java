/**
 *
 */
package org.jdcp.server;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jdcp.job.ParallelizableJob;
import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;
import org.jdcp.remote.JobService;
import org.jdcp.scheduling.TaskScheduler;
import org.selfip.bkimmel.progress.ProgressMonitor;
import org.selfip.bkimmel.rmi.Envelope;
import org.selfip.bkimmel.util.classloader.MapClassLoaderStrategy;
import org.selfip.bkimmel.util.classloader.StrategyClassLoader;

/**
 * @author brad
 *
 */
public final class JobServer implements JobService {

	private final ProgressMonitor monitor;

	private final TaskScheduler scheduler;

	private final Map<UUID, ScheduledJob> jobs = new HashMap<UUID, ScheduledJob>();

	/**
	 * Creates a new <code>JobServer</code>.
	 * @param monitor The <code>ProgressMonitor</code> to report to.
	 * @param scheduler The <code>TaskScheduler</code> to use to assign
	 * 		tasks.
	 */
	public JobServer(ProgressMonitor monitor, TaskScheduler scheduler) {
		this.monitor = monitor;
		this.scheduler = scheduler;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#createJob(java.lang.String)
	 */
	public UUID createJob(String description) throws SecurityException,
			RemoteException {
		ScheduledJob sched = new ScheduledJob(description, monitor);
		jobs.put(sched.id, sched);
		return sched.id;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitJob(org.selfip.bkimmel.rmi.Envelope, java.util.UUID)
	 */
	public void submitJob(Envelope<ParallelizableJob> job, UUID jobId)
			throws IllegalArgumentException, SecurityException, RemoteException, ClassNotFoundException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null || sched.job != null) {
			throw new IllegalArgumentException("No pending job with provided Job ID");
		}

		sched.initializeJob(job);
		sched.scheduleNextTask();
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitJob(org.selfip.bkimmel.rmi.Envelope, java.lang.String)
	 */
	public UUID submitJob(Envelope<ParallelizableJob> job, String description)
			throws SecurityException, RemoteException, ClassNotFoundException {

		ScheduledJob sched = new ScheduledJob(job, description, monitor);
		jobs.put(sched.id, sched);
		sched.scheduleNextTask();
		return sched.id;

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#cancelJob(java.util.UUID)
	 */
	public void cancelJob(UUID jobId) throws IllegalArgumentException, SecurityException, RemoteException {

		if (!jobs.containsKey(jobId)) {
			throw new IllegalArgumentException("No job with provided Job ID");
		}

		jobs.remove(jobId);
		scheduler.removeJob(jobId);

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getTaskWorker(java.util.UUID)
	 */
	public Envelope<TaskWorker> getTaskWorker(UUID jobId)
			throws IllegalArgumentException, SecurityException, RemoteException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No submitted job with provided Job ID");
		}

		return sched.worker;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#requestTask()
	 */
	public TaskDescription requestTask() throws SecurityException,
			RemoteException {
		TaskDescription taskDesc = scheduler.getNextTask();
		ScheduledJob sched = jobs.get(taskDesc.getJobId());
		sched.scheduleNextTask();
		return taskDesc;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitTaskResults(java.util.UUID, int, org.selfip.bkimmel.rmi.Envelope)
	 */
	public void submitTaskResults(UUID jobId, int taskId,
			Envelope<Object> results) throws SecurityException, RemoteException, ClassNotFoundException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No submitted job with provided Job ID");
		}

		sched.submitTaskResults(taskId, results);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDefinition(java.lang.String, java.util.UUID)
	 */
	public byte[] getClassDefinition(String name, UUID jobId)
			throws SecurityException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String, java.util.UUID)
	 */
	public byte[] getClassDigest(String name, UUID jobId)
			throws SecurityException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String)
	 */
	public byte[] getClassDigest(String name) throws SecurityException,
			RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, byte[])
	 */
	public void setClassDefinition(String name, byte[] def)
			throws SecurityException, RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, java.util.UUID, byte[])
	 */
	public void setClassDefinition(String name, UUID jobId, byte[] def)
			throws IllegalArgumentException, SecurityException, RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setIdleTime(int)
	 */
	public void setIdleTime(int idleSeconds) throws IllegalArgumentException,
			SecurityException, RemoteException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setJobPriority(java.util.UUID, int)
	 */
	public void setJobPriority(UUID jobId, int priority)
			throws IllegalArgumentException, SecurityException, RemoteException {
		if (!jobs.containsKey(jobId)) {
			throw new IllegalArgumentException("No job with provided Job ID");
		}

		scheduler.setJobPriority(jobId, priority);
	}

	/**
	 * Represents a <code>ParallelizableJob</code> that has been submitted
	 * to this <code>JobMasterServer</code>.
	 * @author bkimmel
	 */
	private class ScheduledJob {

		/** The <code>ParallelizableJob</code> to be processed. */
		public ParallelizableJob				job;

		/** The <code>UUID</code> identifying the job. */
		public final UUID						id;

		/** A description of the job. */
		public final String						description;

		/** The <code>TaskWorker</code> to use to process tasks for the job. */
		public Envelope<TaskWorker>				worker;

		/**
		 * The <code>ProgressMonitor</code> to use to monitor the progress of
		 * the <code>Job</code>.
		 */
		public final ProgressMonitor			monitor;

		private final Map<String, ByteBuffer>	classDefs = new HashMap<String, ByteBuffer>();

		/**
		 * Initializes the scheduled job.
		 * @param job The <code>ParallelizableJob</code> to be processed.
		 * @param description A description of the job.
		 * @param monitor The <code>ProgressMonitor</code> from which to create a child
		 * 		monitor to use to monitor the progress of the
		 * 		<code>ParallelizableJob</code>.
		 * @throws ClassNotFoundException
		 */
		public ScheduledJob(Envelope<ParallelizableJob> job, String description, ProgressMonitor monitor) throws ClassNotFoundException {

			this.id				= UUID.randomUUID();
			this.description	= description;

			//String title		= String.format("%s (%s)", this.job.getClass().getSimpleName(), this.id.toString());
			this.monitor		= monitor.createChildProgressMonitor(description);
			this.initializeJob(job);

		}

		/**
		 * Initializes the scheduled job.
		 * @param description A description of the job.
		 * @param monitor The <code>ProgressMonitor</code> from which to create a child
		 * 		monitor to use to monitor the progress of the
		 * 		<code>ParallelizableJob</code>.
		 */
		public ScheduledJob(String description, ProgressMonitor monitor) {

			this.id				= UUID.randomUUID();
			this.description	= description;

			//String title		= String.format("%s (%s)", this.job.getClass().getSimpleName(), this.id.toString());
			this.monitor		= monitor.createChildProgressMonitor(description);
			this.monitor.notifyStatusChanged("Awaiting job submission");

		}

		public void initializeJob(Envelope<ParallelizableJob> job) throws ClassNotFoundException {
			ClassLoader loader	= new StrategyClassLoader(new MapClassLoaderStrategy(classDefs));
			this.job			= job.contents(loader);
			this.worker			= new Envelope<TaskWorker>(this.job.worker());
			this.monitor.notifyStatusChanged("");
		}

		public void submitTaskResults(int taskId, Envelope<Object> results) throws ClassNotFoundException {
			ClassLoader cl = job.getClass().getClassLoader();
			Object task = scheduler.remove(id, taskId);
			job.submitTaskResults(task, results.contents(cl), monitor);
		}

		public void scheduleNextTask() {
			scheduler.add(id, job.getNextTask());
		}

	}

}
