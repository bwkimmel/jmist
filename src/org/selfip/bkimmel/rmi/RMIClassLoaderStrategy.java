/**
 * 
 */
package org.selfip.bkimmel.rmi;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;

import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;

/**
 * @author brad
 *
 */
public final class RMIClassLoaderStrategy implements ClassLoaderStrategy {
	
	private final ClassLoaderService service;
	
	public RMIClassLoaderStrategy(ClassLoaderService service) {
		this.service = service;
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
