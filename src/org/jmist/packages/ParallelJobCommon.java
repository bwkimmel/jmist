/**
 *
 */
package org.jmist.packages;

/**
 * @author bkimmel
 *
 */
public class ParallelJobCommon {

	public static final int MESSAGE_TAG_SUBMIT_JOB				= 1;
	public static final int MESSAGE_TAG_SUBMIT_JOB_ACK			= 2;
	public static final int MESSAGE_TAG_REQUEST_TASK			= 3;
	public static final int MESSAGE_TAG_ASSIGN_TASK				= 4;
	public static final int MESSAGE_TAG_SUBMIT_TASK_RESULTS		= 5;
	public static final int MESSAGE_TAG_IDLE					= 6;
	public static final int MESSAGE_TAG_REQUEST_TASK_WORKER		= 7;
	public static final int MESSAGE_TAG_PROVIDE_TASK_WORKER		= 8;

	/**
	 * Default constructor.  This class is non-instantiable.
	 */
	private ParallelJobCommon() {
		// nothing to do.
	}

}
