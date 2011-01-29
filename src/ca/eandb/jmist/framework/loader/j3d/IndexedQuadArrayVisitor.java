/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;

/**
 * @author Brad
 *
 */
class IndexedQuadArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		IndexedQuadArray geom = (IndexedQuadArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);

		int ni = geom.getIndexCount();
		int[] vci = new int[4];
		int[] vni = new int[4];
		int[] vti = new int[4];
	
		for (int i = 0; i < ni; i += 4) {
			geom.getCoordinateIndices(i, vci);
			geom.getNormalIndices(i, vni);
			geom.getTextureCoordinateIndices(0, i, vti);
			
			mesh.addFace(
					new int[]{ vci[0], vci[1], vci[2] },
					new int[]{ vti[0], vti[1], vti[2] },
					new int[]{ vni[0], vni[1], vni[2] });
					
			mesh.addFace(
					new int[]{ vci[2], vci[3], vci[0] },
					new int[]{ vti[2], vti[3], vti[0] },
					new int[]{ vni[2], vni[3], vni[0] });
		}
		
		return mesh;
	}

}
