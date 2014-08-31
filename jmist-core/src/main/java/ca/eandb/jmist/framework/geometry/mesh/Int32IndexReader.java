/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

/**
 * @author bwkimmel
 *
 */
public final class Int32IndexReader implements IndexReader {

  @Override
  public int read(ByteBuffer buffer, int offset) {
    return buffer.getInt(offset);
  }

}
