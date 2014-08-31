/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

/**
 * @author bwkimmel
 *
 */
public final class OffsetMeshElementReader<T> implements MeshElementReader<T> {
  
  private final MeshElementReader<T> inner;
  
  private final int offset;
  
  public OffsetMeshElementReader(int offset, MeshElementReader<T> inner) {
    this.inner = inner;
    this.offset = offset;
  }

  @Override
  public T read(ByteBuffer buffer, int base) {
    return inner.read(buffer, base + offset);
  }

}
