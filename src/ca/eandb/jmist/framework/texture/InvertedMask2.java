/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Mask2</code> that inverts another mask.
 * @author Brad Kimmel
 */
public final class InvertedMask2 implements Mask2 {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -7699716505833575584L;
	
	/** The <code>Mask2</code> to be inverted. */
	private final Mask2 inner;
	
	/**
	 * Creates a new <code>InvertedMask2</code>.
	 * @param inner The <code>Mask2</code> to be inverted.
	 */
	public InvertedMask2(Mask2 inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Mask2#opacity(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public double opacity(Point2 p) {
		return 1.0 - inner.opacity(p);
	}

}
