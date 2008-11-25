/**
 *
 */
package org.jdcp.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.selfip.bkimmel.util.UnexpectedException;
import org.selfip.bkimmel.util.args.AbstractCommand;

/**
 * @author brad
 *
 */
public final class SynchronizeCommand extends AbstractCommand<Configuration> {

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.args.AbstractCommand#run(java.lang.String[], java.lang.Object)
	 */
	@Override
	protected void run(String[] args, Configuration conf) {
		for (String arg : args) {
			verify("", new File(arg), conf);
		}
	}

	public void verify(String pkg, File path, Configuration conf) {
		if (!path.isDirectory()) {
			throw new IllegalArgumentException(path.getAbsolutePath().concat(" is not a directory."));
		}

		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				verify(combine(pkg, file.getName()), file, conf);
			} else {
				String fileName = file.getName();
				int extensionSeparator = fileName.lastIndexOf('.');
				if (extensionSeparator >= 0) {
					String extension = fileName.substring(extensionSeparator + 1);
					if (extension.equals("class")) {
						String className = combine(pkg, fileName.substring(0, extensionSeparator));
						try {
							byte[] digest = conf.getJobService().getClassDigest(className);
							byte[] def = getClassDef(file, conf);
							byte[] localDigest = getDigest(def, conf);
							if (digest == null || !Arrays.equals(digest, localDigest)) {
								conf.getJobService().setClassDefinition(className, def);
								System.out.print(digest == null ? "+ " : "U ");
								System.out.println(className);
							} else if (conf.verbose) {
								System.out.print("= ");
								System.out.println(className);
							}
						} catch (FileNotFoundException e) {
							throw new UnexpectedException(e);
						} catch (IOException e) {
							System.out.print("E ");
							System.out.println(className);
						}
					}
				}
			}
		}

	}

	private byte[] getDigest(byte[] def, Configuration conf) {
		try {
			MessageDigest alg = MessageDigest.getInstance(conf.digestAlgorithm);
			return alg.digest(def);
		} catch (NoSuchAlgorithmException e) {
			throw new UnexpectedException(e);
		}
	}

	private byte[] getClassDef(File file, Configuration conf) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		byte[] def = new byte[(int) file.length()];
		stream.read(def);
		stream.close();
		return def;
	}

	private String combine(String parent, String child) {
		if (parent.length() > 0) {
			return parent.concat(".").concat(child);
		} else {
			return child;
		}
	}

}
