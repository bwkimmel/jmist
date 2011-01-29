/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d.disabled;

import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.TriangleFanArray;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.loader.j3d.Util;
import ca.eandb.jmist.framework.loader.j3d.Visitor;

/**
 * @author Brad
 *
 */
class TriangleFanArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		TriangleFanArray geom = (TriangleFanArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);
		
		int ns = geom.getNumStrips();
		int[] vc = new int[ns];		
		geom.getStripVertexCounts(vc);
		
		int base = 0;
		
		for (int i = 0; i < ns; i++) {
			for (int j = 2; j < vc[i]; j++) {
				int[] vi = new int[]{ base, base + j - 1, base + j }; 
				mesh.addFace(vi, vi, vi);
			}
			base += vc[i];
		}
		
		return mesh;
	}

}
