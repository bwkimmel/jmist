/**
 *
 */
package org.selfip.bkimmel.rmi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import org.selfip.bkimmel.util.ClassUtil;

/**
 * @author brad
 *
 */
public final class ClassLoaderServer implements ClassLoaderService {

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.rmi.ClassLoaderService#getClassDefinition(java.lang.String)
	 */
	public byte[] getClassDefinition(String name) throws RemoteException {

		try {

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Class<?> cl = Class.forName(name);

			ClassUtil.writeClassToStream(cl, out);

			out.flush();
			return out.toByteArray();

		} catch (ClassNotFoundException e) {

			return null;

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		}

	}

}
