/**
 *
 */
package org.jdcp.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jdcp.job.ParallelizableJob;
import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;
import org.jdcp.remote.JobService;
import org.jdcp.scheduling.TaskScheduler;
import org.jdcp.server.classmanager.ClassManager;
import org.jdcp.server.classmanager.ParentClassManager;
import org.selfip.bkimmel.progress.ProgressMonitor;
import org.selfip.bkimmel.rmi.Serialized;
import org.selfip.bkimmel.util.classloader.StrategyClassLoader;

/**
 * @author brad
 *
 */
public final class JobServer implements JobService {

	private static final int DEFAULT_IDLE_SECONDS = 10;

	private final ProgressMonitor monitor;

	private final TaskScheduler scheduler;

	private final ParentClassManager classManager;

	private final File outputDirectory;

	private final Map<UUID, ScheduledJob> jobs = new HashMap<UUID, ScheduledJob>();

	private TaskDescription idleTask = new TaskDescription(null, 0, DEFAULT_IDLE_SECONDS);

	/**
	 * Creates a new <code>JobServer</code>.
	 * @param outputDirectory The directory to write job results to.
	 * @param monitor The <code>ProgressMonitor</code> to report to.
	 * @param scheduler The <code>TaskScheduler</code> to use to assign
	 * 		tasks.
	 * @param classManager The <code>ParentClassManager</code> to use to
	 * 		store and retrieve class definitions.
	 */
	public JobServer(File outputDirectory, ProgressMonitor monitor, TaskScheduler scheduler, ParentClassManager classManager) throws IllegalArgumentException {
		if (!outputDirectory.isDirectory()) {
			throw new IllegalArgumentException("outputDirectory must be a directory.");
		}
		this.outputDirectory = outputDirectory;
		this.monitor = monitor;
		this.scheduler = scheduler;
		this.classManager = classManager;
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
	public void submitJob(Serialized<ParallelizableJob> job, UUID jobId)
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
	public UUID submitJob(Serialized<ParallelizableJob> job, String description)
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
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No job with provided Job ID");
		}

		jobs.remove(jobId);
		scheduler.removeJob(jobId);
		classManager.releaseChildClassManager(sched.classManager);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getTaskWorker(java.util.UUID)
	 */
	public Serialized<TaskWorker> getTaskWorker(UUID jobId)
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
	public synchronized TaskDescription requestTask() throws SecurityException,
			RemoteException {
		TaskDescription taskDesc = scheduler.getNextTask();
		if (taskDesc == null) {
			return idleTask;
		}

		ScheduledJob sched = jobs.get(taskDesc.getJobId());
		sched.scheduleNextTask();
		return taskDesc;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitTaskResults(java.util.UUID, int, org.selfip.bkimmel.rmi.Envelope)
	 */
	public void submitTaskResults(UUID jobId, int taskId,
			Serialized<Object> results) throws SecurityException, RemoteException, ClassNotFoundException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No submitted job with provided Job ID");
		}

		if (sched.submitTaskResults(taskId, results)) { // job is complete
			jobs.remove(jobId);
			scheduler.removeJob(jobId);
			classManager.releaseChildClassManager(sched.classManager);
		}
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDefinition(java.lang.String, java.util.UUID)
	 */
	public byte[] getClassDefinition(String name, UUID jobId)
			throws SecurityException, RemoteException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No job with provided Job ID");
		}

		ByteBuffer def = sched.classManager.getClassDefinition(name);
		if (def.hasArray() && def.arrayOffset() == 0) {
			return def.array();
		} else {
			byte[] bytes = new byte[def.remaining()];
			def.get(bytes);
			return bytes;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String, java.util.UUID)
	 */
	public byte[] getClassDigest(String name, UUID jobId)
			throws SecurityException, RemoteException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null) {
			throw new IllegalArgumentException("No job with provided Job ID");
		}

		return sched.classManager.getClassDigest(name);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String)
	 */
	public byte[] getClassDigest(String name) throws SecurityException,
			RemoteException {
		return classManager.getClassDigest(name);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, byte[])
	 */
	public void setClassDefinition(String name, byte[] def)
			throws SecurityException, RemoteException {
		classManager.setClassDefinition(name, def);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, java.util.UUID, byte[])
	 */
	public void setClassDefinition(String name, UUID jobId, byte[] def)
			throws IllegalArgumentException, SecurityException, RemoteException {
		ScheduledJob sched = jobs.get(jobId);
		if (sched == null || sched.job != null) {
			throw new IllegalArgumentException("No pending job with provided Job ID");
		}

		sched.classManager.setClassDefinition(name, def);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setIdleTime(int)
	 */
	public void setIdleTime(int idleSeconds) throws IllegalArgumentException,
			SecurityException, RemoteException {
		idleTask = new TaskDescription(null, 0, idleSeconds);
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
		public Serialized<TaskWorker>				worker;

		/**
		 * The <code>ProgressMonitor</code> to use to monitor the progress of
		 * the <code>Job</code>.
		 */
		public final ProgressMonitor			monitor;

		/**
		 * The <code>ClassManager</code> to use to store the class definitions
		 * applicable to this job.
		 */
		public final ClassManager				classManager;

		/**
		 * Initializes the scheduled job.
		 * @param job The <code>ParallelizableJob</code> to be processed.
		 * @param description A description of the job.
		 * @param monitor The <code>ProgressMonitor</code> from which to create a child
		 * 		monitor to use to monitor the progress of the
		 * 		<code>ParallelizableJob</code>.
		 * @throws ClassNotFoundException
		 */
		public ScheduledJob(Serialized<ParallelizableJob> job, String description, ProgressMonitor monitor) throws ClassNotFoundException {

			this.id				= UUID.randomUUID();
			this.description	= description;

			//String title		= String.format("%s (%s)", this.job.getClass().getSimpleName(), this.id.toString());
			this.monitor		= monitor.createChildProgressMonitor(description);
			this.classManager	= JobServer.this.classManager.createChildClassManager();

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

			this.classManager	= JobServer.this.classManager.createChildClassManager();

		}

		public void initializeJob(Serialized<ParallelizableJob> job) throws ClassNotFoundException {
			ClassLoader loader	= new StrategyClassLoader(classManager, JobServer.class.getClassLoader());
			this.job			= job.deserialize(loader);
			this.worker			= new Serialized<TaskWorker>(this.job.worker());
			this.monitor.notifyStatusChanged("");
		}

		public boolean submitTaskResults(int taskId, Serialized<Object> results) throws ClassNotFoundException {
			ClassLoader cl = job.getClass().getClassLoader();
			Object task = scheduler.remove(id, taskId);
			job.submitTaskResults(task, results.deserialize(cl), monitor);

			if (job.isComplete()) {
				writeJobResults();
				return true;
			}

			return false;
		}

		public void scheduleNextTask() {
			scheduler.add(id, job.getNextTask());
		}

		/**
		 * Writes the results of a <code>ScheduledJob</code> to the output
		 * directory.
		 * @param sched The <code>ScheduledJob</code> to write results for.
		 */
		private void writeJobResults() {

			assert(job.isComplete());

			try {

				String				filename		= String.format("%s.zip", id.toString());
				File				outputFile		= new File(outputDirectory, filename);
				OutputStream		out				= new FileOutputStream(outputFile);
				ZipOutputStream		zip				= new ZipOutputStream(out);

				zip.putNextEntry(new ZipEntry("job.log"));

				PrintStream			log				= new PrintStream(zip);
				log.printf("%tc: Job %s completed.", new Date(), id.toString());
				log.println();
				log.flush();

				zip.closeEntry();

				job.writeJobResults(zip);
				zip.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
