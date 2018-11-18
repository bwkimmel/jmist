/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import ca.eandb.jmist.math.Point2;

public enum Point2Format {
  DOUBLE_XY(new DoublePoint2Reader());

  private final MeshElementReader<Point2> reader;

  private Point2Format(MeshElementReader<Point2> reader) {
    this.reader = reader;
  }

  public MeshElementReader<Point2> createReader(int offset) {
    return offset == 0
        ? reader : new OffsetMeshElementReader<>(offset, reader);
  }
}
