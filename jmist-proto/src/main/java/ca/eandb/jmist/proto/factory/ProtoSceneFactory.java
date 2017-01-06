/* Copyright (c) 2014 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.proto.factory;

import java.util.List;
import java.util.stream.Collectors;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.accel.BoundingIntervalHierarchy;
import ca.eandb.jmist.framework.geometry.mesh.BufferMesh;
import ca.eandb.jmist.framework.geometry.mesh.IndexReader;
import ca.eandb.jmist.framework.geometry.mesh.MeshGeometry;
import ca.eandb.jmist.framework.geometry.mesh.Point3Format;
import ca.eandb.jmist.framework.geometry.mesh.Vector3Format;
import ca.eandb.jmist.framework.light.CompositeLight;
import ca.eandb.jmist.framework.light.SimpleCompositeLight;
import ca.eandb.jmist.framework.scene.AbstractScene;
import ca.eandb.jmist.framework.scene.MergeSceneElement;
import ca.eandb.jmist.framework.scene.TransformableSceneElement;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.proto.LightProtos;
import ca.eandb.jmist.proto.MeshProtos;
import ca.eandb.jmist.proto.SceneProtos;

/**
 *
 */
public final class ProtoSceneFactory {

  private final ProtoCameraFactory cameraFactory;
  private final ProtoCoreFactory coreFactory;
  private final ProtoLightFactory lightFactory;
  private final ProtoMaterialFactory materialFactory;

  public ProtoSceneFactory(ProtoCameraFactory cameraFactory,
                           ProtoCoreFactory coreFactory,
                           ProtoLightFactory lightFactory,
                           ProtoMaterialFactory materialFactory) {
    this.cameraFactory = cameraFactory;
    this.coreFactory = coreFactory;
    this.lightFactory = lightFactory;
    this.materialFactory = materialFactory;
  }

  public Scene createScene(final SceneProtos.Scene sceneIn) {
    return new AbstractScene() {
      private static final long serialVersionUID = -6778930813957201547L;
      Lens lens = cameraFactory.createLens(sceneIn.getCamera());
      Light light = createSceneLight(sceneIn);
      SceneElement root =
          new BoundingIntervalHierarchy(createSceneRoot(sceneIn));

      @Override
      public Light getLight() {
        return light;
      }

      @Override
      public SceneElement getRoot() {
        return root;
      }

      @Override
      public Lens getLens() {
        return lens;
      }
    };
  }

  private SceneElement createSceneRoot(SceneProtos.Scene sceneIn) {
    List<Material> materials = createSceneMaterials(sceneIn);
    if (sceneIn.getObjectsCount() == 0) {
      return new MergeSceneElement();
    } else if (sceneIn.getObjectsCount() == 1) {
      return createObject(sceneIn.getObjects(0), materials);
    } else {
      MergeSceneElement root = new MergeSceneElement();
      for (SceneProtos.Object objectIn : sceneIn.getObjectsList()) {
        root.addChild(createObject(objectIn, materials));
      }
      return root;
    }
  }

  private IndexReader getIndexReader(MeshProtos.IndexSlice slice) {
    int offset = (int) slice.getOffset();
    switch (slice.getFormat()) {
      case UINT32: return (buffer, base) -> buffer.getInt(base + offset);
      case UINT16:
        return (buffer, base) ->
          Short.toUnsignedInt(buffer.getShort(base + offset));
      case UINT8:
        return (buffer, base) ->
          Byte.toUnsignedInt(buffer.get(base + offset));
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized index format: %d", slice.getFormat().getNumber()));
    }
  }

  private Point3Format getPoint3Format(MeshProtos.VectorSlice.Format format) {
    switch (format) {
      case DOUBLE_XYZ: return Point3Format.DOUBLE_XYZ;
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized Point3 format: %d", format.getNumber()));
    }
  }

  private Vector3Format getVector3Format(MeshProtos.VectorSlice.Format format) {
    switch (format) {
      case DOUBLE_XYZ: return Vector3Format.DOUBLE_XYZ;
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized Vector3 format: %d", format.getNumber()));
    }
  }

  private SceneElement createMesh(
      MeshProtos.Mesh meshIn, List<Material> materials) {
    MeshProtos.MeshFormat format = meshIn.getFormat();
    MeshProtos.VertexBlock vertices = format.getVertices();
    MeshProtos.LoopBlock loops = format.getLoops();
    MeshProtos.FaceBlock faces = format.getFaces();
    BufferMesh.Builder builder = BufferMesh.newBuilder()
        .setCommonBuffer(meshIn.getData().asReadOnlyByteBuffer())
        .setVertexOffset((int) vertices.getOffset())
        .setVertexStride((int) vertices.getStride())
        .setVertexCount(vertices.getCount())
        .setVertexCoordSpec(
            (int) vertices.getCoords().getOffset(),
            getPoint3Format(vertices.getCoords().getFormat()))
        .setVertexNormalSpec(
            (int) vertices.getNormals().getOffset(),
            getVector3Format(vertices.getNormals().getFormat()))
        .setLoopOffset((int) loops.getOffset())
        .setLoopStride((int) loops.getStride())
        .setLoopCount(loops.getCount())
        .setLoopVertexIndexReader(getIndexReader(loops.getIndices()))
        .setLoopNormalSpec(
            (int) loops.getNormals().getOffset(),
            getVector3Format(loops.getNormals().getFormat()))
        .setFaceOffset((int) faces.getOffset())
        .setFaceStride((int) faces.getStride())
        .setFaceCount(faces.getCount())
        .setFaceLoopStartReader(getIndexReader(faces.getLoopStart()))
        .setFaceLoopCountReader(getIndexReader(faces.getLoopCount()))
        .setFaceMaterialIndexReader(getIndexReader(faces.getMaterials()));
    return new MeshGeometry(builder.build(), materials);
  }

  private SceneElement createObject(
      SceneProtos.Object objectIn, List<Material> materials) {
    SceneElement obj;
    switch (objectIn.getType()) {
      case MESH:
        obj = createMesh(objectIn.getMeshObject(), materials);
        break;
      default:
        throw new IllegalArgumentException(String.format(
            "Unrecognized object type: %d", objectIn.getType().getNumber()));
    }

    if (objectIn.getWorldToLocalCount() > 0) {
      AffineMatrix3 transform =
          coreFactory.createAffineMatrix3(objectIn.getWorldToLocalList());
      TransformableSceneElement transformedObj =
          new TransformableSceneElement(obj);
      transformedObj.transform(transform);
      obj = transformedObj;
    }
    return obj;
  }

  private Light createSceneLight(SceneProtos.Scene sceneIn) {
    if (sceneIn.getLightsCount() == 0) {
      return Light.NULL;
    } else if (sceneIn.getLightsCount() == 1) {
      return lightFactory.createLight(sceneIn.getLights(0));
    } else {
      CompositeLight light = new SimpleCompositeLight();
      for (LightProtos.Light lightIn : sceneIn.getLightsList()) {
        light.addChild(lightFactory.createLight(lightIn));
      }
      return light;
    }
  }

  private List<Material> createSceneMaterials(SceneProtos.Scene sceneIn) {
    return sceneIn.getMaterialsList().stream()
        .map(materialFactory::createMaterial)
        .collect(Collectors.toList());
  }
}
