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
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.Texture3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Texture2</code> that represents a slice of a <code>Texture3</code>.
 * @author Brad Kimmel
 */
public final class SliceTexture2 implements Texture2 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8157705084047279308L;

  /** The <code>Texture3</code> to slice. */
  private final Texture3 source;

  /**
   * The <code>Point3</code> on {@link #source} that maps to the origin for
   * this <code>Texture2</code>.
   */
  private final Point3 origin;

  /** The first basis vector for the slice. */
  private final Vector3 u;

  /** The second basis vector for the slice. */
  private final Vector3 v;

  /**
   * Creates a new <code>SliceTexture2</code>.
   * @param origin The <code>Point3</code> on {@link #source} that maps to
   *     the origin for this <code>Texture2</code>.
   * @param u The first basis vector.
   * @param v The second basis vector.
   * @param source The <code>Texture3</code> to slice.
   */
  public SliceTexture2(Point3 origin, Vector3 u, Vector3 v, Texture3 source) {
    this.origin = origin;
    this.u = u;
    this.v = v;
    this.source = source;
  }

  /**
   * Creates a new <code>SliceTexture2</code>.
   * @param origin The <code>Point3</code> on {@link #source} that maps to
   *     the origin for this <code>Texture2</code>.
   * @param basis The <code>Basis3</code> describing the orientation of the
   *     slice.
   * @param source The <code>Texture3</code> to slice.
   */
  public SliceTexture2(Point3 origin, Basis3 basis, Texture3 source) {
    this(origin, basis.u(), basis.v(), source);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color evaluate(Point2 p, WavelengthPacket lambda) {
    Point3 q = origin.plus(u.times(p.x())).plus(v.times(p.y()));
    return source.evaluate(q, lambda);
  }

}
