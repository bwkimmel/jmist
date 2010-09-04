/**
 * 
 */
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A decorator <code>Texture2</code> that tiles the decorated
 * <code>Texture2</code>. 
 * @author Brad Kimmel
 */
public final class TiledTexture2 implements Texture2 {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3873743899579405693L;
	
	/** The <code>Texture2</code> to tile. */
	private final Texture2 inner;
	
	/**
	 * Creates a new <code>TiledTexture2</code>.
	 * @param inner The <code>Texture2</code> to tile.
	 */
	public TiledTexture2(Texture2 inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Texture2#evaluate(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Point2 p, WavelengthPacket lambda) {
		p = new Point2(p.x() - Math.floor(p.x()), p.y() - Math.floor(p.y()));
		return inner.evaluate(p, lambda);
	}

}
