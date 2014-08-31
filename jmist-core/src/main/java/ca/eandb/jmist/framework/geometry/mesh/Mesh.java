/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

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
    Iterable<Vertex> getVertices();
  }
  
  int getMaxFaceVertexCount();
  boolean isFaceVertexCountFixed();
  
  boolean hasVertexNormals();
  boolean hasSplitVertexNormals();
  boolean hasUVs();
  boolean hasSplitUVs();

  int getFaceCount();
  Face getFace(int index);
  Iterable<Face> getFaces();
  
  int getVertexCount();
  Vertex getVertex(int index);
  Iterable<Vertex> getVertices();
  
}
