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
public abstract class ScatteredRay {

	public static enum Type {
		DIFFUSE,
		GLOSSY,
		SPECULAR
	};

	public abstract Ray3 getRay();

	public abstract Color getColor();

	public abstract Type getType();

	public abstract boolean isTransmitted();

	public static ScatteredRay diffuse(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.DIFFUSE, false);
	}

	public static ScatteredRay glossy(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.GLOSSY, false);
	}

	public static ScatteredRay specular(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.SPECULAR, false);
	}

	public static ScatteredRay transmitDiffuse(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.DIFFUSE, true);
	}

	public static ScatteredRay transmitGlossy(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.GLOSSY, true);
	}

	public static ScatteredRay transmitSpecular(Ray3 ray, Color color) {
		return new StandardScatteredRay(ray, color, Type.SPECULAR, true);
	}

}
