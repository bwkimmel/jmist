/**
 *
 */
package ca.eandb.jmist.framework.color;



/**
 * @author Brad
 *
 */
public interface Color {

	Color times(Color other);

	Color times(double c);

	Color divide(Color other);

	Color divide(double c);

	Color plus(Color other);

	Color minus(Color other);

	Color sqrt();

	Color exp();

	Color invert();

	Color negative();

	Color pow(Color other);

	Color pow(double e);

	Color clamp(double max);

	Color clamp(double min, double max);

	double getValue(int channel);

	ColorModel getColorModel();

	Color disperse(int channel);

	double[] toArray();

}
