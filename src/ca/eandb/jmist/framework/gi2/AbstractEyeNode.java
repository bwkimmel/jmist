/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public abstract class AbstractEyeNode extends AbstractTerminalNode implements
		EyeNode {

	private final Point2 pointOnImagePlane;

	/**
	 * @param pathInfo
	 */
	public AbstractEyeNode(PathInfo pathInfo, Point2 pointOnImagePlane) {
		super(pathInfo);
		this.pointOnImagePlane = pointOnImagePlane;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.EyeNode#write(ca.eandb.jmist.framework.color.Color, ca.eandb.jmist.framework.Raster)
	 */
	public void write(Color c, Raster raster) {
		int w = raster.getWidth();
		int h = raster.getHeight();
		int x = (int) MathUtil.threshold(Math.floor(pointOnImagePlane.x() * w),
				0, w - 1);
		int y = (int) MathUtil.threshold(Math.floor(pointOnImagePlane.y() * h),
				0, h - 1);
		raster.addPixel(x, y, c);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathNode#isOnLightPath()
	 */
	public final boolean isOnLightPath() {
		return false;
	}

}
