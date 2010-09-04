/**
 * 
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		return inner.evaluate(transform.applyInverse(p), lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Rotatable2#rotate(double)
	 */
	public void rotate(double angle) {
		transform.rotate(angle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scalable#scale(double)
	 */
	public void scale(double c) {
		transform.scale(c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable2#stretch(double, double)
	 */
	public void stretch(double cx, double cy) {
		transform.stretch(cx, cy);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Stretchable2#stretch(ca.eandb.jmist.math.Vector2, double)
	 */
	public void stretch(Vector2 axis, double c) {
		transform.stretch(axis, c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable2#stretchX(double)
	 */
	public void stretchX(double cx) {
		transform.stretchX(cx);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AxisStretchable2#stretchY(double)
	 */
	public void stretchY(double cy) {
		transform.stretchY(cy);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.AffineTransformable2#transform(ca.eandb.jmist.math.AffineMatrix2)
	 */
	public void transform(AffineMatrix2 T) {
		transform.transform(T);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.LinearTransformable2#transform(ca.eandb.jmist.math.LinearMatrix2)
	 */
	public void transform(LinearMatrix2 T) {
		transform.transform(T);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Translatable2#translate(ca.eandb.jmist.math.Vector2)
	 */
	public void translate(Vector2 v) {
		transform.translate(v);
	}
	
}
