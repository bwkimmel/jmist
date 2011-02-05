/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Matrix4;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad
 *
 */
public final class RtMatrix {
	
	static final RtMatrix IDENTITY = new RtMatrix(Matrix4.IDENTITY);
	
	private Matrix4 matrix;
	
	RtMatrix(Matrix4 matrix) {
		this.matrix = matrix;
	}
	
	public RtMatrix(
			double _00, double _01, double _02, double _03,
			double _10, double _11, double _12, double _13,
			double _20, double _21, double _22, double _23,
			double _30, double _31, double _32, double _33) {
		
		/* JMist treats points and vectors as column vectors.  This is the
		 * opposite of how RenderMan expects them to be represented.  To
		 * compensate, we transparently transpose the given matrix elements.
		 */
		this(new Matrix4(
				_00, _10, _20, _30,
				_01, _11, _21, _31,
				_02, _12, _22, _32,
				_03, _13, _23, _33));

	}
	
	RtMatrix concatenate(RtMatrix other) {
		return new RtMatrix(matrix.times(other.matrix));
	}
	
	static RtMatrix perspective(double fov) {
		throw new UnimplementedException();
	}
	
	static RtMatrix translate(double dx, double dy, double dz) {
		return new RtMatrix(
				1 , 0 , 0 , 0,
				0 , 1 , 0 , 0,
				0 , 0 , 1 , 0,
				dx, dy, dz, 1);
	}

	public static RtMatrix rotate(double angle, double dx, double dy, double dz) {
		LinearMatrix3 T = LinearMatrix3.rotateMatrix(new Vector3(dx, dy, dz), Math.toRadians(angle));
		return new RtMatrix(
				T.at(0, 0), T.at(1, 0), T.at(2, 0), 0.0,
				T.at(0, 1), T.at(1, 1), T.at(2, 1), 0.0,
				T.at(0, 2), T.at(1, 2), T.at(2, 2), 0.0,
				0.0       , 0.0       , 0.0       , 1.0);
	}
	
	static RtMatrix scale(double sx, double sy, double sz) {
		return new RtMatrix(
				sx, 0 , 0 , 0,
				0 , sy, 0 , 0,
				0 , 0 , sz, 0,
				0 , 0 , 0 , 1);
	}
	
	static RtMatrix skew(double angle, double dx1, double dy1,
			double dz1, double dx2, double dy2, double dz2) {
		throw new UnimplementedException();
	}	
	
	Matrix4 toMatrix() {
		return matrix;
	}

}
