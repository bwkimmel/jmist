/**
 * 
 */
package org.jmist.packages;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IOutboundMessage;

/**
 * @author bkimmel
 *
 */
public final class SocketCommunicator implements ICommunicator {

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#createOutboundMessage()
	 */
	@Override
	public IOutboundMessage createOutboundMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#flush()
	 */
	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#queue(org.jmist.framework.IOutboundMessage)
	 */
	@Override
	public void queue(IOutboundMessage message) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#send(org.jmist.framework.IOutboundMessage)
	 */
	@Override
	public void send(IOutboundMessage message) {
		this.queue(message);
		this.flush();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IReceiver#peek()
	 */
	@Override
	public IInboundMessage peek() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IReceiver#receive(long)
	 */
	@Override
	public IInboundMessage receive(long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IReceiver#receive()
	 */
	@Override
	public IInboundMessage receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
