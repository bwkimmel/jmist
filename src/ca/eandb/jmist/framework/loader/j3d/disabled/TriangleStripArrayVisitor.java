/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d.disabled;

import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.TriangleStripArray;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.loader.j3d.Util;
import ca.eandb.jmist.framework.loader.j3d.Visitor;

/**
 * @author Brad
 *
 */
class TriangleStripArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		TriangleStripArray geom = (TriangleStripArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);
		
		int ns = geom.getNumStrips();
		int[] vc = new int[ns];		
		geom.getStripVertexCounts(vc);
		
		int base = 0;
		
		for (int i = 0; i < ns; i++) {
			boolean parity = false;
			for (int j = 2; j < vc[i]; j++) {
				int[] vi = new int[]{ base + j - 2, base + j - (parity ? 1 : 0), base + j - (parity ? 0 : 1) }; 
				mesh.addFace(vi, vi, vi);
				parity = !parity;
			}
			base += vc[i];
		}
		
		return mesh;
	}

}
