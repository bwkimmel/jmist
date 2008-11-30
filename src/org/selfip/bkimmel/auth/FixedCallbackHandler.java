/**
 *
 */
package org.selfip.bkimmel.auth;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * A <code>CallbackHandler</code> that uses a predefined user name and
 * password.
 * @author brad
 */
public final class FixedCallbackHandler implements CallbackHandler {

	/** The user name. */
	private final String name;

	/** The password. */
	private final char[] password;

	/**
	 * Creates a new <code>FixedCallbackHandler</code>.
	 * @param name The user name.
	 * @param password The password.
	 */
	private FixedCallbackHandler(String name, char[] password) {
		this.name = name;
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	@Override
	public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				((NameCallback) callback).setName(name);
			} else if (callback instanceof PasswordCallback) {
				((PasswordCallback) callback).setPassword(password);
			} else {
				throw new UnsupportedCallbackException(callback);
			}
		}
	}

	/**
	 * Creates a <code>CallbackHandler</code> that provides the specified user
	 * name and password.
	 * @param name The user name.
	 * @param password The password.
	 * @return The new <code>CallbackHandler</code>.
	 */
	public static CallbackHandler forNameAndPassword(String name, char[] password) {
		return new FixedCallbackHandler(name, password);
	}

	/**
	 * Creates a <code>CallbackHandler</code> that provides the specified user
	 * name and password.
	 * @param name The user name.
	 * @param password The password.
	 * @return The new <code>CallbackHandler</code>.
	 */
	public static CallbackHandler forNameAndPassword(String name, String password) {
		return FixedCallbackHandler.forNameAndPassword(name, password.toCharArray());
	}

}
