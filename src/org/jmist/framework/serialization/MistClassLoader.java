/**
 *
 */
package org.jmist.framework.serialization;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
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
	 * Removes all external sources for classes.
	 */
	public static void clearSources() {
		getInstance();
		urlSet.clear();
	}

	/**
	 * Adds a new external source for classes.
	 * @param url The URL to load classes from.
	 * @throws MalformedURLException if the URL is invalid.
	 */
	public static void addSource(String url) throws MalformedURLException {
		getInstance();
		urlSet.add(new URL(url));
	}

	/**
	 * Removes an external source for classes.
	 * @param url The URL to remove.
	 * @throws MalformedURLException if the URL is invalid.
	 */
	public static void removeSource(String url) throws MalformedURLException {
		getInstance();
		urlSet.remove(new URL(url));
	}

	/**
	 * Causes the instance to be refreshed to include changes made to the
	 * list of sources.
	 */
	public static void refresh() {
		instance = null;
	}

	/**
	 * Writes changes to the sources.
	 */
	public static void sync() {

		Preferences		prefs		= Preferences.systemNodeForPackage(MistClassLoader.class).node(SOURCES_NODE);
		int				index		= 0;

		try {

			// Clear existing sources.
			prefs.clear();

			for (URL url : urlSet) {
				prefs.put(Integer.toString(index++), url.toString());
			}

			// Write the preferences to the backing store.
			prefs.sync();

		} catch (BackingStoreException e) {

			System.err.println("Could not write sources.");
			System.err.println(e);

		}

		// Cause the instance to be recreated next time getInstance is called.
		refresh();

	}

	/**
	 * Loads the set of external sources.
	 * @throws BackingStoreException if this operation cannot be completed due
	 * 		to a failure in the backing store, or inability to communicate with
	 * 		it.
	 */
	private static void loadSources() throws BackingStoreException {

		Preferences		prefs		= Preferences.systemNodeForPackage(MistClassLoader.class).node(SOURCES_NODE);
		String[]		keys		= prefs.keys();

		urlSet			= new LinkedHashSet<URL>();

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

	}

	/**
	 * Returns the single instance of a <code>ClassLoader</code>.
	 * @return The single instance of a <code>ClassLoader</code>.
	 */
	public static ClassLoader getInstance() {

		if (instance == null) {

			try {

				if (urlSet == null)
					loadSources();

				URL[] urls		= urlSet.toArray(new URL[0]);
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

	/** The set of URLs at which to search for classes. */
	private static Set<URL> urlSet = null;

	/** The name of the preferences node containing the sources. */
	private static final String SOURCES_NODE = "sources";

}
