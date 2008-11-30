package org.jdcp.job;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;

/**
 * Provides access to host services on the server for a
 * <code>ParallelizableJob</code> running in a secure sandbox.
 * @author brad
 * @see org.jdcp.job.ParallelizableJob#initialize(Host)
 */
public interface Host {

	/**
	 * Creates a file in the job's working directory.
	 * @param path The path of the file to create, relative to the job's
	 * 		working directory.
	 * @return A <code>RandomAccessFile</code> for the newly created file.
	 * @throws IllegalArgumentException If the path is absolute or refers
	 * 		to the parent directory ("..").
	 */
	RandomAccessFile createRandomAccessFile(String path);

	/**
	 * Creates a file in the job's working directory.
	 * @param path The path of the file to create, relative to the job's
	 * 		working directory.
	 * @return A <code>FileOutputStream</code> for the newly created file.
	 * @throws IllegalArgumentException If the path is absolute or refers
	 * 		to the parent directory ("..").
	 */
	FileOutputStream createFileOutputStream(String path);

}
