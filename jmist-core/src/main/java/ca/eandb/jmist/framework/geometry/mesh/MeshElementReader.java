/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

public interface MeshElementReader<T> {

  T read(ByteBuffer buffer, int offset);

}
