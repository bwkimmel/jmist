/**
 *
 */
package org.selfip.bkimmel.auth;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * A partial implementation of a <code>LoginModule</code> for convenience.
 * @author brad
 *
 */
public abstract class AbstractLoginModule implements LoginModule {

	/** A value indicating if a callback handler is required. */
	private final boolean requireCallbackHandler;

	/** The <code>Subject</code> being authenticated. */
	private Subject subject;

	/** The <code>CallbackHandler</code> to use to obtain login information. */
	private CallbackHandler callbackHandler;

	/**
	 * The collection of <code>Principal</code>s to populate the subject with
	 * when the login is committed.
	 */
	private final Collection<Principal> principals = new ArrayList<Principal>();

	/**
	 * The collection of public credentials to populate the subject with when
	 * the login is committed.
	 */
	private final Collection<Object> publicCredentials = new ArrayList<Object>();

	/**
	 * The collection of private credentials to populate the subject with when
	 * the login is committed.
	 */
	private final Collection<Object> privateCredentials = new ArrayList<Object>();

	/**
	 * Initializes this <code>AbstractLoginModule</code>.
	 */
	public AbstractLoginModule() {
		this(true);
	}

	/**
	 * Initializes this <code>AbstractLoginModule</code>.
	 * @param requireCallbackHandler A value indicating whether a callback
	 * 		handler is required.
	 */
	public AbstractLoginModule(boolean requireCallbackHandler) {
		this.requireCallbackHandler = requireCallbackHandler;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	@Override
	public boolean abort() {
		this.principals.clear();
		this.publicCredentials.clear();
		this.privateCredentials.clear();
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	@Override
	public boolean commit() {
		subject.getPrincipals().addAll(principals);
		subject.getPrivateCredentials().addAll(privateCredentials);
		subject.getPublicCredentials().addAll(publicCredentials);
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.principals.clear();
		this.publicCredentials.clear();
		this.privateCredentials.clear();
		this.initialize(sharedState, options);
	}

	/**
	 * Initializes this <code>LoginModule</code>.
	 * @param sharedState Shared settings.
	 * @param options Settings specific to this <code>LoginModule</code>.
	 */
	protected abstract void initialize(Map<String, ?> sharedState, Map<String, ?> options);

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	@Override
	public boolean logout() {
		subject.getPrincipals().removeAll(principals);
		subject.getPrivateCredentials().removeAll(privateCredentials);
		subject.getPublicCredentials().removeAll(publicCredentials);
		principals.clear();
		privateCredentials.clear();
		publicCredentials.clear();
		return true;
	}

	/**
	 * Issues the specified callbacks to acquire login information.
	 * @param callbacks The collection of callbacks to issue.
	 * @throws IOException If an I/O exception was thrown by the callback
	 * 		handler.
	 * @throws UnsupportedCallbackException If a requested callback is not
	 * 		supported by the callback handler.
	 * @throws LoginException If a callback handler is required (see
	 * 		{@link #AbstractLoginModule(boolean)}) but was not provided.
	 */
	protected void callback(Callback... callbacks) throws IOException,
			UnsupportedCallbackException, LoginException {
		if (callbackHandler != null) {
			callbackHandler.handle(callbacks);
		} else if (requireCallbackHandler) {
			throw new LoginException("Login module requires a callback handler");
		}
	}

	/**
	 * Adds the specified principal to the subject when the login is committed.
	 * @param principal The <code>Principal</code> to add to the subject.
	 */
	protected void addPrincipal(Principal principal) {
		principals.add(principal);
	}

	/**
	 * Adds the specified public credential to the subject when the login is
	 * committed.
	 * @param credential The credential to add.
	 */
	protected void addPublicCredential(Object credential) {
		publicCredentials.add(credential);
	}

	/**
	 * Adds the specified private credential to the subject when the login is
	 * committed.
	 * @param credential The credential to add.
	 */
	protected void addPrivateCredential(Object credential) {
		privateCredentials.add(credential);
	}

}
