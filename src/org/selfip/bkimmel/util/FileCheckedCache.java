/**
 *
 */
package org.selfip.bkimmel.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.selfip.bkimmel.io.StreamUtil;

/**
 * @author brad
 *
 */
public final class FileCheckedCache implements CheckedCache {

	private final File directory;

	public FileCheckedCache(String directory) {
		this(new File(directory));
	}

	public FileCheckedCache(File directory) {
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("directory must be a directory.");
		}
		this.directory = directory;
	}

	private File getFile(String key, boolean createDirectory) {
		File file = new File(directory, key.concat(".dat"));

		if (createDirectory && !file.exists() && (key.contains("/") || key.contains(File.pathSeparator))) {
			File path = file.getParentFile();
			if (path != null) {
				path.mkdirs();
			}
		}

		return file;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#get(java.lang.String, byte[])
	 */
	public ByteBuffer get(String key, byte[] digest) throws DigestException {
		File file = getFile(key, false);

		try {
			FileInputStream fs = new FileInputStream(file);
			DataInputStream ds = new DataInputStream(fs);
			int digestLength = ds.readInt();
			byte[] cacheDigest = new byte[digestLength];
			ds.read(cacheDigest);

			if (Arrays.equals(digest, cacheDigest)) {
				byte[] def = new byte[(int) file.length() - (digestLength + 4)];
				ds.read(def);
				return ByteBuffer.wrap(def);
			} else {
				throw new DigestException();
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#get(java.lang.String)
	 */
	public ByteBuffer get(String key) {
		try {
			return get(key, null);
		} catch (DigestException e) {
			throw new UnexpectedException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#put(java.lang.String, java.nio.ByteBuffer, java.security.MessageDigest)
	 */
	public byte[] put(String key, ByteBuffer data, MessageDigest digest) {

		int position = data.position();
		digest.reset();
		digest.update(data);
		data.position(position);

		byte[] digestBytes = digest.digest();

		File file = getFile(key, true);

		try {
			FileOutputStream fs = new FileOutputStream(file);
			DataOutputStream ds = new DataOutputStream(fs);

			ds.writeInt(digestBytes.length);
			ds.write(digestBytes);
			StreamUtil.writeBytes(data, ds);

			ds.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return digestBytes;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.CheckedCache#remove(java.lang.String)
	 */
	public void remove(String key) {
		File file = getFile(key, false);
		file.delete();
	}

}
