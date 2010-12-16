/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Light;
import javax.media.j3d.TransformGroup;

import ca.eandb.jmist.framework.Scene;

/**
 * @author Brad
 *
 */
public final class Java3DReader {

	public Scene read(com.sun.j3d.loaders.Scene j3dScene) {
		TransformGroup[] viewGroup = j3dScene.getViewGroups();
		float[] fov = j3dScene.getHorizontalFOVs();
		Light[] light = j3dScene.getLightNodes();
		BranchGroup sceneGroup = j3dScene.getSceneGroup();
		
		
	}
	
}
