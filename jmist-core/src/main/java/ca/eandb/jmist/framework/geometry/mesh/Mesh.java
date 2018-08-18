/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.stream.IntStream;

import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 *
 */
public interface Mesh {
	
  public interface Vertex {
    Point3 getPosition();
    Vector3 getNormal();
    Point2 getUV();
  }
	
  public interface Face {
    int getVertexCount();
    Vertex getVertex(int index);

    default Iterable<Vertex> getVertices() {
      return () -> IntStream.range(0,  getVertexCount())
          .mapToObj(this::getVertex)
          .iterator();
    }
  }

  int getMaxFaceVertexCount();
  boolean isFaceVertexCountFixed();

  boolean hasVertexNormals();
  boolean hasSplitVertexNormals();
  boolean hasUVs();
  boolean hasSplitUVs();

  int getFaceCount();
  Face getFace(int index);

  default Iterable<Face> getFaces() {
    return () -> IntStream.range(0, getFaceCount())
        .mapToObj(this::getFace)
        .iterator();
  }

  int getVertexCount();
  Vertex getVertex(int index);

  default Iterable<Vertex> getVertices() {
    return () -> IntStream.range(0, getVertexCount())
        .mapToObj(this::getVertex)
        .iterator();
  }

}
