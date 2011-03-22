/**
 * 
 */
package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Shader</code> that assigns a colour based on the surface normal.
 * 
 * @author Brad Kimmel
 */
public final class NormalShader implements Shader {

	/** Serialization version ID. */
	private static final long serialVersionUID = -4724073342278275837L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public Color shade(ShadingContext sc) {
		Vector3 n = sc.getNormal();
		
		double r = Math.abs(n.x());
		double g = Math.abs(n.y());
		double b = Math.abs(n.z());
		
		double c = Math.max(r, Math.max(g, b));
		
		return sc.getColorModel().fromRGB(r / c, g / c, b / c).sample(sc.getWavelengthPacket());
	}

}
