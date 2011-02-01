/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d.disabled;

import javax.media.j3d.SceneGraphObject;
import javax.vecmath.Color3f;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.loader.j3d.Visitor;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.material.ModifiedPhongMaterial;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
class MaterialVisitor implements Visitor {
	
	private static final Color3f BLACK = new Color3f(0, 0, 0);

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.j3d.Visitor#createSceneElement(javax.media.j3d.SceneGraphObject)
	 */
	@Override
	public SceneElement createSceneElement(SceneGraphObject obj) {
		if (obj.getUserData() == null) {
			javax.media.j3d.Material mat = (javax.media.j3d.Material) obj;
			ColorModel cm = null;
			
			Color3f color = new Color3f();
			mat.getDiffuseColor(color);
			Spectrum kd = cm.fromRGB(color.x, color.y, color.z);
			
			mat.getSpecularColor(color);
			Spectrum ks = cm.fromRGB(color.x, color.y, color.z);
			
			float n = mat.getShininess();
	
			if (color.epsilonEquals(BLACK, (float) MathUtil.EPSILON)) {
				mat.setUserData(new LambertianMaterial(kd));
			} else {
				mat.setUserData(new ModifiedPhongMaterial(kd, ks, n));
			}
		}
	}

}
