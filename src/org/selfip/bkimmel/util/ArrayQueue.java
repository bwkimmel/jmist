/**
 *
 */
package org.selfip.bkimmel.util;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A fixed <code>Queue</code> backed by an array.
 * @author brad
 */
public final class ArrayQueue<T> extends AbstractQueue<T> {

	/** An array containing the items in the queue. */
	private final T[] items;

	/** The index of the next item in the queue. */
	private int next = 0;

	/**
	 * Creates an new <code>ArrayQueue</code> backed by the provided array.
	 * @param items The array of items with which to initialize the queue.
	 */
	public ArrayQueue(T[] items) {
		this.items = items;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			/** The index of the next item. */
			private int next = ArrayQueue.this.next;

			/* (non-Javadoc)
			 * @see java.util.Iterator#hasNext()
			 */
			public boolean hasNext() {
				return next < items.length;
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#next()
			 */
			public T next() {
				if (next >= items.length) {
					throw new NoSuchElementException();
				}
				return items[next++];
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#remove()
			 */
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return items.length - next;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(T e) {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#peek()
	 */
	@Override
	public T peek() {
		return next < items.length ? items[next] : null;
	}

	/* (non-Javadoc)
	 * @see java.util.Queue#poll()
	 */
	@Override
	public T poll() {
		return next < items.length ? items[next++] : null;
	}

}
