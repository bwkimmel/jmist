/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * @author Brad Kimmel
 *
 */
public interface Light {

	void illuminate(SurfacePoint x, VisibilityFunction3 vf, Illuminable target);

}
