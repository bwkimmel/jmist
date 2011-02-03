package ca.eandb.jmist.framework.geometry.mesh;

import java.util.List;

import ca.eandb.jmist.framework.Bounded3;


public interface MeshFace extends Bounded3 {
	
	List<? extends MeshVertex> vertices();
	
}