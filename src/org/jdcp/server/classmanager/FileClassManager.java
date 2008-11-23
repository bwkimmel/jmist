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

	private static final String CLASS_EXTENSION = ".class";
	private static final String DIGEST_EXTENSION = ".md5";
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

	public String getBaseFileName(String className) {
		return className.replace('.', '/');
	}

	public void writeClass(File directory, String name, ByteBuffer def) {
		writeClass(directory, name, def, computeClassDigest(def));
	}

	public void writeClass(File directory, String name, ByteBuffer def, byte[] digest) {
		String baseName = getBaseFileName(name);
		File classFile = new File(directory, baseName + CLASS_EXTENSION);
		File digestFile = new File(directory, baseName + DIGEST_EXTENSION);

		try {
			FileUtil.setFileContents(classFile, def, true);
			FileUtil.setFileContents(digestFile, digest, true);
		} catch (IOException e) {
			e.printStackTrace();
			classFile.delete();
			digestFile.delete();
		}
	}

	public void moveClass(File fromDirectory, String name, File toDirectory) {
		String baseName = getBaseFileName(name);
		File fromClassFile = new File(fromDirectory, baseName + CLASS_EXTENSION);
		File toClassFile = new File(toDirectory, baseName + CLASS_EXTENSION);
		File fromDigestFile = new File(fromDirectory, baseName + DIGEST_EXTENSION);
		File toDigestFile = new File(toDirectory, baseName + DIGEST_EXTENSION);
		File toClassDirectory = toClassFile.getParentFile();

		toClassDirectory.mkdirs();
		fromClassFile.renameTo(toClassFile);
		fromDigestFile.renameTo(toDigestFile);
	}

	public boolean classExists(File directory, String name) {
		String baseName = getBaseFileName(name);
		File classFile = new File(directory, baseName + CLASS_EXTENSION);
		File digestFile = new File(directory, baseName + DIGEST_EXTENSION);

		return classFile.isFile() && digestFile.isFile();
	}

	private byte[] getFileContents(File file) {
		if (file.exists()) {
			try {
				return FileUtil.getFileContents(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private byte[] getClassDigest(File directory, String name) {
		String baseName = getBaseFileName(name);
		File digestFile = new File(directory, baseName + DIGEST_EXTENSION);
		return getFileContents(digestFile);
	}

	private ByteBuffer getClassDefinition(File directory, String name) {
		String baseName = getBaseFileName(name);
		File digestFile = new File(directory, baseName + CLASS_EXTENSION);
		return ByteBuffer.wrap(getFileContents(digestFile));
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

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ClassManager#getClassDigest(java.lang.String)
	 */
	public byte[] getClassDigest(String name) {
		return getClassDigest(currentDirectory, name);
	}

	/* (non-Javadoc)
	 * @see org.jdcp.server.classmanager.ClassManager#setClassDefinition(java.lang.String, java.nio.ByteBuffer)
	 */
	public void setClassDefinition(String name, ByteBuffer def) {
		byte[] digest = computeClassDigest(def);
		if (classExists(currentDirectory, name)) {
			byte[] oldDigest = getClassDigest(currentDirectory, name);
			if (Arrays.equals(digest, oldDigest)) {
				return;
			}
			if (nextChildIndex > 0) {
				File deprecatedDirectory = getDeprecatedDirectory(name, nextChildIndex);
				if (!classExists(deprecatedDirectory, name)) {
					moveClass(currentDirectory, name, deprecatedDirectory);
					List<Integer> deprecationList = deprecationMap.get(name);
					if (deprecationList == null) {
						deprecationList = new ArrayList<Integer>();
						deprecationMap.put(name, deprecationList);
					}
					deprecationList.add(nextChildIndex);
				}
			}
		}
		writeClass(currentDirectory, name, def, digest);
	}

	private File getDeprecatedDirectory(String name, int childIndex) {
		return new File(deprecatedDirectory, Integer.toString(childIndex));
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {
		return getClassDefinition(currentDirectory, name);
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

		private File getClassDirectory(String name) {
			check();
			if (classExists(childDirectory, name)) {
				return childDirectory;
			}

			List<Integer> deprecationList = deprecationMap.get(name);
			if (deprecationList != null) {
				int index = Collections.binarySearch(deprecationList, childIndex);
				index = Math.abs(index + 1);

				if (index < deprecationList.size()) {
					int deprecationIndex = deprecationList.get(index);
					File deprecatedDirectory = FileClassManager.this.getDeprecatedDirectory(name, deprecationIndex);
					if (!classExists(deprecatedDirectory, name)) {
						throw new UnexpectedException("Deprecated class missing.");
					}
					return deprecatedDirectory;
				}
			}

			return currentDirectory;
		}

		/* (non-Javadoc)
		 * @see org.jdcp.server.classmanager.ClassManager#getClassDigest(java.lang.String)
		 */
		public byte[] getClassDigest(String name) {
			File directory = getClassDirectory(name);
			return FileClassManager.this.getClassDigest(directory, name);
		}

		/* (non-Javadoc)
		 * @see org.jdcp.server.classmanager.ClassManager#setClassDefinition(java.lang.String, java.nio.ByteBuffer)
		 */
		public void setClassDefinition(String name, ByteBuffer def) {
			check();
			writeClass(childDirectory, name, def);
		}

		/* (non-Javadoc)
		 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
		 */
		public ByteBuffer getClassDefinition(String name) {
			File directory = getClassDirectory(name);
			return FileClassManager.this.getClassDefinition(directory, name);
		}

		public FileClassManager getParent() {
			return FileClassManager.this;
		}

		private void release() {
			released = true;
		}

	}

}
