package ca.eandb.jmist.framework.geometry;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.primitive.SphereGeometry;
import ca.eandb.jmist.framework.geometry.primitive.TaperedCylinderGeometry;
import ca.eandb.jmist.framework.scene.MergeSceneElement;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

public final class PolylineSweptSphereBuilder {
	
	private Sphere previous;
	
	private MergeSceneElement geometry;
	
	public PolylineSweptSphereBuilder() {
		reset();
	}
	
	public synchronized void reset() {
		previous = null;
		geometry = new MergeSceneElement();
	}
	
	public synchronized void addVertex(Point3 p, double radius) {
		addVertex(p, radius, false);
	}
	
	public synchronized void addVertex(Sphere sphere) {
		addVertex(sphere, false);
	}
	
	public synchronized void addVertex(Point3 p, double radius, boolean skip) {
		addVertex(new Sphere(p, radius), skip);
	}
	
	public synchronized void addVertex(Sphere sphere, boolean skip) {
		if (!skip && previous != null) {
			double length2 = previous.center().squaredDistanceTo(sphere.center());
			double length = Math.sqrt(length2);
			double jointRadius1 = previous.radius();
			double jointRadius2 = sphere.radius();
			double djr = jointRadius1 - jointRadius2;
			double s2 = length2 - djr * djr;
			double cosTheta = djr / length;
			double sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta);
			double h1 = jointRadius1 * cosTheta;
			double h2 = jointRadius2 * cosTheta;
			double r1 = jointRadius1 * sinTheta;
			double r2 = jointRadius2 * sinTheta;
			
			TransformableGeometry segment = new TransformableGeometry(
					new TaperedCylinderGeometry(h1, r1, length + h2, r2, false));
			
			Vector3 v = previous.center().vectorTo(sphere.center());
			Basis3 basis = Basis3.fromV(v);
			AffineMatrix3 T = AffineMatrix3.fromColumns(basis.u(), basis.v(), basis.w(), previous.center());
			
			segment.transform(T);
			geometry.addChild(segment);
		}
		geometry.addChild(new SphereGeometry(sphere));
		previous = sphere;
	}
	
	public synchronized SceneElement create() {
		return geometry;
	}

}
