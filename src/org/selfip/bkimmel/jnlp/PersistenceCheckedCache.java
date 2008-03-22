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
import java.security.DigestException;
import java.security.MessageDigest;

import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.selfip.bkimmel.util.CheckedCache;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * A <code>CheckedCache</code> to be used by Java Web Start applications that
 * persists its entries across invocations using the
 * <code>PersistenceService</code> service.
 * @author brad
 */
public final class PersistenceCheckedCache implements CheckedCache {

	/** The <code>URL</code> at which the cache should be rooted. */
	private final URL baseUrl;

	/**
	 * The <code>URL</code> at which the data for the cache entries are rooted.
	 */
	private final URL baseDataUrl;

	/**
	 * The <code>URL</code> at which the digests for the cache entries are
	 * rooted.
	 */
	private final URL baseDigestUrl;

	/**
	 * The <code>PersistenceService</code> to use to load and store the cache
	 * entries.
	 */
	private final PersistenceService service;

	/**
	 * Creates a new <code>PersistenceCheckedCache</code>.
	 * @param baseUrl The <code>URL</code> at which the cache should be rooted.
	 * @throws UnavailableServiceException If the
	 * 		<code>javax.jnlp.PersistenceService</code> service is unavailable.
	 * @throws MalformedURLException If <code>baseUrl</code> is inaccessible.
	 */
	public PersistenceCheckedCache(URL baseUrl) throws UnavailableServiceException, MalformedURLException {
		this(baseUrl, (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService"));
	}

	/**
	 * Creates a new <code>PersistenceCheckedCache</code>.
	 * @param baseUrl The <code>URL</code> at which the cache should be rooted.
	 * @param service The <code>PersistenceService</code> to use to load and
	 * 		store the cache entries.
	 * @throws MalformedURLException If <code>baseUrl</code> is inaccessible.
	 */
	public PersistenceCheckedCache(URL baseUrl, PersistenceService service) throws MalformedURLException {
		this.baseUrl = baseUrl;
		this.baseDataUrl = new URL(this.baseUrl, "data");
		this.baseDigestUrl = new URL(this.baseUrl, "digest");
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#get(java.lang.String)
	 */
	public ByteBuffer get(String key) {

		try {

			/* Delegate to the other overloaded method. */
			return this.get(key, null);

		} catch (DigestException e) {

			/*
			 * This should not happen -- if no digest is provided to check
			 * against, the other overload of this method should not throw a
			 * DigestException.
			 */
			e.printStackTrace();
			throw new UnexpectedException(e);

		}

	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#get(java.lang.String, byte[])
	 */
	public ByteBuffer get(String key, byte[] digest) throws DigestException {

		try {

			URL url = this.getDataUrlForKey(key);
			FileContents contents = service.get(url);

			/*
			 * If a digest was provided, check it against the one that has been
			 * stored.
			 */
			if (digest != null) {
				this.checkDigest(key, digest);
			}

			/* If we get here, the digest is okay, so read the data. */
			InputStream in = contents.getInputStream();
			byte[] data = new byte[(int) contents.getLength()];

			in.read(data);

			return ByteBuffer.wrap(data);

		} catch (FileNotFoundException e) {

			/*
			 * Nothing to do.  This just means there is no cached item with the
			 * specified key.
			 */

		} catch (MalformedURLException e) {

			/*
			 * This should not happen.  getDataUrlForKey should ensure that the
			 * URL is valid.
			 */
			throw new UnexpectedException(e);

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * Ensures that the digest associated with the specified cache entry
	 * matches the given digest.
	 * @param key The <code>String</code> identifying the cache entry to check.
	 * @param digest The expected digest.
	 * @throws DigestException Thrown if the digest for the cache entry is
	 * 		missing, could not be read, or does not match <code>digest</code>.
	 */
	private void checkDigest(String key, byte[] digest) throws DigestException {

		try {

			/* Read the digest from the persistence service. */
			URL url = this.getDigestUrlForKey(key);
			FileContents contents = service.get(url);
			InputStream in = contents.getInputStream();
			byte[] cachedDigest = new byte[(int) contents.getLength()];

			in.read(cachedDigest);

			/* Verify that the provided digest matches. */
			if (!MessageDigest.isEqual(digest, cachedDigest)) {
				throw new DigestException("Digest mismatch.");
			}

		} catch (MalformedURLException e) {

			/*
			 * This should not happen -- getDigestUrlForKey should ensure that
			 * the URL is valid.
			 */
			e.printStackTrace();
			throw new UnexpectedException(e);

		} catch (FileNotFoundException e) {

			/* The file exists, but there is no digest. */
			throw new DigestException("No digest.", e);

		} catch (IOException e) {

			/* An error occurred while reading the digest. */
			e.printStackTrace();
			throw new DigestException("Could not read digest.", e);

		}

	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#put(java.lang.String, java.nio.ByteBuffer, java.security.MessageDigest)
	 */
	public void put(String key, ByteBuffer data, MessageDigest digest) {

		digest.reset();
		data.reset();

		byte[] dataArray = new byte[data.remaining()];
		data.get(dataArray);

		digest.update(dataArray);

		byte[] digestArray = digest.digest();

		URL dataUrl = this.getDataUrlForKey(key);
		URL digestUrl = this.getDigestUrlForKey(key);

		this.remove(key);
		this.write(dataUrl, dataArray);
		this.write(digestUrl, digestArray);

	}

	/**
	 * Writes data to persistent storage.
	 * @param url The <code>URL</code> to associate with the data.
	 * @param data The data to write.
	 */
	private void write(URL url, byte[] data) {

		try {

			if (service.create(url, data.length) < data.length) {
				service.delete(url);
				throw new RuntimeException("Could not allocate enough space in persistence store.");
			}

			FileContents contents = service.get(url);
			OutputStream out = contents.getOutputStream(true);

			out.write(data);

		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException("Could not write data to persistence store.", e);

		}

	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#remove(java.lang.String)
	 */
	public void remove(String key) {

		this.remove(this.getDataUrlForKey(key));
		this.remove(this.getDigestUrlForKey(key));

	}

	/**
	 * Removes the entry at the specified URL.
	 * @param url The <code>URL</code> identifying the entry to remove.
	 */
	private void remove(URL url) {

		try {

			service.delete(url);

		} catch (MalformedURLException e) {

			/*
			 * This should not happen -- getDataUrlForKey and getDigestUrlforKey
			 * should ensure that their respective URLs are valid.
			 */
			e.printStackTrace();
			throw new UnexpectedException(e);

		} catch (IOException e) {

			/* Failed to remove the item. */
			e.printStackTrace();

		}

	}

	/**
	 * Gets the <code>URL</code> at which the data associated with the given
	 * key is stored.
	 * @param key The key for which to get the data <code>URL</code>.
	 * @return The <code>URL</code> at which the data is stored.
	 */
	private URL getDataUrlForKey(String key) {
		return tryGetUrlForKey(baseDataUrl, key);
	}

	/**
	 * Gets the <code>URL</code> at which the digest associated with the given
	 * key is stored.
	 * @param key The key for which to get the digest <code>URL</code>.
	 * @return The <code>URL</code> at which the digest is stored.
	 */
	private URL getDigestUrlForKey(String key) {
		return tryGetUrlForKey(baseDigestUrl, key);
	}

	/**
	 * Gets the <code>URL</code> associated with the given key.
	 * @param baseUrl The base <code>URL</code>.
	 * @param key The key for which to obtain the associated <code>URL</code>.
	 * @return The <code>URL</code> associated with <code>key</code>, rooted at
	 * 		<code>baseUrl</code>.
	 */
	private static final URL tryGetUrlForKey(URL baseUrl, String key) {
		try {
			return new URL(baseUrl, key);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
