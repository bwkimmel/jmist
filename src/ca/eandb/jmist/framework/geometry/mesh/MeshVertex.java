package ca.eandb.jmist.framework.geometry.mesh;

import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

public interface MeshVertex {

	Point3 position();
	
	Vector3 normal();
	
	Point2 textureCoordinates();
	
}