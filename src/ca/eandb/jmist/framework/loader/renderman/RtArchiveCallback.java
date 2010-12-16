/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public interface RtArchiveCallback {

	void apply(RtToken name, String... parameters);
	
}
