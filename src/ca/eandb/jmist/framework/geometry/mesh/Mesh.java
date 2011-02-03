/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.io.Serializable;
import java.util.List;

import ca.eandb.jmist.framework.Bounded3;

/**
 * @author Brad
 *
 */
public interface Mesh extends Bounded3, Serializable {
	
	List<? extends MeshFace> faces();
	
	Mesh triangulate();
	
	Mesh triangulate(int maximumVerticesPerFace);
	
}
