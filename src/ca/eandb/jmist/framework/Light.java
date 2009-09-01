/**
 *
 */
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.gi2.LightNode;
import ca.eandb.jmist.framework.gi2.PathInfo;


/**
 * @author Brad Kimmel
 *
 */
public interface Light extends Serializable {

	void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target);

	LightNode sample(PathInfo pathInfo, Random rnd);

	ScatteredRay emit(WavelengthPacket lambda, Random rnd);

	double getSamplePDF(SurfacePoint x, PathInfo pathInfo);

	public static final Light NULL = new Light() {
		private static final long serialVersionUID = 5058166013868688853L;
		public ScatteredRay emit(WavelengthPacket lambda, Random rnd) {
			return null;
		}
		public void illuminate(SurfacePoint x, WavelengthPacket lambda,
				Random rng, Illuminable target) {
		}
		public LightNode sample(PathInfo pathInfo, Random rnd) {
			return null;
		}
		public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
			return 0;
		}
	};

}
