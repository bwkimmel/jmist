/**
 * 
 */
package org.selfip.bkimmel.util.classloader;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * @author brad
 *
 */
public final class CompositeClassLoaderStrategy implements ClassLoaderStrategy {
	
	private final ClassLoaderStrategy[] strategies;
	
	public CompositeClassLoaderStrategy(Collection<ClassLoaderStrategy> strategies) {
		this(strategies.toArray(new ClassLoaderStrategy[strategies.size()]));
	}
	
	private CompositeClassLoaderStrategy(ClassLoaderStrategy[] strategies) {
		this.strategies = strategies;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {

		ByteBuffer def;
		
		for (ClassLoaderStrategy strategy : strategies) {
			if ((def = strategy.getClassDefinition(name)) != null) {
				return def;
			}
		}
		
		return null;

	}

}
