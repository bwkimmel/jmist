/**
 *
 */
package org.selfip.bkimmel.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.selfip.bkimmel.util.StringUtil;

/**
 * A <code>LoginModule</code> that reads user names, password hashes, and roles
 * from a colon delimited file.
 *
 * The file must be formatted as follows.  There should be one line for each
 * user.  The line should have the form:
 *
 * name:password[:role1...:rolen]
 *
 * @author brad
 */
public final class FileLoginModule extends AbstractLoginModule {

	/** The default digest algorithm for storing hashed passwords. */
	private static final String DEFAULT_DIGEST_ALGORITHM = "MD5";

	/** The <code>File</code> in which passwords are stored. */
	private File passwordFile = null;

	/** The digest algorithm to use for storing hashed passwords. */
	private String digestAlgorithm = DEFAULT_DIGEST_ALGORITHM;

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.auth.AbstractLoginModule#initialize(java.util.Map, java.util.Map)
	 */
	@Override
	protected void initialize(Map<String, ?> sharedState, Map<String, ?> options) {
		String fileName = (String) options.get("file");
		passwordFile = new File(fileName);

		if (options.containsKey("digestAlgorithm")) {
			digestAlgorithm = (String) options.get("digestAlgorithm");
		} else {
			digestAlgorithm = DEFAULT_DIGEST_ALGORITHM;
		}
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login() throws LoginException {
		NameCallback user = new NameCallback("Login:");
		PasswordCallback pass = new PasswordCallback("Password:", false);

		try {
			callback(user, pass);

			BufferedReader reader = new BufferedReader(new FileReader(passwordFile));

			while (reader.ready()) {
				String[] fields = reader.readLine().split(":");
				if (fields[0].equals(user.getName())) {
					MessageDigest alg = MessageDigest.getInstance(digestAlgorithm);
					String loginPassword = new String(pass.getPassword());
					byte[] loginDigest = alg.digest(loginPassword.getBytes());
					byte[] realDigest = StringUtil.hexToByteArray(fields[1]);
					if (MessageDigest.isEqual(loginDigest, realDigest)) {
						addPrincipal(new UserPrincipal(fields[0]));
						for (int i = 2; i < fields.length; i++) {
							addPrincipal(new RolePrincipal(fields[i]));
						}
						return true;
					}
					break;
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			/* nothing to do. */
		} catch (UnsupportedCallbackException e) {
			/* nothing to do. */
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		throw new FailedLoginException();
	}

}
