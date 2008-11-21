/**
 *
 */
package org.jdcp.worker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.jdcp.remote.JobService;
import org.selfip.bkimmel.util.StringUtil;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class PersistenceCachingJobServiceClassLoaderStrategy extends
		CachingJobServiceClassLoaderStrategy {

	private final URL baseUrl;
	private final PersistenceService persistenceService;

	/**
	 * @param service
	 * @param jobId
	 * @throws UnavailableServiceException
	 */
	public PersistenceCachingJobServiceClassLoaderStrategy(JobService service,
			UUID jobId) throws UnavailableServiceException {
		super(service, jobId);
		BasicService basicService = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
		this.baseUrl = basicService.getCodeBase();
		this.persistenceService = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
	}

	/**
	 * @param service
	 * @param jobId
	 * @param baseUrl
	 * @throws UnavailableServiceException
	 */
	public PersistenceCachingJobServiceClassLoaderStrategy(JobService service,
			UUID jobId, URL baseUrl) throws UnavailableServiceException {
		super(service, jobId);
		this.baseUrl = baseUrl;
		this.persistenceService = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
	}

	/* (non-Javadoc)
	 * @see org.jdcp.worker.CachingJobServiceClassLoaderStrategy#cacheLookup(java.lang.String, byte[])
	 */
	@Override
	protected byte[] cacheLookup(String name, byte[] digest) {

		try {

			URL url = getUrlForCacheEntry(name, digest);
			FileContents contents = persistenceService.get(url);

			/* If we get here, the digest is okay, so read the data. */
			InputStream in = contents.getInputStream();
			byte[] def = new byte[(int) contents.getLength()];

			in.read(def);

			return def;

		} catch (FileNotFoundException e) {

			/*
			 * Nothing to do.  This just means there is no cached item with the
			 * specified key.
			 */

		} catch (MalformedURLException e) {

			/*
			 * This should not happen.  getUrlForCacheEntry should ensure that the
			 * URL is valid.
			 */
			throw new UnexpectedException(e);

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}

	/* (non-Javadoc)
	 * @see org.jdcp.worker.CachingJobServiceClassLoaderStrategy#cacheStore(java.lang.String, byte[], byte[])
	 */
	@Override
	protected void cacheStore(String name, byte[] digest, byte[] def) {
		URL url = getUrlForCacheEntry(name, digest);
		write(url, def);
	}

	private URL getUrlForCacheEntry(String name, byte[] digest) {
		try {
			return new URL(baseUrl, name.replace('.', '/') + StringUtil.toHex(digest));
		} catch (MalformedURLException e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * Writes data to persistent storage.
	 * @param url The <code>URL</code> to associate with the data.
	 * @param data The data to write.
	 */
	private void write(URL url, byte[] data) {

		try {

			if (persistenceService.create(url, data.length) < data.length) {
				persistenceService.delete(url);
				throw new RuntimeException("Could not allocate enough space in persistence store.");
			}

			FileContents contents = persistenceService.get(url);
			OutputStream out = contents.getOutputStream(true);

			out.write(data);

		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException("Could not write data to persistence store.", e);

		}

	}

}
