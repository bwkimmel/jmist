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
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * An orthonormal basis in three dimensional space.
 * @author Brad Kimmel
 */
public final class Basis3 implements Serializable {

  /**
   * Creates a new <code>Basis3</code>.  This constructor assumes that the
   * three vectors provided form an orthonormal basis (i.e., each is of unit
   * length and the three vectors are pairwise orthogonal).
   * @param u The first tangent <code>Vector3</code>.
   * @param v The second tangent <code>Vector3</code>.
   * @param w The normal <code>Vector3</code>.
   */
  private Basis3(Vector3 u, Vector3 v, Vector3 w) {
    this.u = u;
    this.v = v;
    this.w = w;
  }

  /**
   * Indicates whether a <code>Basis3</code> is left or right handed.
   * @author Brad Kimmel
   */
  public enum Orientation {

    /**
     * The cross product of <code>u</code> and <code>v</code> points in the
     * same direction as <code>w</code>.
     */
    RIGHT_HANDED,

    /**
     * The cross product of <code>u</code> and <code>v</code> points in the
     * opposite direction to <code>w</code>.
     */
    LEFT_HANDED

  }

  /**
   * Gets the first tangent <code>Vector3</code>.
   * @return The first tangent <code>Vector3</code>.
   */
  public Vector3 u() {
    return this.u;
  }

  /**
   * Gets the second tangent <code>Vector3</code>.
   * @return The second tangent <code>Vector3</code>.
   */
  public Vector3 v() {
    return this.v;
  }

  /**
   * Gets the normal <code>Vector3</code>.
   * @return The normal <code>Vector3</code>.
   */
  public Vector3 w() {
    return this.w;
  }

  /**
   * Indicates if this <code>Basis3</code> is left or right handed.
   * @return A value indicating if this <code>Basis3</code> is left or right
   *     handed.
   */
  public Orientation orientation() {
    return u.cross(v).dot(w) > 0.0 ? Orientation.RIGHT_HANDED
        : Orientation.LEFT_HANDED;
  }

  /**
   * Re-expresses the specified vector (expressed using the standard basis)
   * using this basis.
   * Equivalent to <code>fromStandard(new Vector3(tu, tv, tw))</code>.
   * @param tu The first component of the vector to re-express.
   * @param tv The second component of the vector to re-express.
   * @param tw The third component of the vector to re-express.
   * @return The re-expressed <code>Vector3</code>.
   * @see #fromStandard(Vector3)
   * @see Basis3#STANDARD
   */
  public Vector3 fromStandard(double tu, double tv, double tw) {
    return new Vector3(
        tu * u.x() + tv * u.y() + tw * u.z(),
        tu * v.x() + tv * v.y() + tw * v.z(),
        tu * w.x() + tv * w.y() + tw * w.z());
  }

  /**
   * Re-expresses the specified <code>Vector3</code> (expressed using the
   * standard basis) using this basis.
   * Equivalent to <code>fromStandard(r.x(), r.y(), r.z())</code>.
   * @param r The <code>Vector3</code> expressed using the standard basis.
   * @return The <code>Vector3</code> re-expressed using this basis.
   * @see #fromStandard(double, double, double)
   * @see Basis3#STANDARD
   */
  public Vector3 fromStandard(Vector3 r) {
    return new Vector3(r.dot(u), r.dot(v), r.dot(w));
  }

  /**
   * Re-expresses the specified vector using the standard basis.
   * Equivalent to <code>toStandard(new Vector3(tu, tv, tw))</code>.
   * @param tu The first component of the vector to re-express.
   * @param tv The second component of the vector to re-express.
   * @param tw The third component of the vector to re-express.
   * @return The re-expressed <code>Vector3</code>.
   * @see #toStandard(Vector3)
   * @see Basis3#STANDARD
   */
  public Vector3 toStandard(double tu, double tv, double tw) {
    return new Vector3(
        tu * u.x() + tv * v.x() + tw * w.x(),
        tu * u.y() + tv * v.y() + tw * w.y(),
        tu * u.z() + tv * v.z() + tw * w.z());
  }

  /**
   * Re-expresses the specified <code>Vector3</code> using the standard
   * basis.
   * Equivalent to <code>toVector(r.x(), r.y(), r.z())</code>.
   * @param r The <code>Vector3</code> to re-express.
   * @return The re-expressed <code>Vector3</code>.
   * @see #toStandard(double, double, double)
   * @see Basis3#STANDARD
   */
  public Vector3 toStandard(Vector3 r) {
    return toStandard(r.x(), r.y(), r.z());
  }

  /**
   * Gets the <code>Basis3</code> opposite from this one (i.e., where each
   * basis vector points in the opposite direction).
   * @return The opposite <code>Basis3</code> from this one.
   */
  public Basis3 opposite() {
    return new Basis3(u.opposite(), v.opposite(), w.opposite());
  }

  public static Basis3 fromU(Vector3 u) {
    return Basis3.fromU(u, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromU(Vector3 u, Orientation orientation) {
    return fromUV(u, u.perp(), orientation);
  }

  public static Basis3 fromV(Vector3 v) {
    return Basis3.fromV(v, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromV(Vector3 v, Orientation orientation) {
    return fromVW(v, v.perp(), orientation);
  }

  public static Basis3 fromW(Vector3 w) {
    return Basis3.fromW(w, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromW(Vector3 w, Orientation orientation) {
    return fromWU(w, w.perp(), orientation);
  }

  public static Basis3 fromUV(Vector3 u, Vector3 v) {
    return Basis3.fromUV(u, v, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromUV(Vector3 u, Vector3 v, Orientation orientation) {

    Vector3 _u = u.unit();
    Vector3 _v = v.minus(_u.times(_u.dot(v))).unit();

    Vector3 _w = (orientation == Orientation.RIGHT_HANDED ? _u.cross(_v)
        : _v.cross(_u));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromUW(Vector3 u, Vector3 w) {
    return Basis3.fromUW(u, w, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromUW(Vector3 u, Vector3 w, Orientation orientation) {

    Vector3 _u = u.unit();
    Vector3 _w = w.minus(_u.times(_u.dot(w))).unit();

    Vector3 _v = (orientation == Orientation.LEFT_HANDED ? _u.cross(_w)
        : _w.cross(_u));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromVU(Vector3 v, Vector3 u) {
    return Basis3.fromVU(v, u, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromVU(Vector3 v, Vector3 u, Orientation orientation) {

    Vector3 _v = v.unit();
    Vector3 _u = u.minus(_v.times(_v.dot(u))).unit();

    Vector3 _w = (orientation == Orientation.LEFT_HANDED ? _v.cross(_u)
        : _u.cross(_v));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromVW(Vector3 v, Vector3 w) {
    return Basis3.fromVW(v, w, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromVW(Vector3 v, Vector3 w, Orientation orientation) {

    Vector3 _v = v.unit();
    Vector3 _w = w.minus(_v.times(_v.dot(w))).unit();

    Vector3 _u = (orientation == Orientation.RIGHT_HANDED ? _v.cross(_w)
        : _w.cross(_v));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromWU(Vector3 w, Vector3 u) {
    return Basis3.fromWU(w, u, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromWU(Vector3 w, Vector3 u, Orientation orientation) {

    Vector3 _w = w.unit();
    Vector3 _u = u.minus(_w.times(_w.dot(u))).unit();

    Vector3 _v = (orientation == Orientation.RIGHT_HANDED ? _w.cross(_u)
        : _u.cross(_w));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromWV(Vector3 w, Vector3 v) {
    return Basis3.fromWV(w, v, Basis3.Orientation.RIGHT_HANDED);
  }

  public static Basis3 fromWV(Vector3 w, Vector3 v, Orientation orientation) {

    Vector3 _w = w.unit();
    Vector3 _v = v.minus(_w.times(_w.dot(v))).unit();

    Vector3 _u = (orientation == Orientation.LEFT_HANDED ? _w.cross(_v)
        : _v.cross(_w));

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromUVW(Vector3 u, Vector3 v, Vector3 w) {

    Vector3 _u = u.unit();
    Vector3 _v = v.minus(_u.times(_u.dot(v))).unit();

    Vector3 _w = _u.cross(_v);

    if (_w.dot(w) < 0.0)
      _w = _w.opposite();

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromUWV(Vector3 u, Vector3 w, Vector3 v) {

    Vector3 _u = u.unit();
    Vector3 _w = w.minus(_u.times(_u.dot(w))).unit();

    Vector3 _v = _u.cross(_w);

    if (_v.dot(v) < 0.0)
      _v = _v.opposite();

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromVUW(Vector3 v, Vector3 u, Vector3 w) {

    Vector3 _v = v.unit();
    Vector3 _u = u.minus(_v.times(_v.dot(u))).unit();

    Vector3 _w = _v.cross(_u);

    if (_w.dot(w) < 0.0)
      _w = _w.opposite();

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromVWU(Vector3 v, Vector3 w, Vector3 u) {

    Vector3 _v = v.unit();
    Vector3 _w = w.minus(_v.times(_v.dot(w))).unit();

    Vector3 _u = _v.cross(_w);

    if (_u.dot(u) < 0.0)
      _u = _u.opposite();

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromWUV(Vector3 w, Vector3 u, Vector3 v) {

    Vector3 _w = w.unit();
    Vector3 _u = u.minus(_w.times(_w.dot(u))).unit();

    Vector3 _v = _w.cross(_u);

    if (_v.dot(v) < 0.0)
      _v = _v.opposite();

    return new Basis3(_u, _v, _w);

  }

  public static Basis3 fromWVU(Vector3 w, Vector3 v, Vector3 u) {

    Vector3 _w = w.unit();
    Vector3 _v = v.minus(_w.times(_w.dot(v))).unit();

    Vector3 _u = _w.cross(_v);

    if (_u.dot(u) < 0.0)
      _u = _u.opposite();

    return new Basis3(_u, _v, _w);

  }

  /**
   * The standard <code>Basis3</code>.
   */
  public static final Basis3 STANDARD = new Basis3(Vector3.I, Vector3.J, Vector3.K);

  /** The first tangent <code>Vector3</code>. */
  private final Vector3 u;

  /** The second tangent <code>Vector3</code>. */
  private final Vector3 v;

  /** The normal <code>Vector3</code>. */
  private final Vector3 w;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 6204667205376195095L;

}
