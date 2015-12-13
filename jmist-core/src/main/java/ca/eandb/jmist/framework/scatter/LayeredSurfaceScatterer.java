/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.scatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> constructed by layering child
 * <code>SurfaceScatterer</code>s.
 * @author Brad Kimmel
 */
public final class LayeredSurfaceScatterer implements SurfaceScatterer {

  /** Serialization version ID. */
  private static final long serialVersionUID = -8457484575034697420L;

  /**
   * A <code>List</code> of the <code>SurfaceScatterer</code>s representing
   * the layers of this <code>SurfaceScatterer</code>.
   */
  private final List<SurfaceScatterer> layers = new ArrayList<SurfaceScatterer>();

  /**
   * Adds a <code>SurfaceScatterer</code> as a new layer on the top.
   * @param e The <code>SurfaceScatterer</code> to add as the new top layer.
   * @return This <code>LayeredSurfaceScatterer</code>.
   */
  public LayeredSurfaceScatterer addLayerToTop(SurfaceScatterer e) {
    layers.add(0, e);
    return this;
  }

  /**
   * Adds a <code>SurfaceScatterer</code> as a new layer on the bottom.
   * @param e The <code>SurfaceScatterer</code> to add as the new bottom
   *     layer.
   * @return This <code>LayeredSurfaceScatterer</code>.
   */
  public LayeredSurfaceScatterer addLayerToBottom(SurfaceScatterer e) {
    layers.add(e);
    return this;
  }

  /** Removes all layers. */
  public void clear() {
    layers.clear();
  }

  /**
   * Gets the number of layers.
   * @return The number of layers.
   */
  public int getNumLayers() {
    return layers.size();
  }

  /**
   * Gets the <code>List</code> of layers that comprise this
   * <code>LayeredSurfaceScatterer</code>.  The returned list is
   * unmodifiable.
   * @return The <code>List</code> of layers that comprise this
   *     <code>LayeredSurfaceScatterer</code>.
   */
  public List<SurfaceScatterer> getLayers() {
    return Collections.unmodifiableList(layers);
  }

  @Override
  public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
      double lambda, Random rnd) {

    Vector3 N = x.getNormal();
    int  depth = (v.dot(N) > 0.0) ? (layers.size() - 1) : 0;
    int dir;

    do  {

      SurfaceScatterer layer = layers.get(depth);
      v = layer.scatter(x, v, adjoint, lambda, rnd);

      if (v == null) break;

      dir = (v.dot(N) > 0.0) ? -1 : 1;
      depth += dir;

    } while (depth >= 0 && depth < layers.size());

    return v;
  }

}
