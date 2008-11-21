/**
 *
 */
package org.jdcp.worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.jdcp.remote.JobService;
import org.selfip.bkimmel.util.StringUtil;

/**
 * @author brad
 *
 */
public final class FileCachingJobServiceClassLoaderStrategy extends
		CachingJobServiceClassLoaderStrategy {

	private final File directory;

	/**
	 * @param service
	 * @param jobId
	 * @param directory
	 */
	public FileCachingJobServiceClassLoaderStrategy(JobService service,
			UUID jobId, File directory) {
		super(service, jobId);
		this.directory = directory;
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("directory must be a directory.");
		}
	}

	/**
	 * @param service
	 * @param jobId
	 * @param directory
	 */
	public FileCachingJobServiceClassLoaderStrategy(JobService service,
			UUID jobId, String directory) {
		this(service, jobId, new File(directory));
	}

	/* (non-Javadoc)
	 * @see org.jdcp.worker.CachingJobServiceClassLoaderStrategy#cacheLookup(java.lang.String, byte[])
	 */
	@Override
	protected byte[] cacheLookup(String name, byte[] digest) {
		File file = getCacheEntryFile(name, digest, false);
		byte[] def = null;

		if (file.exists()) {
			try {
				FileInputStream stream = new FileInputStream(file);
				def = new byte[(int) file.length()];
				stream.read(def);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				def = null;
			}
		}

		return def;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.worker.CachingJobServiceClassLoaderStrategy#cacheStore(java.lang.String, byte[], byte[])
	 */
	@Override
	protected void cacheStore(String name, byte[] digest, byte[] def) {
		File file = getCacheEntryFile(name, digest, true);
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(def);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getCacheEntryFile(String name, byte[] digest, boolean createDirectory) {
		File entryDirectory = new File(directory, name.replace('.', '/'));
		if (createDirectory && !entryDirectory.isDirectory()) {
			entryDirectory.mkdirs();
		}
		return new File(entryDirectory, StringUtil.toHex(digest));
	}

}
