/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.SceneGraphObject;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix4d;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.scene.TransformableSceneElement;
import ca.eandb.jmist.math.AffineMatrix3;

/**
 * @author Brad
 *
 */
class TransformGroupVisitor extends GroupVisitor {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.GroupVisitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		
		TransformGroup group = (TransformGroup) obj;
		SceneElement inner = super.createSceneElement(obj);
		TransformableSceneElement elem = new TransformableSceneElement(inner);
		Transform3D transform = new Transform3D();
		Matrix4d a4d = new Matrix4d();
		
		group.getTransform(transform);
		transform.get(a4d);
		
		AffineMatrix3 a = new AffineMatrix3(
				a4d.m00, a4d.m01, a4d.m02, a4d.m03,
				a4d.m10, a4d.m11, a4d.m12, a4d.m13,
				a4d.m20, a4d.m21, a4d.m22, a4d.m23);
		
		elem.transform(a);
		return elem;
		
	}

}
