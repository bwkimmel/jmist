/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Spectrum;

/**
 * @author Brad
 *
 */
public interface Color {

	Color times(Spectrum s);

	Color times(double c);

	Color add(Spectrum s);

	MonochromaticColor disperse();

}
