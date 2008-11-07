/**
 * 
 */
package org.selfip.bkimmel.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.selfip.bkimmel.util.ClassUtil;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * TODO: Implement this class.
 * @author brad
 * 
 */
public final class ClassDigestObjectOutputStream extends ObjectOutputStream {

	/**
	 * @throws IOException
	 * @throws SecurityException
	 */
	public ClassDigestObjectOutputStream() throws IOException,
			SecurityException {
		super();
	}

	/**
	 * @param out
	 * @throws IOException
	 */
	public ClassDigestObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}

	/* (non-Javadoc)
	 * @see java.io.ObjectOutputStream#annotateClass(java.lang.Class)
	 */
	@Override
	protected void annotateClass(Class<?> cl) throws IOException {
		
		MessageDigest digest = newDigest();
		ClassUtil.getClassDigest(cl, digest);
		
		write(digest.digest());
		
	}
	
	private MessageDigest newDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new UnexpectedException(e);
		}
	}

}
