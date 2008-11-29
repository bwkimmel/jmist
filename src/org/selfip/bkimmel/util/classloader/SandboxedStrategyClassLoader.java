/**
 *
 */
package org.selfip.bkimmel.util.classloader;

import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

/**
 * @author brad
 *
 */
public class SandboxedStrategyClassLoader extends StrategyClassLoader {

	private final PermissionCollection perms;

	/**
	 * @param strategy
	 * @param parent
	 */
	public SandboxedStrategyClassLoader(ClassLoaderStrategy strategy,
			ClassLoader parent) {
		this(strategy, null, parent);
	}

	/**
	 * @param strategy
	 */
	public SandboxedStrategyClassLoader(ClassLoaderStrategy strategy) {
		this(strategy, (PermissionCollection) null);
	}

	/**
	 * @param strategy
	 */
	public SandboxedStrategyClassLoader(ClassLoaderStrategy strategy, PermissionCollection perms) {
		super(strategy);
		this.perms = perms;
	}

	/**
	 * @param parent
	 */
	public SandboxedStrategyClassLoader(ClassLoaderStrategy strategy, PermissionCollection perms, ClassLoader parent) {
		super(strategy, parent);
		this.perms = perms;
	}

	@Override
	ProtectionDomain getProtectionDomain(String name) {
		CodeSource source = new CodeSource(null, (Certificate[]) null);
		return new ProtectionDomain(source, perms, this, null);
	}

}
