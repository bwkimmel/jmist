/**
 *
 */
package org.selfip.bkimmel.util;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;

/**
 * A cache for storing and retrieving bytes that performs integrity checking on
 * its entries.
 * @author brad
 */
public interface CheckedCache {

	/**
	 * Adds an entry to the cache.
	 * @param key The key identifying the entry to add.
	 * @param data A <code>ByteBuffer</code> containing the data to associate
	 * 		with the key.
	 * @param digest The <code>MessageDigest</code> to update with the data
	 * 		being added.
	 */
	void put(String key, ByteBuffer data, MessageDigest digest);

	/**
	 * Gets an entry from the cache, optionally performing integrity checking.
	 * @param key The key identifying the entry to look up.
	 * @param digest The digest to compare with the digest of the data stored
	 * 		in the cache.  If <code>digest</code> is <code>null</code>, no
	 * 		integrity checking is stored.
	 * @return The cached data associated with <code>key</code>, or
	 * 		<code>null</code> if no matching cache entry was found.
	 * @throws DigestException If <code>digest</code> does not match the digest
	 * 		stored in the cache, or if the digest could not be read from the
	 * 		cache entry.
	 */
	ByteBuffer get(String key, byte[] digest) throws DigestException;

	/**
	 * Gets an entry from the cache without performing integrity checking.
	 * @param key The key identifying the entry to look up.
	 * @returnThe cached data associated with <code>key</code>, or
	 * 		<code>null</code> if no matching cache entry was found.
	 */
	ByteBuffer get(String key);

	/**
	 * Removes an entry from the cache.
	 * @param key The key identifying the entry to remove.
	 */
	void remove(String key);

}
