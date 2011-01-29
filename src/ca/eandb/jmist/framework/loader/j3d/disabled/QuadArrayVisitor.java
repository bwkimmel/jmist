/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d.disabled;

import javax.media.j3d.QuadArray;
import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.loader.j3d.Util;
import ca.eandb.jmist.framework.loader.j3d.Visitor;

/**
 * @author Brad
 *
 */
class QuadArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		QuadArray geom = (QuadArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);
	
		for (int i = 0, n = mesh.getNumVertices(); i < n; i += 4) {
			int[] vi1 = new int[]{ n + 0, n + 1, n + 2 };
			int[] vi2 = new int[]{ n + 2, n + 3, n + 0 };
			mesh.addFace(vi1, vi1, vi1);
			mesh.addFace(vi2, vi2, vi2);
		}
		
		return mesh;
	}

}
