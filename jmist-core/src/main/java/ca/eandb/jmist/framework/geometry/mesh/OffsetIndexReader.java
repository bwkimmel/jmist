/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

/**
 * @author bwkimmel
 *
 */
public final class OffsetIndexReader implements IndexReader {

  private final IndexReader inner;

  private final int offset;

  public OffsetIndexReader(int offset, IndexReader inner) {
    this.inner = inner;
    this.offset = offset;
  }

  @Override
  public int read(ByteBuffer buffer, int base) {
    return inner.read(buffer, base + offset);
  }

}
