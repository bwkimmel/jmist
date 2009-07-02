/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * Receives scattering information from a <code>Material</code>.
 * @author Brad Kimmel
 */
public interface ScatteredRayRecorder {

	/**
	 * Record a scattering event from a <code>Material</code>.
	 * @param sr The <code>ScatteredRay</code> describing the scattering
	 * 		event.
	 */
	void add(ScatteredRay sr);

}
