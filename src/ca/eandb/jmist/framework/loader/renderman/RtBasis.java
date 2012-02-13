/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import ca.eandb.jmist.math.Matrix4;

/**
 * @author Brad
 *
 */
public final class RtBasis {
	
	private final Matrix4 matrix;

	public RtBasis(
			double _00, double _01, double _02, double _03,
			double _10, double _11, double _12, double _13,
			double _20, double _21, double _22, double _23,
			double _30, double _31, double _32, double _33) {
		this.matrix = new Matrix4(
				_00, _01, _02, _03,
				_10, _11, _12, _13,
				_20, _21, _22, _23,
				_30, _31, _32, _33);
	}
	
	public Matrix4 toMatrix() {
		return matrix;
	}

}
