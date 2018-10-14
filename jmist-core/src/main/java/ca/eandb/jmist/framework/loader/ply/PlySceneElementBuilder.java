/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.loader.ply;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>PlyTarget</code> that creates a <code>SceneElement</code> consisting
 * of a single polygonal mesh.
 * @see <a href="http://paulbourke.net/dataformats/ply/">PLY - Polygon File Format</a>
 */
public final class PlySceneElementBuilder implements PlyTarget {

  /** Possible names for the vertex index property on the face element. */
  private static final String[] VERTEX_INDEX_PROP_NAMES = {
    "vertex_index", "vertex_indices"
  };

  /** The result <code>SceneElement</code>. */
  private PolyhedronGeometry geometry = new PolyhedronGeometry();

  /**
   * A value indicating if this mesh has vertex normals (i.e., if the
   * properties <code>nx</code>, <code>ny</code>, and <code>nz</code> are
   * present on the <code>vertex</code> element).
   */
  private boolean hasVertexNormals = false;

  /**
   * A value indicating if this mesh has vertex normals (i.e., if the
   * properties <code>u</code> and <code>v</code> are present on the
   * <code>vertex</code> element).
   */
  private boolean hasTexCoords = false;

  /**
   * Creates the <code>SceneElement</code>.
   * @return The <code>SceneElement</code> read from the PLY-file.
   */
  public SceneElement build() {
    return geometry;
  }

  @Override
  public ElementListener beginSection(ElementDescriptor desc) {

    CompositeElementListener listeners = new CompositeElementListener();
    switch (desc.getName().toLowerCase()) {
    case "vertex":
      hasVertexNormals = desc.hasAllProperties("nx", "ny", "nz");
      if (hasVertexNormals) {
        listeners.addListener(new ElementListener() {
          public void element(PlyElement element) {
            readNormal(element);
          }
        });
      }

      hasTexCoords = desc.hasAllProperties("u", "v");
      if (hasTexCoords) {
        listeners.addListener(new ElementListener() {
          public void element(PlyElement element) {
            readTexCoord(element);
          }
        });
      }

      if (desc.hasAllProperties("red", "green", "blue")) {
        listeners.addListener(new ElementListener() {
          public void element(PlyElement element) {
            readVertexColor(element);
          }
        });
      }

      listeners.addListener(new ElementListener() {
        public void element(PlyElement element) {
          readVertex(element);
        }
      });

      break;

    case "face":
      String propName = null;
      for (String name : VERTEX_INDEX_PROP_NAMES) {
        if (desc.hasProperty(name)) {
          propName = name;
          break;
        }
      }
      if (propName != null) {
        final String name = propName;
        listeners.addListener(new ElementListener() {
          public void element(PlyElement element) {
            readFace(element.getProperty(name));
          }
        });
      }

    }

    return listeners;
  }

  private void readVertex(PlyElement element) {
    double x = element.getProperty("x").getDoubleValue();
    double y = element.getProperty("y").getDoubleValue();
    double z = element.getProperty("z").getDoubleValue();

    geometry.addVertex(new Point3(x, y, z));
  }

  private void readNormal(PlyElement element) {
    double nx = element.getProperty("nx").getDoubleValue();
    double ny = element.getProperty("ny").getDoubleValue();
    double nz = element.getProperty("nz").getDoubleValue();

    geometry.addNormal(new Vector3(nx, ny, nz));
  }

  private void readTexCoord(PlyElement element) {
    double u = element.getProperty("u").getDoubleValue();
    double v = element.getProperty("v").getDoubleValue();

    geometry.addTexCoord(new Point2(u, v));
  }

  private void readVertexColor(PlyElement element) {
    /* not yet implemented. */
  }

  private void readFace(PlyProperty property) {
    int count = property.getCount();
    int face[] = new int[count];
    for (int i = 0; i < count; i++) {
      face[i] = property.getIntValue(i);
    }

    geometry.addFace(face,
        hasTexCoords ? face : null,
        hasVertexNormals ? face : null);
  }

  @Override
  public void endSection() {
    /* nothing to do */
  }

}
