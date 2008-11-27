/**
 *
 */
package org.selfip.bkimmel.util.args;

import java.io.File;
import java.util.Queue;

/**
 * @author brad
 *
 */
public class FileFieldOption<T> extends AbstractFieldOption<T> {

	private final boolean mustExist;

	/**
	 * @param fieldName
	 */
	public FileFieldOption(String fieldName) {
		this(fieldName, false);
	}

	/**
	 * @param fieldName
	 * @param mustExist
	 */
	public FileFieldOption(String fieldName, boolean mustExist) {
		super(fieldName);
		this.mustExist = mustExist;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.AbstractFieldOption#getOptionValue(java.util.Queue)
	 */
	@Override
	protected Object getOptionValue(Queue<String> args) {
		String name = args.remove();
		File file = new File(name);
		if (mustExist && !file.isFile()) {
			System.err.println("File does not exist: '" + name + "'");
			System.exit(1);
		}
		return file;
	}

}
