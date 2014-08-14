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
package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.TransformedEyeNode;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Lens</code> to which affine transformations may be applied.
 * @author Brad Kimmel
 */
public final class TransformableLens extends AbstractLens implements
    AffineTransformable3 {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 6810985479571317184L;

  /**
   * The <code>Lens</code> to be transformed.
   */
  private final Lens inner;

  /**
   * The transformation to apply to the ray in the view coordinate system to
   * obtain the ray in the world coordinate system.
   */
  private final InvertibleAffineTransformation3 view = new InvertibleAffineTransformation3();

  /**
   * Creates a new <code>TransformableLens</code>.
   * @param inner The <code>Lens</code> to transform.
   */
  public TransformableLens(Lens inner) {
    this.inner = inner;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, ca.eandb.jmist.framework.Random)
   */
  public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj) {
    EyeNode eye = inner.sample(p, pathInfo, ru, rv, rj);
    AffineMatrix3 ltow = view.apply(AffineMatrix3.IDENTITY);
    AffineMatrix3 wtol = view.applyInverse(AffineMatrix3.IDENTITY);
    return new TransformedEyeNode(eye, ltow, wtol);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotate(ca.eandb.jmist.toolkit.Vector3, double)
   */
  public void rotate(Vector3 axis, double angle) {
    view.rotate(axis, angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateX(double)
   */
  public void rotateX(double angle) {
    view.rotateX(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateY(double)
   */
  public void rotateY(double angle) {
    view.rotateY(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Rotatable3#rotateZ(double)
   */
  public void rotateZ(double angle) {
    view.rotateZ(angle);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Scalable#scale(double)
   */
  public void scale(double c) {
    view.scale(c);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretch(double, double, double)
   */
  public void stretch(double cx, double cy, double cz) {
    view.stretch(cx, cy, cz);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Stretchable3#stretch(ca.eandb.jmist.toolkit.Vector3, double)
   */
  public void stretch(Vector3 axis, double c) {
    view.stretch(axis, c);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchX(double)
   */
  public void stretchX(double cx) {
    view.stretchX(cx);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchY(double)
   */
  public void stretchY(double cy) {
    view.stretchY(cy);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AxisStretchable3#stretchZ(double)
   */
  public void stretchZ(double cz) {
    view.stretchZ(cz);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.AffineTransformable3#transform(ca.eandb.jmist.toolkit.AffineMatrix3)
   */
  public void transform(AffineMatrix3 T) {
    view.transform(T);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.LinearTransformable3#transform(ca.eandb.jmist.toolkit.LinearMatrix3)
   */
  public void transform(LinearMatrix3 T) {
    view.transform(T);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Translatable3#translate(ca.eandb.jmist.toolkit.Vector3)
   */
  public void translate(Vector3 v) {
    view.translate(v);
  }

}
