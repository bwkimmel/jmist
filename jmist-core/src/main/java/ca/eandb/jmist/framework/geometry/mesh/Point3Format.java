/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import ca.eandb.jmist.math.Point3;

/**
 * @author bwkimmel
 *
 */
public enum Point3Format {
  DOUBLE_XYZ(new DoublePoint3Reader());

  private final MeshElementReader<Point3> reader;

  private Point3Format(MeshElementReader<Point3> reader) {
    this.reader = reader;
  }

  public MeshElementReader<Point3> createReader(int offset) {
    return offset == 0
        ? reader : new OffsetMeshElementReader<Point3>(offset, reader);
  }
}
