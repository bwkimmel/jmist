/**
 *
 */
package org.selfip.bkimmel.rmi;

import java.net.URL;
import java.nio.ByteBuffer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;

/**
 * @author brad
 *
 */
public final class RMIClassLoaderStrategy implements ClassLoaderStrategy {

	private final ClassLoaderService service;
	
	public RMIClassLoaderStrategy() throws RemoteException, NotBoundException {
		this(getClassLoaderService());
	}

	public RMIClassLoaderStrategy(URL baseUrl) throws RemoteException, NotBoundException {
		this(getClassLoaderService(baseUrl));
	}

	public RMIClassLoaderStrategy(ClassLoaderService service) {
		this.service = service;
	}
	
	public static ClassLoaderService getClassLoaderService() throws RemoteException, NotBoundException {
		try {
			BasicService basic = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			return getClassLoaderService(basic.getCodeBase());
		} catch (UnavailableServiceException e) {
			throw new IllegalStateException("javax.jnlp.BasicService is required.", e);
		}
	}
	
	public static ClassLoaderService getClassLoaderService(URL baseUrl) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(baseUrl.getHost());
		return (ClassLoaderService) registry.lookup("ClassLoaderService");
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {

		try {

			byte[] def = service.getClassDefinition(name);

			if (def != null) {
				return ByteBuffer.wrap(def);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;

	}

}
