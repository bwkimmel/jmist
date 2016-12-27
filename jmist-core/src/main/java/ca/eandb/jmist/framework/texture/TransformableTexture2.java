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

import ca.eandb.jmist.framework.AffineTransformable2;
import ca.eandb.jmist.framework.InvertibleAffineTransformation2;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.AffineMatrix2;
import ca.eandb.jmist.math.LinearMatrix2;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector2;

/**
 * A decorator <code>Texture2</code> that applies an affine transformation to
 * the decorated <code>Texture2</code>.
 *
 * @author Brad Kimmel
 */
public final class TransformableTexture2 implements Texture2, AffineTransformable2 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 9136935061345736572L;

  /** The <code>Texture2</code> to be transformed. */
  private final Texture2 inner;

  /** The transformation to apply to the texture. */
  private final InvertibleAffineTransformation2 transform = new InvertibleAffineTransformation2();

  /**
   * Creates a new <code>TransformableTexture2</code>.
   * @param inner The <code>Texture2</code> to make transformable.
   */
  public TransformableTexture2(Texture2 inner) {
    this.inner = inner;
  }

  @Override
  public Color evaluate(Point2 p, WavelengthPacket lambda) {
    return inner.evaluate(transform.applyInverse(p), lambda);
  }

  @Override
  public void rotate(double angle) {
    transform.rotate(angle);
  }

  @Override
  public void scale(double c) {
    transform.scale(c);
  }

  @Override
  public void stretch(double cx, double cy) {
    transform.stretch(cx, cy);
  }

  @Override
  public void stretch(Vector2 axis, double c) {
    transform.stretch(axis, c);
  }

  @Override
  public void stretchX(double cx) {
    transform.stretchX(cx);
  }

  @Override
  public void stretchY(double cy) {
    transform.stretchY(cy);
  }

  @Override
  public void transform(AffineMatrix2 T) {
    transform.transform(T);
  }

  @Override
  public void transform(LinearMatrix2 T) {
    transform.transform(T);
  }

  @Override
  public void translate(Vector2 v) {
    transform.translate(v);
  }

}
