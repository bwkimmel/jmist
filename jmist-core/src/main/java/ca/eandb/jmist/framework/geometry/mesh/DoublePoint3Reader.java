/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

import ca.eandb.jmist.math.Point3;

public final class DoublePoint3Reader implements MeshElementReader<Point3> {

  @Override
  public Point3 read(ByteBuffer buffer, int offset) {
    return new Point3(
        buffer.getDouble(offset + 0 * Double.SIZE / 8),
        buffer.getDouble(offset + 1 * Double.SIZE / 8),
        buffer.getDouble(offset + 2 * Double.SIZE / 8));
  }

}
