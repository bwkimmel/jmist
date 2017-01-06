/**
 *
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 *
 */
public final class BufferMesh implements Mesh {

  private final ByteBuffer faceBuffer;

  private final ByteBuffer loopBuffer;

  private final ByteBuffer vertexBuffer;

  private final int maxFaceVertexCount;

  private final int faceCount;

  private final int faceOffset;

  private final int faceStride;

  private final int loopCount;

  private final int loopOffset;

  private final int loopStride;

  private final int vertexCount;

  private final int vertexOffset;

  private final int vertexStride;

  private final IndexReader faceLoopStartReader;

  private final IndexReader faceLoopCountReader;

  private final IndexReader faceMaterialIndexReader;

  private final IndexReader loopVertexIndexReader;

  private final MeshElementReader<Vector3> loopNormalReader;

  private final MeshElementReader<Point2> loopUVReader;

  private final MeshElementReader<Point3> vertexCoordReader;

  private final MeshElementReader<Vector3> vertexNormalReader;

  private final MeshElementReader<Point2> vertexUVReader;

  public static final class Builder {
    private Builder() {}

    private ByteBuffer faceBuffer;
    private ByteBuffer loopBuffer;
    private ByteBuffer vertexBuffer;
    private int maxFaceVertexCount;
    private int faceCount;
    private int faceOffset;
    private int faceStride;
    private int loopCount;
    private int loopOffset;
    private int loopStride;
    private int vertexCount;
    private int vertexOffset;
    private int vertexStride;
    private IndexReader faceLoopStartReader;
    private IndexReader faceLoopCountReader;
    private IndexReader faceMaterialIndexReader;
    private IndexReader loopVertexIndexReader;
    private MeshElementReader<Vector3> loopNormalReader;
    private MeshElementReader<Point2> loopUVReader;
    private MeshElementReader<Point3> vertexCoordReader;
    private MeshElementReader<Vector3> vertexNormalReader;
    private MeshElementReader<Point2> vertexUVReader;

    public BufferMesh build() {
      return new BufferMesh(faceBuffer, loopBuffer, vertexBuffer,
          maxFaceVertexCount, faceCount, faceOffset, faceStride, loopCount,
          loopOffset, loopStride, vertexCount, vertexOffset, vertexStride,
          faceLoopStartReader, faceLoopCountReader, faceMaterialIndexReader,
          loopVertexIndexReader, loopNormalReader, loopUVReader,
          vertexCoordReader, vertexNormalReader, vertexUVReader);
    }
    public Builder setCommonBuffer(ByteBuffer buffer) {
      faceBuffer = loopBuffer = vertexBuffer = buffer.duplicate();
      return this;
    }
    public Builder setFaceBuffer(ByteBuffer faceBuffer) {
      this.faceBuffer = faceBuffer.duplicate();
      return this;
    }
    public Builder setLoopBuffer(ByteBuffer loopBuffer) {
      this.loopBuffer = loopBuffer.duplicate();
      return this;
    }
    public Builder setVertexBuffer(ByteBuffer vertexBuffer) {
      this.vertexBuffer = vertexBuffer.duplicate();
      return this;
    }
    public Builder setMaxFaceVertexCount(int maxFaceVertexCount) {
      this.maxFaceVertexCount = maxFaceVertexCount;
      return this;
    }
    public Builder setFaceCount(int faceCount) {
      this.faceCount = faceCount;
      return this;
    }
    public Builder setFaceOffset(int faceOffset) {
      this.faceOffset = faceOffset;
      return this;
    }
    public Builder setFaceStride(int faceStride) {
      this.faceStride = faceStride;
      return this;
    }
    public Builder setLoopCount(int loopCount) {
      this.loopCount = loopCount;
      return this;
    }
    public Builder setLoopOffset(int loopOffset) {
      this.loopOffset = loopOffset;
      return this;
    }
    public Builder setLoopStride(int loopStride) {
      this.loopStride = loopStride;
      return this;
    }
    public Builder setVertexCount(int vertexCount) {
      this.vertexCount = vertexCount;
      return this;
    }
    public Builder setVertexOffset(int vertexOffset) {
      this.vertexOffset = vertexOffset;
      return this;
    }
    public Builder setVertexStride(int vertexStride) {
      this.vertexStride = vertexStride;
      return this;
    }
    public Builder setFaceLoopStartReader(IndexReader faceLoopStartReader) {
      this.faceLoopStartReader = faceLoopStartReader;
      return this;
    }
    public Builder setFaceLoopCountReader(IndexReader faceLoopCountReader) {
      this.faceLoopCountReader = faceLoopCountReader;
      return this;
    }
    public Builder setFaceMaterialIndexReader(IndexReader faceMaterialIndexReader) {
      this.faceMaterialIndexReader = faceMaterialIndexReader;
      return this;
    }
    public Builder setLoopVertexIndexReader(IndexReader loopVertexIndexReader) {
      this.loopVertexIndexReader = loopVertexIndexReader;
      return this;
    }
    public Builder setLoopNormalReader(MeshElementReader<Vector3> loopNormalReader) {
      this.loopNormalReader = loopNormalReader;
      return this;
    }
    public Builder setLoopUVReader(MeshElementReader<Point2> loopUVReader) {
      this.loopUVReader = loopUVReader;
      return this;
    }
    public Builder setVertexCoordReader(MeshElementReader<Point3> vertexCoordReader) {
      this.vertexCoordReader = vertexCoordReader;
      return this;
    }
    public Builder setVertexNormalReader(MeshElementReader<Vector3> vertexNormalReader) {
      this.vertexNormalReader = vertexNormalReader;
      return this;
    }
    public Builder setVertexUVReader(MeshElementReader<Point2> vertexUVReader) {
      this.vertexUVReader = vertexUVReader;
      return this;
    }

    public Builder setFaceLoopStartSpec(int offset, IndexFormat format) {
      return setFaceLoopStartReader(format.createReader(offset));
    }
    public Builder setFaceLoopCountSpec(int offset, IndexFormat format) {
      return setFaceLoopCountReader(format.createReader(offset));
    }
    public Builder setFaceMaterialIndexSpec(int offset, IndexFormat format) {
      return setFaceMaterialIndexReader(format.createReader(offset));
    }
    public Builder setLoopVertexIndexSpec(int offset, IndexFormat format) {
      return setLoopVertexIndexReader(format.createReader(offset));
    }
    public Builder setLoopNormalSpec(int offset, Vector3Format format) {
      return setLoopNormalReader(format.createReader(offset));
    }
    public Builder setLoopUVSpec(int offset, Point2Format format) {
      return setLoopUVReader(format.createReader(offset));
    }
    public Builder setVertexCoordSpec(int offset, Point3Format format) {
      return setVertexCoordReader(format.createReader(offset));
    }
    public Builder setVertexNormalSpec(int offset, Vector3Format format) {
      return setVertexNormalReader(format.createReader(offset));
    }
    public Builder setVertexUVSpec(int offset, Point2Format format) {
      return setVertexUVReader(format.createReader(offset));
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private BufferMesh(ByteBuffer faceBuffer, ByteBuffer loopBuffer,
      ByteBuffer vertexBuffer, int maxFaceVertexCount, int faceCount,
      int faceOffset, int faceStride, int loopCount, int loopOffset,
      int loopStride, int vertexCount, int vertexOffset, int vertexStride,
      IndexReader faceLoopStartReader, IndexReader faceLoopCountReader,
      IndexReader faceMaterialIndexReader, IndexReader loopVertexIndexReader,
      MeshElementReader<Vector3> loopNormalReader,
      MeshElementReader<Point2> loopUVReader,
      MeshElementReader<Point3> vertexCoordReader,
      MeshElementReader<Vector3> vertexNormalReader,
      MeshElementReader<Point2> vertexUVReader) {
    this.faceBuffer = faceBuffer;
    this.loopBuffer = loopBuffer;
    this.vertexBuffer = vertexBuffer;
    this.maxFaceVertexCount = maxFaceVertexCount;
    this.faceCount = faceCount;
    this.faceOffset = faceOffset;
    this.faceStride = faceStride;
    this.loopCount = loopCount;
    this.loopOffset = loopOffset;
    this.loopStride = loopStride;
    this.vertexCount = vertexCount;
    this.vertexOffset = vertexOffset;
    this.vertexStride = vertexStride;
    this.faceLoopStartReader = faceLoopStartReader;
    this.faceLoopCountReader = faceLoopCountReader;
    this.faceMaterialIndexReader = faceMaterialIndexReader;
    this.loopVertexIndexReader = loopVertexIndexReader;
    this.loopNormalReader = loopNormalReader;
    this.loopUVReader = loopUVReader;
    this.vertexCoordReader = vertexCoordReader;
    this.vertexNormalReader = vertexNormalReader;
    this.vertexUVReader = vertexUVReader;
  }

  private final class MeshVertex implements Mesh.Vertex {

    private final int vertexBase;

    public MeshVertex(int vertexBase) {
      this.vertexBase = vertexBase;
    }

    @Override
    public Point3 getPosition() {
      return vertexCoordReader.read(vertexBuffer, vertexBase);
    }

    @Override
    public Vector3 getNormal() {
      return vertexNormalReader != null
          ? vertexNormalReader.read(vertexBuffer, vertexBase)
          : Vector3.ZERO;
    }

    @Override
    public Point2 getUV() {
      return vertexUVReader != null
          ? vertexUVReader.read(vertexBuffer, vertexBase)
          : Point2.ORIGIN;
    }

  }

  @Override
  public int getVertexCount() {
    return vertexCount;
  }

  @Override
  public Vertex getVertex(int index) {
    if (index < 0 || index >= vertexCount) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return new MeshVertex(vertexOffset + index * vertexStride);
  }

  @Override
  public Iterable<Vertex> getVertices() {
    int vertexLimit = vertexOffset + vertexCount * vertexStride;
    return () -> new Iterator<Vertex>() {
      int offset = vertexOffset;

      @Override
      public boolean hasNext() {
        return offset != vertexLimit;
      }

      @Override
      public Vertex next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Vertex vertex = new MeshVertex(offset);
        offset += vertexStride;
        return vertex;
      }
    };
  }

  @Override
  public int getFaceCount() {
    return faceCount;
  }

  @Override
  public Face getFace(int index) {
    if (index < 0 || index >= faceCount) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return new MeshFace(faceOffset + index * faceStride);
  }

  @Override
  public Iterable<Face> getFaces() {
    int faceLimit = faceOffset + faceCount * faceStride;
    return () -> new Iterator<Face>() {
      int offset = faceOffset;

      @Override
      public boolean hasNext() {
        return offset != faceLimit;
      }

      @Override
      public Face next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Face face = new MeshFace(offset);
        offset += faceStride;
        return face;
      }
    };
  }

  private final class MeshFace implements Mesh.Face {

    private final int faceBase;

    public MeshFace(int faceBase) {
      this.faceBase = faceBase;
    }

    private final class FaceVertex implements Mesh.Vertex {

      private final int loopBase;

      public FaceVertex(int loopBase) {
        this.loopBase = loopBase;
      }

      private Vertex getMeshVertex() {
        return BufferMesh.this.getVertex(
            loopVertexIndexReader.read(loopBuffer, loopBase));
      }

      @Override
      public Point3 getPosition() {
        return getMeshVertex().getPosition();
      }

      @Override
      public Vector3 getNormal() {
        return loopNormalReader != null
            ? loopNormalReader.read(loopBuffer, loopBase)
            : getMeshVertex().getNormal();
      }

      @Override
      public Point2 getUV() {
        return loopUVReader != null
            ? loopUVReader.read(loopBuffer, loopBase)
            : getMeshVertex().getUV();
      }

    }

    @Override
    public int getVertexCount() {
      return faceLoopCountReader != null
          ? faceLoopCountReader.read(faceBuffer, faceBase)
          : maxFaceVertexCount;
    }

    @Override
    public Vertex getVertex(int index) {
      if (index < 0 || index >= getVertexCount()) {
        throw new IllegalArgumentException("index out of bounds");
      }

      int loopIndex = faceLoopStartReader.read(faceBuffer, faceBase) + index;
      if (loopIndex < 0 || loopIndex >= loopCount) {
        throw new IllegalArgumentException("loopIndex out of bounds");
      }

      int loopBase = loopOffset + loopIndex * loopStride;
      return new FaceVertex(loopBase);
    }

    @Override
    public Iterable<Vertex> getVertices() {
      int loopLimit = loopOffset + getVertexCount() * loopStride;
      return () -> new Iterator<Vertex>() {
        int offset = loopOffset;
        @Override
        public boolean hasNext() {
          return offset != loopLimit;
        }

        @Override
        public Vertex next() {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          Vertex vertex = new FaceVertex(offset);
          offset += loopStride;
          return vertex;
        }
      };
    }

    @Override
    public int getMaterialIndex() {
      return faceMaterialIndexReader != null
          ? faceMaterialIndexReader.read(faceBuffer, faceBase)
          : 0;
    }

  }

  @Override
  public int getMaxFaceVertexCount() {
    return maxFaceVertexCount;
  }

  @Override
  public boolean isFaceVertexCountFixed() {
    return faceLoopCountReader == null;
  }

  @Override
  public boolean hasVertexNormals() {
    return vertexNormalReader != null || loopNormalReader != null;
  }

  @Override
  public boolean hasSplitVertexNormals() {
    return loopNormalReader != null;
  }

  @Override
  public boolean hasUVs() {
    return vertexUVReader != null || loopUVReader != null;
  }

  @Override
  public boolean hasSplitUVs() {
    return loopUVReader != null;
  }

}
