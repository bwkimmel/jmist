/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * Receives scattering information from a <code>Material</code>.
 * @author Brad Kimmel
 */
public interface ScatterRecorder {

	/**
	 * Record a scattering event from a <code>Material</code>.
	 * @param sr The <code>ScatterResult</code> describing the scattering
	 * 		event.
	 */
	void record(ScatterResult sr);

}
