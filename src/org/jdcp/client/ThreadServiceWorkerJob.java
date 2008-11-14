/**
 *
 */
package org.jdcp.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;
import org.jdcp.remote.JobService;
import org.selfip.bkimmel.jobs.Job;
import org.selfip.bkimmel.progress.ProgressMonitor;

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
	 * @param maxConcurrentWorkers The maximum number of concurrent worker
	 * 		threads to allow.
	 * @param executor The <code>Executor</code> to use to process tasks.
	 */
	public ThreadServiceWorkerJob(String masterHost, long idleTime, int maxConcurrentWorkers, Executor executor) {

		assert(maxConcurrentWorkers > 0);

		this.masterHost = masterHost;
		this.idleTime = idleTime;
		this.executor = executor;
		this.workerSlot = new Semaphore(maxConcurrentWorkers, true);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Job#go(org.jmist.framework.ProgressMonitor)
	 */
	public boolean go(ProgressMonitor monitor) {

		try {

			int workerIndex = 0;

			monitor.notifyIndeterminantProgress();
			monitor.notifyStatusChanged("Looking up master...");

			this.registry = LocateRegistry.getRegistry(this.masterHost);
			this.initializeService();

			while (!monitor.isCancelPending()) {

				this.workerSlot.acquire();

				String workerTitle = String.format("Worker (%d)", ++workerIndex);

				monitor.notifyStatusChanged("Queueing worker process...");
				this.executor.execute(new Worker(monitor.createChildProgressMonitor(workerTitle)));

			}

			monitor.notifyStatusChanged("Cancelled.");

		} catch (Exception e) {

			monitor.notifyStatusChanged("Exception: " + e.toString());

			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();

		} finally {

			monitor.notifyCancelled();

		}

		return false;

	}

	/**
	 * Attempt to initialize a connection to the master service.
	 * @return A value indicating whether the operation succeeded.
	 */
	private boolean initializeService() {
		try {
			this.service = (JobService) this.registry.lookup("JobMasterService");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * An entry in the <code>TaskWorker</code> cache.
	 * @author bkimmel
	 */
	private static class WorkerCacheEntry {

		/**
		 * Initializes the cache entry.
		 * @param jobId The <code>UUID</code> of the job that the
		 * 		<code>TaskWorker</code> processes tasks for.
		 */
		public WorkerCacheEntry(UUID jobId) {
			this.jobId = jobId;
			this.workerGuard.writeLock().lock();
		}

		/**
		 * Returns a value indicating if this <code>WorkerCacheEntry</code>
		 * is to be used for the job with the specified <code>UUID</code>.
		 * @param jobId The job's <code>UUID</code> to test.
		 * @return A value indicating if this <code>WorkerCacheEntry</code>
		 * 		applies to the specified job.
		 */
		public boolean matches(UUID jobId) {
			return this.jobId.equals(jobId);
		}

		/**
		 * Sets the <code>TaskWorker</code> to use.  This method may only be
		 * called once.
		 * @param worker The <code>TaskWorker</code> to use for matching
		 * 		jobs.
		 */
		public synchronized void setWorker(TaskWorker worker) {

			/* Set the worker. */
			this.worker = worker;

			/* Release the lock. */
			this.workerGuard.writeLock().unlock();

		}

		/**
		 * Gets the <code>TaskWorker</code> to use to process tasks for the
		 * matching job.  This method will wait for <code>setWorker</code>
		 * to be called if it has not yet been called.
		 * @return The <code>TaskWorker</code> to use to process tasks for
		 * 		the matching job.
		 * @see {@link #setWorker(TaskWorker)}.
		 */
		public TaskWorker getWorker() {

			this.workerGuard.readLock().lock();
			TaskWorker worker = this.worker;
			this.workerGuard.readLock().unlock();

			return worker;

		}

		/**
		 * The <code>UUID</code> of the job that the <code>TaskWorker</code>
		 * processes tasks for.
		 */
		private final UUID jobId;

		/**
		 * The cached <code>TaskWorker</code>.
		 */
		private TaskWorker worker;

		/**
		 * The <code>ReadWriteLock</code> to use before reading from or writing
		 * to the <code>worker</code> field.
		 */
		private final ReadWriteLock workerGuard = new ReentrantReadWriteLock();

	}

	/**
	 * Searches for the <code>WorkerCacheEntry</code> matching the job with the
	 * specified <code>UUID</code>.
	 * @param jobId The <code>UUID</code> of the job whose
	 * 		<code>WorkerCacheEntry</code> to search for.
	 * @return The <code>WorkerCacheEntry</code> corresponding to the job with
	 * 		the specified <code>UUID</code>, or <code>null</code> if the
	 * 		no such entry exists.
	 */
	private WorkerCacheEntry getCacheEntry(UUID jobId) {

		assert(jobId != null);

		synchronized (this.workerCache) {

			Iterator<WorkerCacheEntry> i = this.workerCache.iterator();

			/* Search for the worker for the specified job. */
			while (i.hasNext()) {

				WorkerCacheEntry entry = i.next();

				if (entry.matches(jobId)) {

					/* Remove the entry and re-insert it at the end of the list.
					 * This will ensure that when an item is removed from the list,
					 * the item that is removed will always be the least recently
					 * used.
					 */
					i.remove();
					this.workerCache.add(entry);

					return entry;

				}

			}

			/* cache miss */
			return null;

		}

	}

	/**
	 * Removes the specified entry from the task worker cache.
	 * @param entry The <code>WorkerCacheEntry</code> to remove.
	 */
	private void removeCacheEntry(WorkerCacheEntry entry) {

		assert(entry != null);

		synchronized (this.workerCache) {
			this.workerCache.remove(entry);
		}

	}

	/**
	 * Removes least recently used entries from the task worker cache until
	 * there are at most <code>this.maxCachedWorkers</code> entries.
	 */
	private void removeOldCacheEntries() {

		synchronized (this.workerCache) {

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
	 * @throws RemoteException
	 */
	private TaskWorker getTaskWorker(UUID jobId) throws RemoteException {

		WorkerCacheEntry entry = null;
		boolean hit;

		synchronized (this.workerCache) {

			/* First try to get the worker from the cache. */
			entry = this.getCacheEntry(jobId);
			hit = (entry != null);

			/* If there was no matching cache entry, then add a new entry to
			 * the cache.
			 */
			if (!hit) {
				entry = new WorkerCacheEntry(jobId);
				this.workerCache.add(entry);
			}

		}

		if (hit) {

			/* We found a cache entry, so get the worker from that entry. */
			return entry.getWorker();

		} else { /* cache miss */

			/* The task worker was not in the cache, so use the service to
			 * obtain the task worker.
			 */
			TaskWorker worker = this.service.getTaskWorker(jobId);
			entry.setWorker(worker);

			/* If we couldn't get a worker from the service, then don't keep
			 * the cache entry.
			 */
			if (worker == null) {
				this.removeCacheEntry(entry);
			}

			/* Clean up the cache. */
			this.removeOldCacheEntries();

			return worker;

		}

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
		public void run() {

			try {

				this.monitor.notifyIndeterminantProgress();
				this.monitor.notifyStatusChanged("Requesting task...");

				if (service != null) {

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

					} else {

						this.monitor.notifyStatusChanged("Idling...");
						this.idle();

					}

					this.monitor.notifyComplete();

				} else {

					this.monitor.notifyStatusChanged("No service at " + ThreadServiceWorkerJob.this.masterHost);
					this.waitForService();
					this.monitor.notifyCancelled();

				}

			} catch (RemoteException e) {

				System.err.println("Remote exception: " + e.toString());
				e.printStackTrace();

				this.monitor.notifyStatusChanged("Failed to communicate with master.");
				this.waitForService();

				this.monitor.notifyCancelled();

			} finally {

				workerSlot.release();

			}

		}

		/**
		 * Blocks until a successful attempt is made to reconnect to the
		 * service.  This method will idle for some time between attempts.
		 */
		private void waitForService() {
			synchronized (registry) {
				while (!initializeService()) {
					this.idle();
				}
			}
		}

		/**
		 * Idles for a period of time before finishing the task.
		 */
		private void idle() {
			try {
				Thread.sleep(idleTime);
			} catch (InterruptedException e) {
				// continue.
			}
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
	 * The <code>Semaphore</code> to use to throttle <code>Worker</code>
	 * threads.
	 */
	private final Semaphore workerSlot;

	/**
	 * The <code>Registry</code> to obtain the service from.
	 */
	private Registry registry = null;

	/**
	 * The <code>JobMasterService</code> to obtain tasks from and submit
	 * results to.
	 */
	private JobService service = null;

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
