/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public enum RtErrorSeverity {
	
	INFO		(0), /* Rendering stats and other info */
	WARNING		(1), /* Something seems wrong, maybe okay */
	ERROR		(2), /* Problem. Results may be wrong */
	SEVERE		(3); /* So bad you should probably abort */

	private final int level;
	
	RtErrorSeverity(int level) {
		this.level = level;
	}
	
	public int level() {
		return level;
	}
	
}
