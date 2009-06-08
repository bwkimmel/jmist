/**
 *
 */
package ca.eandb.jmist.framework.color;

import java.awt.image.WritableRaster;


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

	MonochromaticColor disperse();

	void writeToRaster(WritableRaster raster, int x, int y);

}
