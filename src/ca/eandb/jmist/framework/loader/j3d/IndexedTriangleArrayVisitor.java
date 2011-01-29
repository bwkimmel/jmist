/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;

/**
 * @author Brad
 *
 */
class IndexedTriangleArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		IndexedTriangleArray geom = (IndexedTriangleArray) obj;
		PolyhedronGeometry mesh = Util.prepareMesh(geom);

		int ni = geom.getIndexCount();
		int[] vci = new int[3];
		int[] vni = new int[3];
		int[] vti = new int[3];
	
		for (int i = 0; i < ni; i += 3) {
			geom.getCoordinateIndices(i, vci);
			geom.getNormalIndices(i, vni);
			geom.getTextureCoordinateIndices(0, i, vti);
			
			mesh.addFace(vci.clone(), vti.clone(), vni.clone());
		}
		
		return mesh;
	}

}
