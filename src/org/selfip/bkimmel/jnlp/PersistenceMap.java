/**
 *
 */
package org.selfip.bkimmel.jnlp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.selfip.bkimmel.util.UnexpectedException;

/**
 * A <code>Map</code> to be used by Java Web Start applications that
 * persists its entries across invocations of the application using the JNLP
 * <code>PersistenceService</code> service.
 * @author brad
 */
public final class PersistenceMap extends AbstractMap<String, ByteBuffer> {

	/**
	 * The root <code>URL</code> at which the map entries should be stored.
	 */
	private final URL baseUrl;
	
	/**
	 * The <code>PersistenceService</code> to use to load and store the map
	 * entries.
	 */
	private final PersistenceService service;

	/**
	 * Creates a new <code>PersistenceMap</code>.
	 * @param baseUrl The root <code>URL</code> at which the map entries should
	 * 		be stored.
	 * @throws UnavailableServiceException If the 
	 * 		<code>javax.jnlp.PersistenceService</code> service is unavailable.
	 */
	public PersistenceMap(URL baseUrl) throws UnavailableServiceException {
		this(baseUrl, (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService"));
	}

	/**
	 * Creates a new <code>PersistenceMap</code>.
	 * @param baseUrl The root <code>URL</code> at which the map entries should
	 * 		be stored.
	 * @param service The <code>PersistenceService</code> to use to load and
	 * 		store the map entries.
	 */
	public PersistenceMap(URL baseUrl, PersistenceService service) {
		this.baseUrl = baseUrl;
		this.service = service;
	}

	/**
	 * Gets the URL, relative to {@link #baseUrl}, at which to store an entry
	 * with the given key.
	 * @param key The key for which to obtain the associated URL.
	 * @return The associated URL.
	 */
	private static final String getEntryNameForKey(String key) {
		// FIXME Modify the key so that the result is a valid URL.
		return key;
	}

	/**
	 * Gets the key associated with an entry stored at the given URL relative
	 * to {@link #baseUrl}.
	 * @param name The URL of the entry, relative to {@link #baseUrl}.
	 * @return The associated key.
	 */
	private static final String getKeyForEntryName(String name) {
		// TODO Perform inverse operation of getEntryNameForKey.
		return name;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return containsKey((String) key);
	}

	/**
	 * Determine if this <code>PersistenceMap</code> contains an entry with the
	 * given key.
	 * @param key The key to look up.
	 * @return A value indicating if an entry with the key <code>key</code> is
	 * 		stored in this <code>PersistenceMap</code>.
	 */
	public boolean containsKey(String key) {
		try {
			URL url = new URL(baseUrl, getEntryNameForKey((String) key));
			return service.get(url) != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the <code>URL</code> at which to store the entry with the given
	 * key.
	 * @param key The key for which to get the <code>URL</code>.
	 * @return The <code>URL</code> at which to store an entry with the given
	 * 		key.
	 * @throws MalformedURLException If {@link #baseUrl} is inaccessible.
	 */
	private URL getUrlForKey(Object key) throws MalformedURLException {
		return new URL(baseUrl, getEntryNameForKey((String) key));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#get(java.lang.Object)
	 */
	@Override
	public ByteBuffer get(Object key) {
		return get((String) key);
	}

	/**
	 * Gets an entry from the map.
	 * @param key The key identifying the entry.
	 * @return The <code>ByteBuffer</code> stored at the entry with the
	 * 		specified key.
	 */
	public ByteBuffer get(String key) {

		try {

			URL url = getUrlForKey(key);
			FileContents contents = service.get(url);
			byte[] data = new byte[(int) contents.getLength()];
			InputStream in = contents.getInputStream();

			in.read(data);

			return ByteBuffer.wrap(data);

		} catch (FileNotFoundException e) {

			return null;

		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(e);

		}

	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ByteBuffer put(String key, ByteBuffer value) {

		if (this.containsKey(key)) {
			this.remove(key);
		}

		try {

			URL url = getUrlForKey(key);

			value.reset();
			if (service.create(url, value.remaining()) < value.remaining()) {
				throw new RuntimeException("Could not allocate enough space in persistence store.");
			}

			FileContents contents = service.get(url);
			OutputStream out = contents.getOutputStream(true);
			byte[] data = new byte[value.remaining()];
			value.get(data);

			out.write(data);
			out.flush();
			out.close();

			return value;

		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Failed to add item.", e);

		}

	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#remove(java.lang.Object)
	 */
	@Override
	public ByteBuffer remove(Object key) {
		ByteBuffer value = this.get(key);
		this.remove((String) key);
		return value;
	}

	/**
	 * Removes an entry from this map.
	 * @param key The key identifying the entry to remove.
	 */
	private void remove(String key) {

		try {

			String name = getEntryNameForKey(key);
			service.delete(new URL(baseUrl, name));

		} catch (MalformedURLException e) {

			/*
			 * This should not happen, getEntryNameForKey should ensure that
			 * the URL is valid.
			 */
			e.printStackTrace();
			throw new UnexpectedException(e);

		} catch (IOException e) {

			/* Could not delete the entry. */
			e.printStackTrace();

		}

	}

	/* (non-Javadoc)
	 * @see java.util.AbstractMap#entrySet()
	 */
	@Override
	public Set<Map.Entry<String, ByteBuffer>> entrySet() {

		try {

			return new EntrySet(service.getNames(baseUrl));

		} catch (MalformedURLException e) {

			e.printStackTrace();
			throw new UnexpectedException(e);

		} catch (IOException e) {

			e.printStackTrace();

		}

		return new EntrySet();

	}

	/**
	 * The <code>Set</code> of entries in a <code>PersistenceMap</code>.
	 * @author brad
	 */
	private final class EntrySet extends AbstractSet<Map.Entry<String, ByteBuffer>> {

		/** The <code>List</code> of the names of the entries. */
		private final List<String> names;

		/**
		 * Creates an empty <code>EntrySet</code>.
		 */
		public EntrySet() {
			this(new String[0]);
		}

		/**
		 * Creates an <code>EntrySet</code> containing the entries named by
		 * the elements of the given array.
		 * @param names The names of the entries.
		 */
		public EntrySet(String[] names) {
			this.names = Arrays.asList(names);
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<Map.Entry<String, ByteBuffer>> iterator() {

			return new Iterator<Map.Entry<String, ByteBuffer>>() {

				/**
				 * The underlying <code>Iterator</code> used to iterate through
				 * the names.
				 */
				private final Iterator<String> iter = names.iterator();
				
				/**
				 * The last <code>Entry</code> returned by {@link #next()}.
				 */
				private Entry entry = null;

				/* (non-Javadoc)
				 * @see java.util.Iterator#hasNext()
				 */
				public boolean hasNext() {
					return iter.hasNext();
				}

				/* (non-Javadoc)
				 * @see java.util.Iterator#next()
				 */
				public Map.Entry<String, ByteBuffer> next() {
					try {
						String name = iter.next();
						String key = getKeyForEntryName(name);
						return (entry = new Entry(key));
					} catch (NoSuchElementException e) {
						entry = null;
						throw e;
					}
				}

				/* (non-Javadoc)
				 * @see java.util.Iterator#remove()
				 */
				public void remove() {
					if (entry != null) {
						PersistenceMap.this.remove(entry.getKey());
					} else {
						throw new IllegalStateException();
					}
				}

			};

		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return names.size();
		}

	} // class EntrySet

	/**
	 * A map entry in a <code>PersistenceMap</code>.
	 * @author brad
	 */
	private final class Entry implements Map.Entry<String, ByteBuffer> {

		/** The key identifying this entry. */
		private final String key;

		/**
		 * Creates a new <code>Entry</code>.
		 * @param key The key identifying this entry.
		 */
		private Entry(String key) {
			this.key = key;
		}

		/* (non-Javadoc)
		 * @see java.util.Map$Entry#getKey()
		 */
		public String getKey() {
			return key;
		}

		/* (non-Javadoc)
		 * @see java.util.Map$Entry#getValue()
		 */
		public ByteBuffer getValue() {
			return PersistenceMap.this.get(key);
		}

		/* (non-Javadoc)
		 * @see java.util.Map$Entry#setValue(java.lang.Object)
		 */
		public ByteBuffer setValue(ByteBuffer value) {
			return PersistenceMap.this.put(key, value);
		}

	} // class Entry

}
