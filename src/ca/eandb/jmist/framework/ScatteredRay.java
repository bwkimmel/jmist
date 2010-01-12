/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public final class ScatteredRay {

	public static enum Type {
		DIFFUSE,
		GLOSSY,
		SPECULAR
	}

	private final Ray3 scatteredRay;
	private final Color color;
	private final Type type;
	private final double pdf;
	private final boolean transmitted;;

	public ScatteredRay(Ray3 scatteredRay, Color color, Type type, double pdf, boolean transmitted) {

		assert(scatteredRay != null);

		this.scatteredRay = scatteredRay;
		this.color = color;
		this.transmitted = transmitted;
		this.type = type;
		this.pdf = pdf;

	}

	public final Ray3 getRay() {
		return scatteredRay;
	}

	public final Color getColor() {
		return color;
	}

	public final Type getType() {
		return type;
	}

	public final double getPDF() {
		return pdf;
	}

	public final boolean isTransmitted() {
		return transmitted;
	}

	public ScatteredRay transform(AffineMatrix3 T) {
		return new ScatteredRay(scatteredRay.transform(T), color, type,
				pdf, transmitted);
	}

	public static ScatteredRay diffuse(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.DIFFUSE, pdf, false);
	}

	public static ScatteredRay glossy(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.GLOSSY, pdf, false);
	}

	public static ScatteredRay specular(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.SPECULAR, pdf, false);
	}

	public static ScatteredRay transmitDiffuse(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.DIFFUSE, pdf, true);
	}

	public static ScatteredRay transmitGlossy(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.GLOSSY, pdf, true);
	}

	public static ScatteredRay transmitSpecular(Ray3 ray, Color color, double pdf) {
		return new ScatteredRay(ray, color, Type.SPECULAR, pdf, true);
	}

	public static ScatteredRay select(ScatteredRay sr, double probability) {
		return new ScatteredRay(sr.getRay(), sr.getColor().divide(probability),
				sr.getType(), sr.getPDF() * probability, sr.isTransmitted());
	}

}
