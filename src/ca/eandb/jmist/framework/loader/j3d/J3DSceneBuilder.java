/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.BranchGroup;

import ca.eandb.jmist.framework.SceneElement;

import com.sun.j3d.loaders.objectfile.ObjectFile;

/**
 * @author Brad
 *
 */
public final class J3DSceneBuilder {

	public SceneElement createScene(com.sun.j3d.loaders.Scene j3d) {
		
		BranchGroup group = j3d.getSceneGroup();
		group.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		
		SceneElement root = VisitorFactory.createSceneElement(j3d.getSceneGroup());
		return root;
	}
	
	public static void main(String[] args) {
		String fn = "C:\\Users\\Brad\\Downloads\\DS02_obj\\DS02_obj.obj";
		ObjectFile loader = new ObjectFile();
		com.sun.j3d.loaders.Scene j3d;
		try {
			j3d = loader.load(fn);
			J3DSceneBuilder builder = new J3DSceneBuilder();
			SceneElement scene = builder.createScene(j3d);
			System.out.println(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
