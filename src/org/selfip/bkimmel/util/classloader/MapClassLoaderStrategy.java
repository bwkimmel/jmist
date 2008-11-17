/**
 *
 */
package org.selfip.bkimmel.util.classloader;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * A <code>ClassLoaderStrategy</code> that loads class definitions from a
 * <code>Map</code>.
 * @author brad
 */
public final class MapClassLoaderStrategy implements ClassLoaderStrategy {

	/** The <code>Map</code> containing the class definitions. */
	private final Map<String, ByteBuffer> classDefs;

	/**
	 * Creates a new <code>MapClassLoaderStrategy</code>.
	 */
	public MapClassLoaderStrategy() {
		this.classDefs = new HashMap<String, ByteBuffer>();
	}

	/**
	 * Creates a new <code>MapClassLoaderStrategy</code>.
	 * @param classDefs The <code>Map</code> from which to get class
	 * 		definitions.
	 */
	public MapClassLoaderStrategy(Map<String, ByteBuffer> classDefs) {
		this.classDefs = classDefs;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {
		return classDefs.get(name);
	}

	/**
	 * Sets the definition of a class.
	 * @param name The name of the class to define.
	 * @param def The definition of the class.
	 */
	public void setClassDefinition(String name, byte[] def) {
		classDefs.put(name, ByteBuffer.wrap(def));
	}

	/**
	 * Sets the definition of a class.
	 * @param name The name of the class to define.
	 * @param def The definition of the class.
	 */
	public void setClassDefinition(String name, ByteBuffer def) {
		classDefs.put(name, def);
	}

}
