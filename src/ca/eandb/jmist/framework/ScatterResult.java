/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public final class ScatterResult {

	public static enum Type {
		DIFFUSE,
		GLOSSY,
		SPECULAR
	};

	private final Ray3 scatteredRay;
	private Color color;
	private final Type type;
	private final boolean transmitted;

	private ScatterResult(Ray3 scatteredRay, Color color, Type type, boolean transmitted) {

		assert(scatteredRay != null);

		this.scatteredRay = scatteredRay;
		this.color = color;
		this.transmitted = transmitted;
		this.type = type;

	}

	/**
	 * @param color the color to set
	 */
	public final void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the scatteredRay
	 */
	public final Ray3 getScatteredRay() {
		return scatteredRay;
	}

	/**
	 * @return the color
	 */
	public final Color getColor() {
		return color;
	}

	/**
	 * @return the type
	 */
	public final Type getType() {
		return type;
	}

	/**
	 * @return the transmitted
	 */
	public final boolean isTransmitted() {
		return transmitted;
	}

	public static ScatterResult diffuse(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.DIFFUSE, false);
	}

	public static ScatterResult glossy(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.GLOSSY, false);
	}

	public static ScatterResult specular(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.SPECULAR, false);
	}

	public static ScatterResult transmitDiffuse(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.DIFFUSE, true);
	}

	public static ScatterResult transmitGlossy(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.GLOSSY, true);
	}

	public static ScatterResult transmitSpecular(Ray3 ray, Color color) {
		return new ScatterResult(ray, color, Type.SPECULAR, true);
	}

}
