/**
 *
 */
package org.jmist.packages;

import java.util.LinkedList;
import java.util.Queue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;

import org.jmist.framework.ICommunicator;
import org.jmist.framework.IInboundMessage;
import org.jmist.framework.IOutboundMessage;

/**
 * An implementation of <code>ICommunicator</code> that uses sockets.
 * @author bkimmel
 */
public final class SocketCommunicator implements ICommunicator {

	/**
	 * Initializes the underlying socket to use.
	 * @param socket The underlying socket to use.
	 */
	public SocketCommunicator(Socket socket) {
		this.channel = socket.getChannel();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#createOutboundMessage()
	 */
	@Override
	public IOutboundMessage createOutboundMessage() {
		return new SocketOutboundMessage();
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#flush()
	 */
	@Override
	public void flush() {

		try {

			// Put the channel in blocking mode.
			this.channel.configureBlocking(true);

			// Write all outstanding buffers in the send queue.
			while (!this.sendq.isEmpty()) {
				this.channel.write(this.sendq.poll());
			}

			// Return the channel to non-blocking mode.
			this.channel.configureBlocking(false);

		} catch (IOException e) {

			// TODO do something other than just print out the exception.
			System.err.println(e);

		}

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.ISender#queue(org.jmist.framework.IOutboundMessage)
	 */
	@Override
	public void queue(IOutboundMessage message) {
		this.queue((SocketOutboundMessage) message);
	}

	/**
	 * Queues a message to be sent.
	 * @param message The message to be sent.
	 */
	private void queue(SocketOutboundMessage message) {
		message.queue(sendq);
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
	 * @see org.jmist.framework.IReceiver#receive()
	 */
	@Override
	public IInboundMessage receive() {

		try {

			// Put the socket channel in blocking mode.
			this.channel.configureBlocking(true);

			// Read from the socket until we have a complete message.
			while (this.recvq.isEmpty()) {
				this.readSocket();
			}

			// Put the socket channel back to non-blocking mode.
			this.channel.configureBlocking(false);

		} catch (IOException e) {

			// TODO Do something other than just print out the exception.
			System.err.println(e);

		}

		return this.recvq.poll();

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.IReceiver#peek()
	 */
	@Override
	public IInboundMessage peek() {

		try {

			// Read anything waiting on the socket.
			this.readSocket();

		} catch (IOException e) {

			// TODO Do something other than just print out the exception.
			System.err.println(e);

		}

		return this.recvq.peek();

	}

	/**
	 * Writes data waiting to be written to the socket.
	 * @throws IOException If the socket threw an <code>IOException</code>.
	 */
	public void writeSocket() throws IOException {

		ByteBuffer buffer;

		// begin send loop.
		while (!this.sendq.isEmpty()) {

			// Get the buffer at the front of the queue.
			buffer = this.sendq.peek();
			assert(buffer != null);

			// Write to the socket.
			this.channel.write(buffer);

			// If the entire buffer wasn't written, then the socket buffer
			// is full, so we've written all we can.
			if (buffer.hasRemaining())
				break;

			// The buffer has been written to the socket, so pop it off of the
			// queue.
			assert(!buffer.hasRemaining());
			this.sendq.remove();

		} // end send loop.

	}

	/**
	 * Reads data waiting on the socket.
	 * @throws IOException If the socket threw an <code>IOException</code>.
	 */
	private void readSocket() throws IOException {

		// begin message loop.
		while (true) {

			// If we haven't finished reading the message header, then
			// try to finish reading that.
			if (this.headerBuffer.hasRemaining()) {

				this.channel.read(this.headerBuffer);

				// If the header was not completely read, then stop here.
				if (this.headerBuffer.hasRemaining()) {
					break;
				}

			}

			// If we got to this point, then we're done reading the header.
			assert(!this.headerBuffer.hasRemaining());

			// Initialize the message buffer if it hasn't already been
			// initialized.
			if (this.messageBuffer == null) {

				// If we got to this point, then we got the message header, so
				// analyze it and move on to the extended header (if any).
				this.headerBuffer.flip();

				// Now that we have the header, make sure it really is a message
				// header.
				if (this.headerBuffer.getInt() != MESSAGE_MAGIC) {
					// TODO Throw an exception.
					assert(false);
				}

				int headerLength	= this.headerBuffer.getInt();
				int dataLength		= this.headerBuffer.getInt();
				int totalLength		= headerLength + dataLength;

				// Create a buffer to hold the message.
				this.messageBuffer	= ByteBuffer.allocate(totalLength);
				this.messageBuffer.clear();

				// Copy the header to the message buffer.
				this.headerBuffer.rewind();
				this.messageBuffer.put(this.headerBuffer);

			}

			assert(this.messageBuffer != null);

			// If we got to this point, then the header has been read and we
			// are reading the message.  If there is still data left to be read
			// for the message, then try to finish reading that.
			if (this.messageBuffer.hasRemaining()) {

				this.channel.read(this.messageBuffer);

				if (this.messageBuffer.hasRemaining()) {
					break;
				}

			}

			// If we got to this point, then we are done reading the message.
			assert(!this.messageBuffer.hasRemaining());
			this.messageBuffer.flip();

			// Create a new inbound message and add it to the queue.
			IInboundMessage message = new SocketInboundMessage(this.messageBuffer, this.channel.socket().getInetAddress().getCanonicalHostName());
			this.recvq.add(message);

			// Prepare for the next message.
			this.headerBuffer.clear();
			this.messageBuffer = null;

		} // end message loop.

	}

	/**
	 * A message that was received by a <code>SocketCommunicator</code>.
	 * @author bkimmel
	 */
	private final class SocketInboundMessage implements IInboundMessage {

		/**
		 * Creates a new inbound message.
		 * @param buffer The buffer containing the message.
		 * @param from The source of the message.
		 */
		public SocketInboundMessage(ByteBuffer buffer, String from) {

			assert(buffer.hasArray());

			this.from			= from;

			buffer.rewind();

			// Ensure that the buffer holds a valid message.
			if (buffer.getInt() != MESSAGE_MAGIC) {
				assert(false);
			}

			// Validate the length of the message.
			int headerLength	= buffer.getInt();
			int dataLength		= buffer.getInt();
			int totalLength		= headerLength + dataLength;

			assert(buffer.limit() == totalLength);

			// Get the message tag.
			this.tag			= buffer.getInt();

			// Read the extended header, if any (none at this point).

			// Seek to the beginning of the message.
			buffer.position(headerLength);

			// Create a stream to read the message.
			this.contents		= new ByteArrayInputStream(
					buffer.array(),
					buffer.arrayOffset() + buffer.position(),
					buffer.remaining()
			);

		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IInboundMessage#contents()
		 */
		@Override
		public InputStream contents() {
			return this.contents;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IInboundMessage#from()
		 */
		@Override
		public String from() {
			return this.from;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IMessage#tag()
		 */
		@Override
		public int tag() {
			return this.tag;
		}

		/**
		 * The <code>InputStream</code> that reads the contents of the
		 * message buffer.
		 */
		private final InputStream contents;

		/** The source of the message. */
		private final String from;

		/** The message tag. */
		private final int tag;

	}

	/**
	 * A message to be sent via a <code>SocketCommunicator</code>.
	 * @author bkimmel
	 */
	private final class SocketOutboundMessage implements IOutboundMessage {

		/* (non-Javadoc)
		 * @see org.jmist.framework.IOutboundMessage#contents()
		 */
		@Override
		public OutputStream contents() {
			return this.contents;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IMessage#tag()
		 */
		@Override
		public int tag() {
			return this.tag;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IOutboundMessage#tag(int)
		 */
		@Override
		public void tag(int value) {
			this.tag = value;
		}

		/**
		 * Adds the message to the message buffer send queue.
		 * @param sendq The queue to add the message buffers to.
		 */
		public void queue(Queue<ByteBuffer> sendq) {

			byte[]		msg				= this.contents.toByteArray();
			ByteBuffer	headerBuffer	= ByteBuffer.allocate(MESSAGE_HEADER_SIZE);
			ByteBuffer	messageBuffer	= ByteBuffer.wrap(msg);

			// Write the header.
			headerBuffer.putInt(MESSAGE_MAGIC);
			headerBuffer.putInt(MESSAGE_HEADER_SIZE);
			headerBuffer.putInt(msg.length);
			headerBuffer.putInt(this.tag);

			// Queue the message.
			sendq.add(headerBuffer);
			sendq.add(messageBuffer);

		}

		/** The message stream. */
		private final ByteArrayOutputStream contents = new ByteArrayOutputStream();

		/** The message tag. */
		private int tag = 0;

	}

	/** The size of the primary message header. */
	private static final int MESSAGE_HEADER_SIZE			= 16;

	/** The magic value identifying a stream of bytes as a mist message. */
	private static final int MESSAGE_MAGIC					= 0x4D495354;

	/** The underlying socket channel. */
	private final SocketChannel channel;

	/** The inbound message queue. */
	private final Queue<IInboundMessage> recvq = new LinkedList<IInboundMessage>();

	/** The outbound message queue. */
	private final Queue<ByteBuffer> sendq = new LinkedList<ByteBuffer>();

	/** The header of the message currently being processed. */
	private final ByteBuffer headerBuffer = ByteBuffer.allocate(MESSAGE_HEADER_SIZE);

	/** The message buffer of the message currently being processed. */
	private ByteBuffer messageBuffer = null;

}
