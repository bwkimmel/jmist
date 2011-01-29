/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedGeometryArray;
import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;

import com.sun.j3d.utils.geometry.GeometryInfo;

/**
 * @author Brad
 *
 */
class GeometryArrayVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		GeometryArray geom = (GeometryArray) obj;
		GeometryInfo gi = new GeometryInfo(geom);
		IndexedGeometryArray array = gi.getIndexedGeometryArray(true);
		
		return VisitorFactory.createSceneElement(array);
	}

}
