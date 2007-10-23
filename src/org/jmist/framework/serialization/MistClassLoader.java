/**
 *
 */
package org.jmist.framework.serialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * A class loader that loads classes from sources specified by system
 * preferences.  This class is a singleton.
 * @author bkimmel
 */
public final class MistClassLoader {

	/**
	 * Returns the single instance of <code>MistClassLoader</code>.
	 * @return The single instance of <code>MistClassLoader</code>.
	 */
	public static ClassLoader getInstance() {

		if (instance == null) {

			try {

				Preferences		prefs		= Preferences.systemNodeForPackage(MistClassLoader.class).node("sources");
				String[]		keys		= prefs.keys();
				Set<URL>		urlSet		= new HashSet<URL>();
				URL[]			urls;

				for (String key : keys) {

					String		urlSpec		= prefs.get(key, "");

					if (urlSpec != "") {

						try {
							urlSet.add(new URL(urlSpec));
						} catch (MalformedURLException e) {
							System.err.println(e);
						}

					} // urlSpec != ""

				} // key : keys

				urls			= urlSet.toArray(new URL[0]);
				instance		= new URLClassLoader(urls);

			} catch (BackingStoreException e) {

				System.err.println("Could not load external sources, using system class loader.");
				System.err.println(e);

				instance		= ClassLoader.getSystemClassLoader();

			}

		}

		return instance;

	}

	/**
	 * Default constructor.  This class is non-instantiable.
	 */
	private MistClassLoader() {
		// nothing to do.
		assert(false);
	}

	/** The single instance. */
	private static ClassLoader instance = null;

}
