/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public interface RayCaster extends VisibilityFunction3 {

	IntersectionGeometry castRay(Ray3 ray, PathContext pc, RenderContext rc);

	Color shadeRay(Ray3 ray, PathContext pc, RenderContext rc);

	boolean castShadowRay(Ray3 ray, double distance);

	boolean castShadowRay(Ray3 ray);

	Scene getScene();

}
