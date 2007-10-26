/**
 * 
 */
package org.jmist.packages;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IProgressMonitor;
import org.jmist.framework.base.ServerJob;

/**
 * A job that processes manages the distribution of tasks for parallelizable
 * jobs.
 * @author bkimmel
 */
public final class MasterJob extends ServerJob {
	
	/* (non-Javadoc)
	 * @see org.jmist.framework.base.NetworkJob#onMessageReceived(org.jmist.framework.IInboundMessage, org.jmist.framework.ICommunicator, org.jmist.framework.IProgressMonitor)
	 */
	@Override
	protected boolean onMessageReceived(IInboundMessage msg, ICommunicator comm,
			IProgressMonitor monitor) {

		switch (msg.tag()) {

		case ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK:
			this.processRequestTask(msg, comm, monitor);
			break;
			
		case ParallelJobCommon.MESSAGE_TAG_REQUEST_TASK_WORKER:
			this.processRequestTaskWorker(msg, comm, monitor);
			break;
			
		case ParallelJobCommon.MESSAGE_TAG_SUBMIT_JOB:
			this.processSubmitJob(msg, comm, monitor);
			break;
			
		case ParallelJobCommon.MESSAGE_TAG_SUBMIT_TASK_RESULTS:
			this.processSubmitTaskResults(msg, comm, monitor);
			break;
			
		default:
			return false;
			
		}
		
		return true;
		
	}
	
	private void processRequestTask(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {
		
	}
	
	private void processRequestTaskWorker(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {
		
	}
	
	private void processSubmitTaskResults(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {
		
	}

	private void processSubmitJob(IInboundMessage msg, ICommunicator comm, IProgressMonitor monitor) {
		
	}

}
