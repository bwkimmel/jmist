/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

/**
 * @author brad
 *
 */
public enum EnvironmentMapType {

	LATLONG(0),
	CUBE(1);
	
	private final int key;
	
	private EnvironmentMapType(int key) {
		this.key = key;
	}
	
}
