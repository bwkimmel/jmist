/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Sphere;


/**
 * @author Brad Kimmel
 *
 */
public interface Light {

	void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target);

	Emitter sample(Random rng);

	Photon emit(Sphere target, WavelengthPacket lambda, Random rng);

}
