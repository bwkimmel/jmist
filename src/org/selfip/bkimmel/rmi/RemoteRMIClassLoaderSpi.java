/**
 *
 */
package org.selfip.bkimmel.rmi;

import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoaderSpi;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.selfip.bkimmel.jnlp.PersistenceCheckedCache;
import org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy;
import org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy.DigestLookup;
import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;
import org.selfip.bkimmel.util.classloader.StrategyClassLoader;
import org.selfip.bkimmel.util.CheckedCache;
import org.selfip.bkimmel.util.ClassUtil;
import org.selfip.bkimmel.util.StringUtil;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class RemoteRMIClassLoaderSpi extends RMIClassLoaderSpi {

	/**
	 * The name of the digest algorithm to use to compute the annotation for
	 * a class.
	 */
	private static final String digestAlgorithm = "MD5";

	private final URL baseUrl;

	public RemoteRMIClassLoaderSpi() throws IllegalStateException {
		try {
			BasicService service = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			this.baseUrl = service.getCodeBase();
		} catch (UnavailableServiceException e) {
			throw new IllegalStateException("javax.jnlp.BasicService is required.", e);
		}
	}

	public RemoteRMIClassLoaderSpi(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	/* (non-Javadoc)
	 * @see java.rmi.server.RMIClassLoaderSpi#getClassAnnotation(java.lang.Class)
	 */
	@Override
	public String getClassAnnotation(Class<?> cl) {

		try {

			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			ClassUtil.getClassDigest(cl, digest);

			return StringUtil.toHex(digest.digest());

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
			throw new UnexpectedException(e);

		}

	}

	/* (non-Javadoc)
	 * @see java.rmi.server.RMIClassLoaderSpi#getClassLoader(java.lang.String)
	 */
	@Override
	public ClassLoader getClassLoader(final String annotation)
			throws MalformedURLException {

		try {

			DigestLookup lookup = new DigestLookup() {

				/* (non-Javadoc)
				 * @see org.selfip.bkimmel.util.CheckedCacheClassLoaderStrategy.DigestLookup#getDigest(java.lang.String)
				 */
				public byte[] getDigest(String name) {
					return StringUtil.hexToByteArray(annotation);
				}

			};

			CheckedCache cache = new PersistenceCheckedCache(baseUrl);
			ClassLoaderStrategy fallback = new RMIClassLoaderStrategy(baseUrl);
			ClassLoaderStrategy strategy = new CheckedCacheClassLoaderStrategy(cache, lookup, fallback);

			return new StrategyClassLoader(strategy);

		} catch (UnavailableServiceException e) {

			throw new IllegalStateException("A required service could not be found.", e);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		throw new RuntimeException();

	}

	/* (non-Javadoc)
	 * @see java.rmi.server.RMIClassLoaderSpi#loadClass(java.lang.String, java.lang.String, java.lang.ClassLoader)
	 */
	@Override
	public Class<?> loadClass(String annotation, String name,
			ClassLoader defaultLoader) throws MalformedURLException,
			ClassNotFoundException {

		if (defaultLoader != null) {
			return defaultLoader.loadClass(name);
		}

		return getClassLoader(annotation).loadClass(name);

	}

	/* (non-Javadoc)
	 * @see java.rmi.server.RMIClassLoaderSpi#loadProxyClass(java.lang.String, java.lang.String[], java.lang.ClassLoader)
	 */
	@Override
	public Class<?> loadProxyClass(String annotation, String[] interfaceNames,
			ClassLoader defaultLoader) throws MalformedURLException,
			ClassNotFoundException {

		Class<?>[] interfaces = new Class<?>[interfaceNames.length];
		
		for (int i = 0; i < interfaces.length; i++) {
			interfaces[i] = loadClass(null, interfaceNames[i], defaultLoader);
		}

		if (defaultLoader != null) {
			return Proxy.getProxyClass(defaultLoader, interfaces);
		} else {
			return Proxy.getProxyClass(getClassLoader(annotation), interfaces);
		}

	}

}
