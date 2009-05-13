/**
 *
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
	 * 		handed.
	 */
	public Orientation orientation() {
		return u.cross(v).dot(w) > 0.0 ? Orientation.RIGHT_HANDED
				: Orientation.LEFT_HANDED;
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
		Vector3 _v = v.unit();

		_v = _v.minus(_u.times(_u.dot(_v)));

		Vector3 _w = (orientation == Orientation.RIGHT_HANDED ? _u.cross(_v)
				: _v.cross(_u));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromUW(Vector3 u, Vector3 w) {
		return Basis3.fromUW(u, w, Basis3.Orientation.RIGHT_HANDED);
	}

	public static Basis3 fromUW(Vector3 u, Vector3 w, Orientation orientation) {

		Vector3 _u = u.unit();
		Vector3 _w = w.unit();

		_w = _w.minus(_u.times(_u.dot(_w)));

		Vector3 _v = (orientation == Orientation.LEFT_HANDED ? _u.cross(_w)
				: _w.cross(_u));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromVU(Vector3 v, Vector3 u) {
		return Basis3.fromVU(v, u, Basis3.Orientation.RIGHT_HANDED);
	}

	public static Basis3 fromVU(Vector3 v, Vector3 u, Orientation orientation) {

		Vector3 _v = v.unit();
		Vector3 _u = u.unit();

		_u = _u.minus(_v.times(_v.dot(_u)));

		Vector3 _w = (orientation == Orientation.LEFT_HANDED ? _v.cross(_u)
				: _u.cross(_v));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromVW(Vector3 v, Vector3 w) {
		return Basis3.fromVW(v, w, Basis3.Orientation.RIGHT_HANDED);
	}

	public static Basis3 fromVW(Vector3 v, Vector3 w, Orientation orientation) {

		Vector3 _v = v.unit();
		Vector3 _w = w.unit();

		_w = _w.minus(_v.times(_v.dot(_w)));

		Vector3 _u = (orientation == Orientation.RIGHT_HANDED ? _v.cross(_w)
				: _w.cross(_v));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromWU(Vector3 w, Vector3 u) {
		return Basis3.fromWU(w, u, Basis3.Orientation.RIGHT_HANDED);
	}

	public static Basis3 fromWU(Vector3 w, Vector3 u, Orientation orientation) {

		Vector3 _w = w.unit();
		Vector3 _u = u.unit();

		_u = _u.minus(_w.times(_w.dot(_u)));

		Vector3 _v = (orientation == Orientation.RIGHT_HANDED ? _w.cross(_u)
				: _u.cross(_w));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromWV(Vector3 w, Vector3 v) {
		return Basis3.fromWV(w, v, Basis3.Orientation.RIGHT_HANDED);
	}

	public static Basis3 fromWV(Vector3 w, Vector3 v, Orientation orientation) {

		Vector3 _w = w.unit();
		Vector3 _v = v.unit();

		_v = _v.minus(_w.times(_w.dot(_v)));

		Vector3 _u = (orientation == Orientation.LEFT_HANDED ? _w.cross(_v)
				: _v.cross(_w));

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromUVW(Vector3 u, Vector3 v, Vector3 w) {

		Vector3 _u = u.unit();
		Vector3 _v = v.unit();

		_v = _v.minus(_u.times(_u.dot(_v)));

		Vector3 _w = _u.cross(_v);

		if (_w.dot(w) < 0.0)
			_w = _w.opposite();

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromUWV(Vector3 u, Vector3 w, Vector3 v) {

		Vector3 _u = u.unit();
		Vector3 _w = w.unit();

		_w = _w.minus(_u.times(_u.dot(_w)));

		Vector3 _v = _u.cross(_w);

		if (_v.dot(v) < 0.0)
			_v = _v.opposite();

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromVUW(Vector3 v, Vector3 u, Vector3 w) {

		Vector3 _v = v.unit();
		Vector3 _u = u.unit();

		_u = _u.minus(_v.times(_v.dot(_u)));

		Vector3 _w = _v.cross(_u);

		if (_w.dot(w) < 0.0)
			_w = _w.opposite();

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromVWU(Vector3 v, Vector3 w, Vector3 u) {

		Vector3 _v = v.unit();
		Vector3 _w = w.unit();

		_w = _w.minus(_v.times(_v.dot(_w)));

		Vector3 _u = _v.cross(_w);

		if (_u.dot(u) < 0.0)
			_u = _u.opposite();

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromWUV(Vector3 w, Vector3 u, Vector3 v) {

		Vector3 _w = w.unit();
		Vector3 _u = u.unit();

		_u = _u.minus(_w.times(_w.dot(_u)));

		Vector3 _v = _w.cross(_u);

		if (_v.dot(v) < 0.0)
			_v = _v.opposite();

		return new Basis3(_u, _v, _w);

	}

	public static Basis3 fromWVU(Vector3 w, Vector3 v, Vector3 u) {

		Vector3 _w = w.unit();
		Vector3 _v = v.unit();

		_v = _v.minus(_w.times(_w.dot(_v)));

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
