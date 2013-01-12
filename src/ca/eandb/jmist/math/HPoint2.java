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
package ca.eandb.jmist.math;

/**
 * A homogenized point in 2D space.  This will always be either a
 * <code>Point2</code> or a <code>Vector2</code>.
 * @author Brad Kimmel
 */
public abstract class HPoint2 extends Tuple2 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -7913735953929018849L;

	/** The zero <code>HPoint2</code>. */
	public static final HPoint2 ZERO = Vector2.ZERO;

	/** The standard basis vector in the x-direction. */
	public static final HPoint2 I = Vector2.I;

	/** The standard basis vector in the y-direction. */
	public static final HPoint2 J = Vector2.J;

	/** The standard basis vector in the w-direction. */
	public static final HPoint2 K = Point2.ORIGIN;

	/**
	 * Initializes the coordinates of this <code>HPoint2</code>.
	 * NOTE: This constructor has package scope to prevent classes other than
	 * <code>Point2</code> and <code>Vector2</code> from extending this class.
	 * @param x The value of the x-coordinate.
	 * @param y The value of the y-coordinate.
	 */
	/* package */ HPoint2(double x, double y) {
		super(x, y);
	}

	/**
	 * Creates a homogenized point.
	 * @param x The value of the x-coordinate.
	 * @param y The value of the y-coordinate.
	 * @param w The value of the w-coordinate (must be one or zero).
	 * @return An <code>HPoint2</code> corresponding to the given coordinates.
	 * 		If <code>w == 0</code>, a <code>Vector2</code> is returned.  If
	 * 		<code>w == 1</code>, a <code>Point2</code> is returned.
	 * @throws IllegalArgumentException if <code>w</code> is not within
	 * 		<code>MathUtil.EPSILON</code> of <code>0.0</code> or
	 * 		<code>1.0</code>.
	 * @see MathUtil#EPSILON
	 * @see Point2
	 * @see Vector2
	 */
	public static HPoint2 create(double x, double y, double w) {
		if (MathUtil.isZero(w)) {
			return new Vector2(x, y);
		} else if (MathUtil.equal(w, 1.0)) {
			return new Point2(x, y);
		} else {
			throw new IllegalArgumentException("w must be 0 or 1");
		}
	}

	/**
	 * Gets a value indicating if this <code>HPoint2</code> is a
	 * <code>Point2</code>.
	 * @return A value indicating if this <code>HPoint2</code> is a
	 * 		<code>Point2</code>.
	 */
	public final boolean isPoint() {
		return this instanceof Point2;
	}

	/**
	 * Gets a value indicating if this <code>HPoint2</code> is a
	 * <code>Vector2</code>.
	 * @return A value indicating if this <code>HPoint2</code> is a
	 * 		<code>Vector2</code>.
	 */
	public final boolean isVector() {
		return this instanceof Vector2;
	}

	/**
	 * Converts this <code>HPoint2</code> to a <code>Point2</code>.
	 * @return If this <code>HPoint2</code> is a <code>Point2</code>, this
	 * 		object is returned, otherwise <code>null</code> is returned.
	 */
	public final Point2 toPoint2() {
		return (Point2) this;
	}

	/**
	 * Converts this <code>HPoint2</code> to a <code>Vector2</code>.
	 * @return If this <code>HPoint2</code> is a <code>Vector2</code>, this
	 * 		object is returned, otherwise <code>null</code> is returned.
	 */
	public final Vector2 toVector2() {
		return (Vector2) this;
	}

	/**
	 * Gets a representation of this <code>HPoint2</code> as a 3 dimensional
	 * vector.
	 * @return A <code>Vector3</code> representation of this
	 * 		<code>HPoint2</code>.
	 */
	public final Vector3 toVector4() {
		return new Vector3(x, y, w());
	}

	/**
	 * Gets the distance from the origin along the x-axis.
	 * @return The distance from the origin along the x-axis.
	 */
	public final double x() {
		return x;
	}

	/**
	 * Gets the distance from the origin along the y-axis.
	 * @return The distance from the origin along the y-axis.
	 */
	public final double y() {
		return y;
	}

	/**
	 * Gets the distance from the origin along the w-axis.
	 * @return The distance from the origin along the w-axis.
	 */
	public abstract double w();

	/**
	 * Adds a <code>Vector2</code> to this <code>HPoint2</code>.
	 * @param v The <code>Vector2</code> to add.
	 * @return The sum of this <code>HPoint2</code> and <code>v</code>.
	 */
	public abstract HPoint2 plus(Vector2 v);

	/**
	 * Subtracts a <code>Vector2</code> from this <code>HPoint2</code>.
	 * @param v The <code>Vector2</code> to subtract.
	 * @return The difference of this <code>HPoint2</code> and <code>v</code>.
	 */
	public abstract HPoint2 minus(Vector2 v);

}
