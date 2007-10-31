/**
 * 
 */
package org.jmist.packages;

import org.jmist.framework.AbstractParallelizableJob;
import org.jmist.framework.TaskWorker;
import org.jmist.framework.reporting.ProgressMonitor;

/**
 * @author bkimmel
 *
 */
public final class PhotometerParallelizableJob extends
		AbstractParallelizableJob {

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#getNextTask()
	 */
	@Override
	public Object getNextTask() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#submitTaskResults(java.lang.Object, java.lang.Object, org.jmist.framework.reporting.ProgressMonitor)
	 */
	@Override
	public void submitTaskResults(Object task, Object results,
			ProgressMonitor monitor) {
		

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ParallelizableJob#worker()
	 */
	@Override
	public TaskWorker worker() {
		// TODO Auto-generated method stub
		return null;
	}

}
