/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import java.util.Arrays;
import java.util.List;

import javax.media.j3d.GeometryArray;

import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
final class Util {

	public static List<Point3> getVertices(GeometryArray geom) {
		int nv = geom.getVertexCount();
		double[] coord = new double[3];
		Point3[] v = new Point3[nv];
		for (int i = 0; i < nv; i++) {
			geom.getCoordinate(i, coord);
			v[i] = new Point3(coord[0], coord[1], coord[2]);
		}
		return Arrays.asList(v);
	}

	public static List<Vector3> getNormals(GeometryArray geom) {
		int nv = geom.getVertexCount();
		float[] coord = new float[3];
		Vector3[] v = new Vector3[nv];
		for (int i = 0; i < nv; i++) {
			geom.getNormal(i, coord);
			v[i] = new Vector3(coord[0], coord[1], coord[2]);
		}
		return Arrays.asList(v);
	}

	public static List<Point2> getTextureCoordinates(GeometryArray geom) {
		int nt = geom.getTexCoordSetCount();
		float[] coord = new float[2];
		Point2[] vt = new Point2[nt];
		for (int i = 0; i < nt; i++) {
			geom.getTextureCoordinate(0, i, coord);
			vt[i] = new Point2(coord[0], coord[1]);
		}
		return Arrays.asList(vt);
	}
	
	public static PolyhedronGeometry prepareMesh(GeometryArray geom) {
		List<Point3> vertices = Util.getVertices(geom);
		List<Vector3> normals = Util.getNormals(geom);
		List<Point2> texCoords = Util.getTextureCoordinates(geom);
		
		return new PolyhedronGeometry(vertices, texCoords, normals);
	}
	
}
