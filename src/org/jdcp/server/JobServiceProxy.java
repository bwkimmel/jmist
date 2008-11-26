/**
 *
 */
package org.jdcp.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.UUID;

import javax.security.auth.Subject;

import org.jdcp.job.ParallelizableJob;
import org.jdcp.job.TaskDescription;
import org.jdcp.job.TaskWorker;
import org.jdcp.remote.JobService;
import org.selfip.bkimmel.rmi.Serialized;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class JobServiceProxy extends UnicastRemoteObject implements JobService {

	/**
	 *
	 */
	private static final long serialVersionUID = -3663995122172056330L;

	private final Subject user;
	private final JobService service;

	/**
	 * @param user
	 * @param server
	 */
	public JobServiceProxy(Subject user, JobService service) throws RemoteException {
		this.user = user;
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#cancelJob(java.util.UUID)
	 */
	@Override
	public void cancelJob(final UUID jobId) throws IllegalArgumentException,
			SecurityException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("job.cancel"));
					service.cancelJob(jobId);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#createJob(java.lang.String)
	 */
	@Override
	public UUID createJob(final String description) throws SecurityException,
			RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<UUID>() {

				@Override
				public UUID run() throws Exception {
					AccessController.checkPermission(new ServerPermission("job.submit"));
					return service.createJob(description);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDefinition(java.lang.String, java.util.UUID)
	 */
	@Override
	public byte[] getClassDefinition(final String name, final UUID jobId)
			throws SecurityException, RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<byte[]>() {

				@Override
				public byte[] run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					return service.getClassDefinition(name, jobId);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String, java.util.UUID)
	 */
	@Override
	public byte[] getClassDigest(final String name, final UUID jobId)
			throws SecurityException, RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<byte[]>() {

				@Override
				public byte[] run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					return service.getClassDigest(name, jobId);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getClassDigest(java.lang.String)
	 */
	@Override
	public byte[] getClassDigest(final String name) throws SecurityException,
			RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<byte[]>() {

				@Override
				public byte[] run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					return service.getClassDigest(name);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#getTaskWorker(java.util.UUID)
	 */
	@Override
	public Serialized<TaskWorker> getTaskWorker(final UUID jobId)
			throws IllegalArgumentException, SecurityException, RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Serialized<TaskWorker>>() {

				@Override
				public Serialized<TaskWorker> run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					return service.getTaskWorker(jobId);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#requestTask()
	 */
	@Override
	public TaskDescription requestTask() throws SecurityException,
			RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<TaskDescription>() {

				@Override
				public TaskDescription run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					return service.requestTask();
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, byte[])
	 */
	@Override
	public void setClassDefinition(final String name, final byte[] def)
			throws SecurityException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("admin.setClassDefinition"));
					service.setClassDefinition(name, def);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setClassDefinition(java.lang.String, java.util.UUID, byte[])
	 */
	@Override
	public void setClassDefinition(final String name, final UUID jobId, final byte[] def)
			throws IllegalArgumentException, SecurityException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("job.setClassDefinition"));
					service.setClassDefinition(name, jobId, def);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setIdleTime(int)
	 */
	@Override
	public void setIdleTime(final int idleSeconds) throws IllegalArgumentException,
			SecurityException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("admin.setIdleTime"));
					service.setIdleTime(idleSeconds);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#setJobPriority(java.util.UUID, int)
	 */
	@Override
	public void setJobPriority(final UUID jobId, final int priority)
			throws IllegalArgumentException, SecurityException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("admin.setJobPriority"));
					service.setJobPriority(jobId, priority);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitJob(org.selfip.bkimmel.rmi.Serialized, java.util.UUID)
	 */
	@Override
	public void submitJob(final Serialized<ParallelizableJob> job, final UUID jobId)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("job.submit"));
					service.submitJob(job, jobId);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitJob(org.selfip.bkimmel.rmi.Serialized, java.lang.String)
	 */
	@Override
	public UUID submitJob(final Serialized<ParallelizableJob> job, final String description)
			throws SecurityException, ClassNotFoundException, RemoteException {

		try {
			return Subject.doAsPrivileged(user, new PrivilegedExceptionAction<UUID>() {

				@Override
				public UUID run() throws Exception {
					AccessController.checkPermission(new ServerPermission("job.submit"));
					return service.submitJob(job, description);
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.jdcp.remote.JobService#submitTaskResults(java.util.UUID, int, org.selfip.bkimmel.rmi.Serialized)
	 */
	@Override
	public void submitTaskResults(final UUID jobId, final int taskId,
			final Serialized<Object> results) throws SecurityException,
			ClassNotFoundException, RemoteException {

		try {
			Subject.doAsPrivileged(user, new PrivilegedExceptionAction<Object>() {

				@Override
				public Object run() throws Exception {
					AccessController.checkPermission(new ServerPermission("worker"));
					service.submitTaskResults(jobId, taskId, results);
					return null;
				}

			}, null);
		} catch (PrivilegedActionException e) {
			if (e.getException() instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) e.getException();
			} else if (e.getException() instanceof SecurityException) {
				throw (SecurityException) e.getException();
			} else if (e.getException() instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) e.getException();
			} else if (e.getException() instanceof RemoteException) {
				throw (RemoteException) e.getException();
			} else {
				throw new UnexpectedException(e);
			}
		}

	}

}
