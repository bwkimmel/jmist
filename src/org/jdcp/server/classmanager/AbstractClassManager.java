/**
 *
 */
package org.jdcp.server.classmanager;

import java.nio.ByteBuffer;

/**
 * @author brad
 *
 */
public abstract class AbstractClassManager implements ClassManager {

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ClassManager#setClassDefinition(java.lang.String, byte[])
	 */
	public void setClassDefinition(String name, byte[] def) {
		this.setClassDefinition(name, ByteBuffer.wrap(def));
	}

}
