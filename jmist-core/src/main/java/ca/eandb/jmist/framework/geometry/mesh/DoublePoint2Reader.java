/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

import ca.eandb.jmist.math.Point2;

/**
 * @author bwkimmel
 *
 */
public final class DoublePoint2Reader implements MeshElementReader<Point2> {

  @Override
  public Point2 read(ByteBuffer buffer, int offset) {
    return new Point2(
        buffer.getDouble(offset + 0 * Double.SIZE / 8),
        buffer.getDouble(offset + 1 * Double.SIZE / 8));
  }

}
