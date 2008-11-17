/**
 *
 */
package org.selfip.bkimmel.rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.selfip.bkimmel.io.AlternateClassLoaderObjectInputStream;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class Envelope<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 222718136239175900L;

	private final byte[] data;

	public Envelope(T obj) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream;
			objectStream = new ObjectOutputStream(byteStream);
			objectStream.flush();
			data = byteStream.toByteArray();
			objectStream.close();
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T contents() throws ClassNotFoundException {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
			return (T) objectStream.readObject();
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T contents(ClassLoader loader) throws ClassNotFoundException {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new AlternateClassLoaderObjectInputStream(
					byteStream, loader);
			return (T) objectStream.readObject();
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

}
