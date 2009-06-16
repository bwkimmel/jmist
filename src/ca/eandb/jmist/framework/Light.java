/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * @author Brad Kimmel
 *
 */
public interface Light {

	void illuminate(Intersection x, Illuminable target);

}
