/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.util.Iterator;

import ca.eandb.util.ByteArray;
import ca.eandb.util.IntegerArray;

/**
 * @author bwkimmel
 *
 */
final class TriangulatedMesh implements Mesh {

  private final Mesh mesh;

  private final IntegerArray faceIndices = new IntegerArray();

  private final ByteArray keyVertexIndices = new ByteArray();

  TriangulatedMesh(Mesh mesh) {
    this.mesh = mesh;
    prepare();
  }

  private void prepare() {
    int numTriangles = 0;
    for (Mesh.Face face : mesh.getFaces()) {
      int numFaceTriangles = face.getVertexCount() - 2;
      if (numFaceTriangles > 255) {
        throw new IllegalArgumentException(
            "faces may not exceed 255 triangles");
      }
      numTriangles += face.getVertexCount() - 2;
    }

    faceIndices.ensureCapacity(numTriangles);
    keyVertexIndices.ensureCapacity(numTriangles);

    int faceIndex = 0;
    for (Mesh.Face face : mesh.getFaces()) {
      int numFaceTriangles = face.getVertexCount() - 2;
      for (int keyVertexIndex = 1; keyVertexIndex <= numFaceTriangles;
           keyVertexIndex++) {
        faceIndices.add(faceIndex);
        keyVertexIndices.add((byte) keyVertexIndex);
      }
      faceIndex++;
    }
  }

  @Override
  public int getMaxFaceVertexCount() {
    return 3;
  }

  @Override
  public boolean isFaceVertexCountFixed() {
    return true;
  }

  @Override
  public boolean hasVertexNormals() {
    return mesh.hasVertexNormals();
  }

  @Override
  public boolean hasSplitVertexNormals() {
    return mesh.hasSplitVertexNormals();
  }

  @Override
  public boolean hasUVs() {
    return mesh.hasUVs();
  }

  @Override
  public boolean hasSplitUVs() {
    return mesh.hasSplitUVs();
  }

  @Override
  public int getFaceCount() {
    return faceIndices.size();
  }

  @Override
  public Face getFace(int index) {
    return new Triangle(mesh.getFace(faceIndices.get(index)),
                        Byte.toUnsignedInt(keyVertexIndices.get(index)));
  }

  @Override
  public Iterable<Face> getFaces() {
    return () -> new Iterator<Face>() {
      Iterator<Face> inner = mesh.getFaces().iterator();
      Mesh.Face face;
      int numFaceVertices;
      int keyVertexIndex;

      @Override
      public boolean hasNext() {
        return (keyVertexIndex < numFaceVertices - 1) || inner.hasNext();
      }

      @Override
      public Face next() {
        if (keyVertexIndex >= numFaceVertices - 1) {
          face = inner.next();
          numFaceVertices = face.getVertexCount();
          keyVertexIndex = 1;
        }
        return new Triangle(face, keyVertexIndex++);
      }
    };
  }

  @Override
  public int getVertexCount() {
    return mesh.getVertexCount();
  }

  @Override
  public Vertex getVertex(int index) {
    return mesh.getVertex(index);
  }

  @Override
  public Iterable<Vertex> getVertices() {
    return mesh.getVertices();
  }

  private final class Triangle implements Mesh.Face {
    private final Mesh.Face face;
    private final int keyVertexIndex;

    public Triangle(Mesh.Face face, int keyVertexIndex) {
      this.face = face;
      this.keyVertexIndex = keyVertexIndex;
    }

    @Override
    public int getVertexCount() {
      return 3;
    }

    @Override
    public Vertex getVertex(int index) {
      switch (index) {
        case 0: return face.getVertex(0);
        case 1: return face.getVertex(keyVertexIndex);
        case 2: return face.getVertex(keyVertexIndex + 1);
        default:
          throw new IllegalArgumentException("vertex index out of bounds");
      }
    }
  }

}
