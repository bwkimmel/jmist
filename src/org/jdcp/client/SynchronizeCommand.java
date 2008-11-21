/**
 *
 */
package org.jdcp.client;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

/**
 * @author brad
 *
 */
public final class SynchronizeCommand implements Command {

	/* (non-Javadoc)
	 * @see org.jdcp.client.Command#run(java.util.List, org.jdcp.client.Configuration)
	 */
	public void run(List<String> args, Configuration conf) throws Exception {

		for (String arg: args) {
			verify("", new File(arg), conf);
		}

	}

	public void verify(String pkg, File path, Configuration conf) throws Exception {
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
					}
				}
			}
		}

	}

	private byte[] getDigest(byte[] def, Configuration conf) throws Exception {
		MessageDigest alg = MessageDigest.getInstance(conf.digestAlgorithm);
		return alg.digest(def);
	}

	private byte[] getClassDef(File file, Configuration conf) throws Exception {
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
