/**
 *
 */
package org.jmist.framework.services;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import org.jmist.framework.Job;
import org.jmist.framework.ProgressMonitor;
import org.jmist.framework.TaskWorker;

/**
 * A job that processes tasks for a parallelizable job from a remote
 * <code>JobServiceMaster<code>.  This class may potentially use multiple
 * threads to process tasks.
 * @author bkimmel
 */
public final class ThreadServiceWorkerJob implements Job {

	/**
	 * Initializes the address of the master and the amount of time to idle
	 * when no task is available.
	 * @param masterHost The URL of the master.
	 * @param idleTime The time (in milliseconds) to idle when no task is
	 * 		available.
	 * @param executor The <code>Executor</code> to use to process tasks
	 * 		(must not use an unbounded queue).
	 */
	public ThreadServiceWorkerJob(String masterHost, long idleTime, Executor executor) {
		this.masterHost = masterHost;
		this.idleTime = idleTime;
		this.executor = executor;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	@Override
	public void go(ProgressMonitor monitor) {

		try {

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged("Looking up master...");

			Registry registry = LocateRegistry.getRegistry(this.masterHost);
			this.service = (JobMasterService) registry.lookup("JobMasterService");

			while (monitor.notifyIndeterminantProgress()) {

				this.executor.execute(new Worker(monitor.createChildProgressMonitor()));

			}

			monitor.notifyStatusChanged("Cancelled.");

		} catch (Exception e) {

			monitor.notifyStatusChanged("Exception: " + e.toString());

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		} finally {

			monitor.notifyCancelled();

		}

	}

	/**
	 * An entry in the <code>TaskWorker</code> cache.
	 * @author bkimmel
	 */
	private class WorkerCacheEntry {

		/**
		 * The <code>UUID</code> of the job that the <code>TaskWorker</code>
		 * processes tasks for.
		 */
		public final UUID			jobId;

		/**
		 * The cached <code>TaskWorker</code>.
		 */
		public final TaskWorker	worker;

		/**
		 * Initializes the cache entry.
		 * @param jobId The <code>UUID</code> of the job that the
		 * 		<code>TaskWorker</code> processes tasks for.
		 * @param worker The <code>TaskWorker</code> to be cached.
		 */
		public WorkerCacheEntry(UUID jobId, TaskWorker worker) {
			this.jobId = jobId;
			this.worker = worker;
		}

	}

	/**
	 * Searches for the <code>TaskWorker</code> to use to process tasks for
	 * the job with the specified <code>UUID</code> in the local cache.
	 * @param jobId The <code>UUID</code> of the job whose
	 * 		<code>TaskWorker</code> to search for.
	 * @return The <code>TaskWorker</code> corresponding to the job with the
	 * 		specified <code>UUID</code>, or <code>null</code> if the
	 * 		<code>TaskWorker</code> for that job is not in the cache.
	 */
	private synchronized TaskWorker getCachedTaskWorker(UUID jobId) {

		assert(jobId != null);

		Iterator<WorkerCacheEntry> i = this.workerCache.iterator();

		/* Search for the worker for the specified job. */
		while (i.hasNext()) {

			WorkerCacheEntry entry = i.next();

			if (entry.jobId.compareTo(jobId) == 0) {

				/* Remove the entry and re-insert it at the end of the list.
				 * This will ensure that when an item is removed from the list,
				 * the item that is removed will always be the least recently
				 * used.
				 */
				i.remove();
				this.workerCache.add(entry);

				return entry.worker;

			}

		}

		/* cache miss */
		return null;

	}

	/**
	 * Ensures that the specified <code>TaskWorker</code> is cached.
	 * @param jobId The <code>UUID</code> of the job whose tasks are to be
	 * 		processed by <code>worker</code>.
	 * @param worker The <code>TaskWorker</code> to add to the cache.
	 */
	private synchronized void addWorkerToCache(UUID jobId, TaskWorker worker) {

		assert(jobId != null);
		assert(worker != null);

		/* First check to see if the worker for the specified job is already in
		 * the cache.
		 */
		if (this.getCachedTaskWorker(jobId) == null) {

			/* Add the worker to the cache. */
			this.workerCache.add(new WorkerCacheEntry(jobId, worker));

			/* If the cache has exceeded capacity, then remove the least
			 * recently used entry.
			 */
			assert(this.maxCachedWorkers > 0);

			while (this.workerCache.size() > this.maxCachedWorkers) {
				this.workerCache.remove(0);
			}

		}

	}

	/**
	 * Obtains the task worker to process tasks for the job with the specified
	 * <code>UUID</code>.
	 * @param jobId The <code>UUID</code> of the job to obtain the task worker
	 * 		for.
	 * @return The <code>TaskWorker</code> to process tasks for the job with
	 * 		the specified <code>UUID</code>, or <code>null</code> if the job
	 * 		is invalid or has already been completed.
	 */
	private TaskWorker getTaskWorker(UUID jobId) {

		/* First try to obtain the worker from the cache. */
		TaskWorker worker = this.getCachedTaskWorker(jobId);

		if (worker != null) {
			return worker;
		}

		/* If the task worker was not in the cache, then use the service to
		 * obtain the task worker.
		 */
		worker = this.service.getTaskWorker(jobId);

		/* If we were able to get the worker from the service, add it to the
		 * cache so that we don't have to call the service next time.
		 */
		if (worker != null) {
			this.addWorkerToCache(jobId, worker);
		}

		return worker;

	}

	/**
	 * Used to process tasks in threads.
	 * @author bkimmel
	 */
	private class Worker implements Runnable {

		/**
		 * Initializes the progress monitor to report to.
		 * @param monitor The <code>ProgressMonitor</code> to report
		 * 		the progress of the task to.
		 */
		public Worker(ProgressMonitor monitor) {
			this.monitor = monitor;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			this.monitor.notifyIndeterminantProgress();
			this.monitor.notifyStatusChanged("Requesting task...");

			TaskDescription taskDesc = service.requestTask();

			if (taskDesc != null) {

				this.monitor.notifyStatusChanged("Obtaining task worker...");
				TaskWorker worker = getTaskWorker(taskDesc.getJobId());

				if (worker == null) {
					this.monitor.notifyStatusChanged("Could not obtain worker...");
					this.monitor.notifyCancelled();
					return;
				}

				this.monitor.notifyStatusChanged("Performing task...");
				Object results = worker.performTask(taskDesc.getTask(), monitor);

				this.monitor.notifyStatusChanged("Submitting task results...");
				service.submitTaskResults(taskDesc.getJobId(), taskDesc.getTaskId(), results);

			} else { /* taskDesc == null */

				this.monitor.notifyStatusChanged("Idling...");

				try {
					Thread.sleep(idleTime);
				} catch (InterruptedException e) {
					// continue.
				}

			}

			this.monitor.notifyComplete();

		}

		/**
		 * The <code>ProgressMonitor</code> to report to.
		 */
		private final ProgressMonitor monitor;

	}

	/** The URL of the master. */
	private final String masterHost;

	/**
	 * The amount of time (in milliseconds) to idle when no task is available.
	 */
	private final long idleTime;

	/** The <code>Executor</code> to use to process tasks. */
	private final Executor executor;

	/**
	 * The <code>JobMasterService</code> to obtain tasks from and submit
	 * results to.
	 */
	private JobMasterService service = null;

	/**
	 * A list of recently used <code>TaskWorker</code>s and their associated
	 * job's <code>UUID</code>s, in order from least recently used to most
	 * recently used.
	 */
	private final List<WorkerCacheEntry> workerCache = new LinkedList<WorkerCacheEntry>();

	/**
	 * The maximum number of <code>TaskWorker</code>s to retain in the cache.
	 */
	private final int maxCachedWorkers = 5;

}
