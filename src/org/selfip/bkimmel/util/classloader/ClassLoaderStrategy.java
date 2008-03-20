/**
 * 
 */
package org.selfip.bkimmel.util.classloader;

import java.nio.ByteBuffer;

/**
 * Obtains the class definition for a given binary class name.
 * @author brad
 */
public interface ClassLoaderStrategy {
	
	/**
	 * Obtains the class definition for a given binary class name.
	 * @param name The binary name of the class whose class definition to load.
	 * @return The class definition.
	 */
	ByteBuffer getClassDefinition(String name);

}
