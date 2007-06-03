/**
 *
 */
package org.jmist.util;

/**
 * Represents an container for simulating the passing
 * of an argument by reference.
 * @author bkimmel
 */
public final class ReferenceArgument<T> {

	/**
	 * Default constructor.
	 * Initializes the argument value to null.
	 */
	public ReferenceArgument() {
		this.value = null;
	}

	/**
	 * Initializes the argument value.
	 * @param value The initial value of the argument.
	 */
	public ReferenceArgument(T value) {
		this.value = value;
	}

	/**
	 * Gets the value of this argument.
	 * @return The value of this argument.
	 */
	public T get() {
		return this.value;
	}

	/**
	 * Safely sets the value of a reference argument.  If the
	 * argument to be set is null, then nothing happens.
	 * @param arg The argument whose value is to be set (may be null).
	 * @param value The new value for the argument.
	 */
	public static<T> void set(ReferenceArgument<T> arg, T value) {
		if (arg != null) {
			arg.value = value;
		}
	}

	/** The argument value. */
	private T value;

}
