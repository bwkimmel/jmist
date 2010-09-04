/**
 * 
 */
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;

/**
 * A <code>RayShader</code> that shades a ray based on a <code>Texture2</code>
 * representing a hemispherical environment map.  The x-coordinate of the
 * <code>Texture2</code> is interpreted as an azimuthal angle, and the
 * y-coordinate is interpreted as a polar angle from 0 to pi/2.
 * 
 * @author Brad Kimmel
 */
public final class HemisphericalEnvironmentRayShader implements RayShader {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 6076285080204159796L;

	/** The <code>Texture2</code> to interpret as a hemispherical map. */
	private final Texture2 texture;
	
	/** The <code>Basis3</code> representing the view orientation. */
	private final Basis3 basis;

	/** The <code>RayShader</code> to apply to the lower hemisphere. */
	private final RayShader background;
	
	/**
	 * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
	 * @param texture The <code>Texture2</code> to interpret as a hemispherical 
	 * 		map.
	 */
	public HemisphericalEnvironmentRayShader(Texture2 texture) {
		this(texture, Basis3.STANDARD);
	}
	
	/**
	 * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
	 * @param texture The <code>Texture2</code> to interpret as a hemispherical 
	 * 		map.
	 * @param basis The <code>Basis3</code> representing the view orientation.
	 */
	public HemisphericalEnvironmentRayShader(Texture2 texture, Basis3 basis) {
		this(texture, basis, RayShader.BLACK);
	}
	
	/**
	 * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
	 * @param texture The <code>Texture2</code> to interpret as a hemispherical 
	 * 		map.
	 * @param basis The <code>Basis3</code> representing the view orientation.
	 * @param background The <code>RayShader</code> to apply to the lower
	 * 		hemisphere.
	 */
	public HemisphericalEnvironmentRayShader(Texture2 texture, Basis3 basis, RayShader background) {
		this.texture = texture;
		this.basis = basis;
		this.background = background;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
		if (ray.direction().dot(basis.w()) >= 0.0) {
			SphericalCoordinates sc = SphericalCoordinates.fromCartesian(ray.direction(), basis);
			Point2 uv = new Point2(
					(sc.azimuthal() + Math.PI) / (2.0 * Math.PI),
					2.0 * sc.polar() / Math.PI);
			return texture.evaluate(uv, lambda);
		} else {
			return background.shadeRay(ray, lambda);
		}
	}

}
