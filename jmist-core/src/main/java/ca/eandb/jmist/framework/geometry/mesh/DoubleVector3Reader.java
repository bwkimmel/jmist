/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;

import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public final class DoubleVector3Reader implements MeshElementReader<Vector3> {

  @Override
  public Vector3 read(ByteBuffer buffer, int offset) {
    return new Vector3(
        buffer.getDouble(offset + 0 * Double.SIZE / 8),
        buffer.getDouble(offset + 1 * Double.SIZE / 8),
        buffer.getDouble(offset + 2 * Double.SIZE / 8));
  }

}
