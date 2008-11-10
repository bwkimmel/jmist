/**
 *
 */
package org.jmist.packages.texture;

import org.jmist.framework.Mask2;
import org.jmist.framework.Spectrum;
import org.jmist.framework.Texture2;
import org.jmist.packages.spectrum.ScaledSpectrum;
import org.jmist.toolkit.Point2;

/**
 * A <code>Texture2</code> decorator that applies a <code>Mask2</code> to
 * another <code>Texture2</code>.
 * @author bkimmel
 */
public final class MaskedTexture2 implements Texture2 {

	/**
	 * Creates a new <code>MaskedTexture2</code>.
	 * @param mask The <code>Mask2</code> to apply to the
	 * 		<code>Texture2</code>.
	 * @param texture The <code>Texture2</code> to mask.
	 */
	public MaskedTexture2(Mask2 mask, Texture2 texture) {
		this.mask = mask;
		this.texture = texture;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Texture2#evaluate(org.jmist.toolkit.Point2)
	 */
	public Spectrum evaluate(Point2 p) {
		return new ScaledSpectrum(this.mask.opacity(p), this.texture.evaluate(p));
	}

	/** The <code>Texture2</code> to mask. */
	private final Texture2 texture;

	/** The <code>Mask2</code> to apply to the <code>Texture2</code>. */
	private final Mask2 mask;

}
