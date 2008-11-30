/**
 *
 */
package org.selfip.bkimmel.io;

import java.io.File;

/**
 * @author brad
 *
 */
public interface FileVisitor {

	boolean visit(File file) throws Exception;

}
