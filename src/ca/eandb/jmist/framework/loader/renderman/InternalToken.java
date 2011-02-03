/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class InternalToken implements RtToken {

	private final String name;
	
	public InternalToken(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
