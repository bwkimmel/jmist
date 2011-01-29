/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d.disabled;

import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.TriangleArray;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.loader.j3d.Util;
import ca.eandb.jmist.framework.loader.j3d.Visitor;

/**
 * @author Brad
 *
 */
class TriangleArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		TriangleArray geom = (TriangleArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);
	
		for (int i = 0, n = mesh.getNumVertices(); i < n; i += 3) {
			int[] vi = new int[]{ n, n + 1, n + 2 };
			mesh.addFace(vi, vi, vi);
		}
		
		return mesh;
	}

}
