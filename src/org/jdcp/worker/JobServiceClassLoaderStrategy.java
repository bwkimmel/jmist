/**
 *
 */
package org.jdcp.worker;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.util.UUID;

import org.jdcp.remote.JobService;
import org.selfip.bkimmel.jnlp.PersistenceCheckedCache;
import org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy;
import org.selfip.bkimmel.util.UnexpectedException;
import org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy.DigestLookup;
import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;
import org.selfip.bkimmel.util.classloader.StrategyClassLoader;

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

	public static ClassLoader createCachingClassLoader(JobService service, UUID jobId) {
		return createCachingClassLoader(service, jobId, null);
	}

	public static ClassLoader createCachingClassLoader(
			final JobService service, final UUID jobId, ClassLoader parent) {
		ClassLoaderStrategy fallback = new JobServiceClassLoaderStrategy(
				service, jobId);
		ClassLoaderStrategy strategy = new CheckedCacheClassLoaderStrategy(
				new PersistenceCheckedCache(), new DigestLookup() {

					public byte[] getDigest(String name) {
						try {
							return service.getClassDigest(name, jobId);
						} catch (Exception e) {
							e.printStackTrace();
							throw new UnexpectedException(e);
						}
					}

				}, fallback);

		return new StrategyClassLoader(strategy, parent);
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
