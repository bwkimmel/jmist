/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

/**
 * @author bwkimmel
 *
 */
public interface MeshElementReader<T> {

  T read(ByteBuffer buffer, int offset);

}
