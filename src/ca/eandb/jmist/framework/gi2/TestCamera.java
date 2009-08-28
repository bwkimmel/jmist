/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad
 *
 */
public final class TestCamera implements Camera {

	private final Lens lens;

	public TestCamera(Lens lens) {
		this.lens = lens;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.Camera#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.gi2.PathInfo)
	 */
	public EyeNode sample(Point2 pointOnImagePlane, PathInfo pathInfo) {
		return new Node(pointOnImagePlane, pathInfo);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.Camera#project(ca.eandb.jmist.framework.gi2.PathNode)
	 */
	public EyeNode project(PathNode target) {
		// TODO Auto-generated method stub
		return null;
	}

	private final class Node extends AbstractEyeNode {

		private final Point2 pointOnImagePlane;

		private final Ray3 ray;

		public Node(Point2 pointOnImagePlane, PathInfo pathInfo) {
			super(pathInfo);
			this.pointOnImagePlane = pointOnImagePlane;
			this.ray = lens.rayAt(pointOnImagePlane);
		}

		public double getAperturePDF() {
			return 1.0 / lens.areaOfAperture();
		}

		public Camera getCamera() {
			return TestCamera.this;
		}

		public Color getImportance(PathNode to) {
			PathInfo path = getPathInfo();
			ColorModel cm = path.getColorModel();
			WavelengthPacket lambda = path.getWavelengthPacket();
			return cm.getWhite(lambda);
		}

		public Color getImportanceExitance() {
			PathInfo path = getPathInfo();
			ColorModel cm = path.getColorModel();
			WavelengthPacket lambda = path.getWavelengthPacket();
			return cm.getWhite(lambda);
		}

		public boolean isApertureSingular() {
			return lens.areaOfAperture() == 0.0;
		}

		public void write(Color c, Raster raster) {
			int w = raster.getWidth();
			int h = raster.getHeight();
			int x = MathUtil.threshold(
					(int) Math.floor(pointOnImagePlane.x() * w),
					0, w - 1);
			int y = MathUtil.threshold(
					(int) Math.floor(pointOnImagePlane.y() * h),
					0, h - 1);
			raster.addPixel(x, y, c);
		}

		public PathNode expand() {
			PathInfo path = getPathInfo();
			ColorModel cm = path.getColorModel();
			WavelengthPacket lambda = path.getWavelengthPacket();
			Ray3 ray = lens.rayAt(pointOnImagePlane);
			ScatteredRay sr = ScatteredRay.diffuse(ray, cm.getWhite(lambda));
			return trace(sr);
		}

		public double getCosine(PathNode node) {
			return 1.0;
		}

		public double getForwardPDF(PathNode to) {
			return 1.0;
		}

		public HPoint3 getPosition() {
			return ray.origin();
		}

	}

}
