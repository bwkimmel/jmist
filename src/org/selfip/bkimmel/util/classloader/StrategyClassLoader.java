/**
 *
 */
package org.selfip.bkimmel.util.classloader;

import java.nio.ByteBuffer;
import java.security.ProtectionDomain;

/**
 * @author brad
 *
 */
public class StrategyClassLoader extends ClassLoader {

	private final ClassLoaderStrategy strategy;

	/**
	 *
	 */
	public StrategyClassLoader(ClassLoaderStrategy strategy) {
		this(strategy, null);
	}

	/**
	 * @param parent
	 */
	public StrategyClassLoader(ClassLoaderStrategy strategy, ClassLoader parent) {
		super(parent);
		this.strategy = strategy;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		ByteBuffer def = strategy.getClassDefinition(name);

		if (def != null) {

			ProtectionDomain domain = getProtectionDomain(name);
			Class<?> result = super.defineClass(name, def, domain);

			if (result != null) {
				super.resolveClass(result);
				return result;
			}

		}

		throw new ClassNotFoundException(name);

	}

	ProtectionDomain getProtectionDomain(String name) {
		return null;
	}

}
