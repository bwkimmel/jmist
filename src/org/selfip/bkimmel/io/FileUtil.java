/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.File;

/**
 * File I/O utility methods.
 * @author brad
 */
public final class FileUtil {

	/**
	 * Recursively removes all entries from a directory.
	 * @param directory The directory to clear.
	 * @return A value indicating whether the directory was successfully
	 * 		cleared.  If unsuccessful, the directory may have been partially
	 * 		cleared.
	 */
	public static boolean clearDirectory(File directory) {
		if (directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (!deleteRecursive(file)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Recursively removes a file or a directory and all its subdirectories.
	 * @param file The file or directory to delete.
	 * @return A value indicating whether the file directory was successfully
	 * 		removed.  If unsuccessful, the directory may have been partially
	 * 		cleared.
	 */
	public static boolean deleteRecursive(File file) {
		return clearDirectory(file) && file.delete();
	}

	/** Declared private to prevent this class from being instantiated. */
	private FileUtil() {}

}
