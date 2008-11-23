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
 * Represents an object that is serialized.  When constructed using the
 * constructor, the object remains deserialized.  However, when constructed
 * by deserialization, it remains in a serialized state until explicitly
 * asked to deserialize (via the {@link #deserialize()} method).
 * @author brad
 */
public final class Serialized<T> implements Serializable {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 222718136239175900L;

	/** The serialized object. */
	private final byte[] data;

	/** The deserialized object. */
	private transient T object;

	/**
	 * Initializes the <code>Serialized</code> object.
	 * @param obj
	 */
	public Serialized(T obj) {
		this.object = obj;
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
			objectStream.writeObject(obj);
			objectStream.flush();
			data = byteStream.toByteArray();
			objectStream.close();
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * Determines if the object has been deserialized.
	 * @return A value indicating if the object has been deserialized.
	 */
	public boolean isDeserialized() {
		return (object != null);
	}

	/**
	 * Returns the deserialized object.
	 * @return The deserialized object.
	 * @throws IllegalStateException If the object has not been deserialized.
	 */
	public T get() {
		if (!isDeserialized()) {
			throw new IllegalStateException("Object not deserialized.");
		}
		return object;
	}

	/**
	 * Returns the deserialized object.
	 * @param deserialize A value indicating whether the object should be
	 * 		deserialized if it is not already.
	 * @return The deserialized object.
	 * @throws ClassNotFoundException If a required class could not be found
	 * 		during deserialization.
	 * @throws IllegalStateException If the object has not been deserialized
	 * 		and <code>deserialize</code> is false.
	 */
	public T get(boolean deserialize) throws ClassNotFoundException {
		if (!isDeserialized()) {
			if (deserialize) {
				deserialize();
			} else {
				throw new IllegalStateException("Object not deserialized.");
			}
		}
		return object;
	}

	/**
	 * Deserializes the serialized object.
	 * @return The deserialized object.
	 * @throws ClassNotFoundException If a required class could not be found
	 * 		during deserialization.
	 */
	@SuppressWarnings("unchecked")
	public T deserialize() throws ClassNotFoundException {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
			object = (T) objectStream.readObject();
			return object;
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

	/**
	 * Deserializes the serialized object, using the specified
	 * <code>ClassLoader</code> to load classes as necessary.
	 * @param loader The <code>ClassLoader</code> to use to load classes.
	 * @return The deserialized object.
	 * @throws ClassNotFoundException If a required class could not be found
	 * 		during deserialization.
	 */
	@SuppressWarnings("unchecked")
	public T deserialize(ClassLoader loader) throws ClassNotFoundException {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new AlternateClassLoaderObjectInputStream(
					byteStream, loader);
			object = (T) objectStream.readObject();
			return object;
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

}
