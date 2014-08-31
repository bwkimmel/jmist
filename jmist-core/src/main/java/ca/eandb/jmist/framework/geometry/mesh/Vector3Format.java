/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public enum Vector3Format {
  DOUBLE_XYZ(new DoubleVector3Reader());
  
  private final MeshElementReader<Vector3> reader;
  
  private Vector3Format(MeshElementReader<Vector3> reader) {
    this.reader = reader;
  }
  
  public MeshElementReader<Vector3> createReader(int offset) {
    return offset == 0
        ? reader : new OffsetMeshElementReader<Vector3>(offset, reader);
  }
}
