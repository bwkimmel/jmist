/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

public final class Int32IndexReader implements IndexReader {

  @Override
  public int read(ByteBuffer buffer, int offset) {
    return buffer.getInt(offset);
  }

}
