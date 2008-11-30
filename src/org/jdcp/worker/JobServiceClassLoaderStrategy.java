/**
 *
 */
package org.jdcp.worker;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.UUID;

import org.jdcp.remote.JobService;
import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;

/**
 * @author brad
 *
 */
public final class JobServiceClassLoaderStrategy implements ClassLoaderStrategy {

	private final JobService service;
	private final UUID jobId;

	public JobServiceClassLoaderStrategy(JobService service, UUID jobId) {
		this.service = service;
		this.jobId = jobId;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {

		try {

			byte[] def = service.getClassDefinition(name, jobId);

			if (def != null) {
				return ByteBuffer.wrap(def);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}

}
