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
public abstract class CachingJobServiceClassLoaderStrategy implements ClassLoaderStrategy {

	private final JobService service;
	private final UUID jobId;

	protected CachingJobServiceClassLoaderStrategy(JobService service, UUID jobId) {
		this.service = service;
		this.jobId = jobId;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public final ByteBuffer getClassDefinition(String name) {

		try {

			byte[] digest = service.getClassDigest(name, jobId);
			byte[] def = cacheLookup(name, digest);

			if (def == null) {
				def = service.getClassDefinition(name, jobId);
				if (def != null) {
					cacheStore(name, digest, def);
				}
			}

			if (def != null) {
				return ByteBuffer.wrap(def);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}

	protected abstract byte[] cacheLookup(String name, byte[] digest);

	protected abstract void cacheStore(String name, byte[] digest, byte[] def);

}
