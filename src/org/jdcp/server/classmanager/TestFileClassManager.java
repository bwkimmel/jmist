/**
 *
 */
package org.jdcp.server.classmanager;

import java.io.File;
import java.nio.ByteBuffer;

import org.selfip.bkimmel.util.StringUtil;

/**
 * @author brad
 *
 */
public final class TestFileClassManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		printBytes(StringUtil.hexToByteArray("0123456789ABCDEF"));

		File root = new File("C:\\test");
		FileClassManager cm = new FileClassManager(root);


		cm.setClassDefinition("Test1", StringUtil.hexToByteArray("0123456789ABCDEF"));
		printBytes(cm.getClassDefinition("Test1"));
		printBytes(cm.getClassDigest("Test1"));

		ClassManager child = cm.createChildClassManager();
		cm.setClassDefinition("Test1", StringUtil.hexToByteArray("FEDCBA9876543210"));
		cm.setClassDefinition("Test1", StringUtil.hexToByteArray("FEDCBA9876543210"));
		printBytes(cm.getClassDefinition("Test1"));
		printBytes(cm.getClassDigest("Test1"));
		printBytes(child.getClassDefinition("Test1"));
		printBytes(child.getClassDigest("Test1"));

		cm.releaseChildClassManager(child);

		child.getClassDefinition("Test1");

	}

	private static void printBytes(byte[] bytes) {
		System.out.println(StringUtil.toHex(bytes));
	}

	private static void printBytes(ByteBuffer bytes) {
		byte[] array = new byte[bytes.remaining()];
		bytes.get(array);
		printBytes(array);
	}

}
