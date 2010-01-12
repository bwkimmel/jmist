/**
 *
 */
package ca.eandb.jmist.framework.color;

import java.io.Serializable;



/**
 * @author Brad
 *
 */
public interface Color extends Serializable {

	WavelengthPacket getWavelengthPacket();

	ColorModel getColorModel();

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
	
	Color abs();

	Color pow(Color other);

	Color pow(double e);

	Color clamp(double max);

	Color clamp(double min, double max);

	double getValue(int channel);

	Color disperse(int channel);

	double luminance();

	double[] toArray();

	CIEXYZ toXYZ();

	RGB toRGB();

}
