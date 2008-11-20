/**
 *
 */
package org.jdcp.server.classmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.selfip.bkimmel.io.FileUtil;
import org.selfip.bkimmel.io.StreamUtil;
import org.selfip.bkimmel.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class FileClassManager extends AbstractClassManager implements
		ParentClassManager {

	private static final String DIGEST_ALGORITHM = "MD5";
	private static final int DIGEST_LENGTH = 16;

	private final File currentDirectory;
	private final File deprecatedDirectory;
	private final File childrenDirectory;
	private int nextChildIndex = 0;

	private final Map<String, List<Integer>> deprecationMap = new HashMap<String, List<Integer>>();
	private final List<Reference<ChildClassManager>> activeChildren = new ArrayList<Reference<ChildClassManager>>();
	private final List<Integer> deprecationPendingList = new ArrayList<Integer>();

	public FileClassManager(String rootDirectory) throws IllegalArgumentException {
		this(new File(rootDirectory));
	}

	public FileClassManager(File rootDirectory) throws IllegalArgumentException {
		if (!rootDirectory.isDirectory()) {
			throw new IllegalArgumentException("rootDirectory must be a directory");
		}
		this.currentDirectory = new File(rootDirectory, "current");
		this.deprecatedDirectory = new File(rootDirectory, "deprecated");
		this.childrenDirectory = new File(rootDirectory, "children");
		currentDirectory.mkdir();
		deprecatedDirectory.mkdir();
		childrenDirectory.mkdir();
		FileUtil.clearDirectory(deprecatedDirectory);
		FileUtil.clearDirectory(childrenDirectory);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ClassManager#getClassDigest(java.lang.String)
	 */
	@Override
	public byte[] getClassDigest(String name) {
		return getClassDigest(getClassFile(name));
	}

	private byte[] getClassDigest(File file) {
		if (file.exists()) {
			try {
				FileInputStream stream = new FileInputStream(file);
				byte[] digest = new byte[DIGEST_LENGTH];
				stream.read(digest);
				stream.close();
				return digest;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private byte[] computeClassDigest(ByteBuffer def) {
		try {
			MessageDigest alg = MessageDigest.getInstance(DIGEST_ALGORITHM);
			def.mark();
			alg.update(def);
			def.reset();
			return alg.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}

	private File getClassFile(String name) {
		return new File(currentDirectory, name);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ClassManager#setClassDefinition(java.lang.String, java.nio.ByteBuffer)
	 */
	@Override
	public void setClassDefinition(String name, ByteBuffer def) {
		File file = getClassFile(name);
		byte[] digest = computeClassDigest(def);
		if (file.exists()) {
			byte[] oldDigest = getClassDigest(file);
			if (Arrays.equals(digest, oldDigest)) {
				return;
			}
			if (nextChildIndex > 0) {
				File deprecatedFile = getDeprecatedClassFile(name, nextChildIndex, true);
				if (!deprecatedFile.exists()) {
					if (!file.renameTo(deprecatedFile)) {
						throw new UnexpectedException("Unable to deprecate class.");
					}
					List<Integer> deprecationList = deprecationMap.get(name);
					if (deprecationList == null) {
						deprecationList = new ArrayList<Integer>();
						deprecationMap.put(name, deprecationList);
					}
					deprecationList.add(nextChildIndex);
				}
			}
		}
		writeClass(file, def, digest);
	}

	private File getDeprecatedClassFile(String name, int childIndex,
			boolean createDirectory) {
		File directory = new File(deprecatedDirectory, Integer
				.toString(childIndex));
		if (createDirectory && !directory.isDirectory()) {
			directory.mkdir();
		}
		return new File(directory, name);
	}

	private void writeClass(File file, ByteBuffer def, byte[] digest) {
		try {
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(digest);
			StreamUtil.writeBytes(def, stream);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeClass(File file, ByteBuffer def) {
		writeClass(file, def, computeClassDigest(def));
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	@Override
	public ByteBuffer getClassDefinition(String name) {
		return getClassDefinition(getClassFile(name));
	}

	private ByteBuffer getClassDefinition(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			stream.skip(DIGEST_LENGTH);

			int size = (int) file.length() - DIGEST_LENGTH;
			byte[] def = new byte[size];
			stream.read(def);
			stream.close();

			return ByteBuffer.wrap(def);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ParentClassManager#createChildClassManager()
	 */
	public ClassManager createChildClassManager() {
		ChildClassManager child = new ChildClassManager();
		activeChildren.add(new WeakReference<ChildClassManager>(child));
		return child;
	}

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ParentClassManager#releaseChildClassManager(org.jdcp.server.classmanager.ClassManager)
	 */
	public void releaseChildClassManager(ClassManager childClassManager) {
		ChildClassManager child = (ChildClassManager) childClassManager;
		if (child.getParent() != this) {
			throw new IllegalArgumentException("childClassManager is not the child of this ParentClassManager");
		}
		releaseChildClassManager(child);
	}

	private void releaseChildClassManager(ChildClassManager child) {
		child.release();
		for (int i = 0; i < activeChildren.size(); i++) {
			Reference<ChildClassManager> ref = activeChildren.get(i);
			ChildClassManager current = ref.get();
			if (current.childIndex == child.childIndex) {
				FileUtil.deleteRecursive(child.childDirectory);
				deprecationPendingList.add(child.childIndex);
				if (i == 0) {
					Collections.sort(deprecationPendingList);
					for (int pendingIndex : deprecationPendingList) {
						if (pendingIndex > current.childIndex) {
							break;
						}
						File pendingDirectory = new File(deprecatedDirectory, Integer.toString(pendingIndex + 1));
						FileUtil.deleteRecursive(pendingDirectory);
					}
				}
			}
		}
	}

	/**
	 * @author brad
	 *
	 */
	private final class ChildClassManager extends AbstractClassManager {

		private final File childDirectory;
		private final int childIndex;
		private boolean released = false;

		public ChildClassManager() {
			this.childIndex = nextChildIndex++;
			this.childDirectory = new File(childrenDirectory, Integer
					.toString(childIndex));
		}

		private void check() {
			if (released) {
				throw new IllegalStateException("Attempt to use a released child ClassManager.");
			}
		}

		private File getClassFile(String name) {
			check();
			if (childDirectory.isDirectory()) {
				File file = new File(childDirectory, name);
				if (file.exists()) {
					return file;
				}
			}

			List<Integer> deprecationList = deprecationMap.get(name);
			if (deprecationList != null) {
				int index = Collections.binarySearch(deprecationList, childIndex);
				index = Math.abs(index + 1);

				if (index < deprecationList.size()) {
					int deprecationIndex = deprecationList.get(index);
					File file = FileClassManager.this.getDeprecatedClassFile(name, deprecationIndex, false);
					if (!file.exists()) {
						throw new UnexpectedException("Deprecated class missing.");
					}
					return file;
				}
			}

			return new File(currentDirectory, name);
		}

		/* (non-Javadoc)
		 * @see org.jdcp.server.classmanager.ClassManager#getClassDigest(java.lang.String)
		 */
		@Override
		public byte[] getClassDigest(String name) {
			return FileClassManager.this.getClassDigest(getClassFile(name));
		}

		/* (non-Javadoc)
		 * @see org.jdcp.server.classmanager.ClassManager#setClassDefinition(java.lang.String, java.nio.ByteBuffer)
		 */
		@Override
		public void setClassDefinition(String name, ByteBuffer def) {
			check();
			if (!childDirectory.exists()) {
				childDirectory.mkdir();
			}
			File file = new File(childDirectory, name);
			writeClass(file, def);
		}

		/* (non-Javadoc)
		 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
		 */
		@Override
		public ByteBuffer getClassDefinition(String name) {
			return FileClassManager.this.getClassDefinition(getClassFile(name));
		}

		public FileClassManager getParent() {
			return FileClassManager.this;
		}

		private void release() {
			released = true;
		}

	}

}
