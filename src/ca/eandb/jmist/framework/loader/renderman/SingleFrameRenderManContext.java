/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.Map;

/**
 * @author Brad
 *
 */
public final class SingleFrameRenderManContext implements RenderManContext {
	
	private final RenderManContext inner;
	
	private final int frame;
	
	private int currentFrame = -1;
	
	public SingleFrameRenderManContext(int frame, RenderManContext inner) {
		this.inner = inner;
		this.frame = frame;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#declare(java.lang.String, java.lang.String)
	 */
	@Override
	public RtToken declare(String name, String declaration) {
		if (currentFrame < 0 || currentFrame == frame) {
			return inner.declare(name, declaration);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameBegin(int)
	 */
	@Override
	public void frameBegin(int frame) {
		if (currentFrame >= 0) {
			throw new IllegalStateException("frame blocks may not be nested");
		}
		currentFrame = frame;
		if (frame == this.frame) {
			inner.frameBegin(frame);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameEnd()
	 */
	@Override
	public void frameEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.frameEnd();
		}
		currentFrame = -1;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldBegin()
	 */
	@Override
	public void worldBegin() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.worldBegin();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldEnd()
	 */
	@Override
	public void worldEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.worldEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#format(int, int, double)
	 */
	@Override
	public void format(int xresolution, int yresolution, double pixelaspectratio) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.format(xresolution, yresolution, pixelaspectratio);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameAspectRatio(double)
	 */
	@Override
	public void frameAspectRatio(double frameaspectratio) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.frameAspectRatio(frameaspectratio);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#screenWindow(double, double, double, double)
	 */
	@Override
	public void screenWindow(double left, double right, double bottom,
			double top) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.screenWindow(left, right, bottom, top);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cropWindow(double, double, double, double)
	 */
	@Override
	public void cropWindow(double xmin, double xmax, double ymin, double ymax) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.cropWindow(xmin, xmax, ymin, ymax);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#projection(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void projection(RtToken name, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.projection(name, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#clipping(double, double)
	 */
	@Override
	public void clipping(double near, double far) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.clipping(near, far);
		}
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#clippingPlane(double, double, double, double, double, double)
	 */
	@Override
	public void clippingPlane(double x, double y, double z, double nx, double ny, double nz) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.clippingPlane(x, y, z, nx, ny, nz);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#depthOfField(double, double, double)
	 */
	@Override
	public void depthOfField(double fstop, double focallength,
			double focaldistance) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.depthOfField(fstop, focallength, focaldistance);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shutter(double, double)
	 */
	@Override
	public void shutter(double min, double max) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.shutter(min, max);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelVariance(double)
	 */
	@Override
	public void pixelVariance(double variation) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.pixelVariance(variation);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelSamples(double, double)
	 */
	@Override
	public void pixelSamples(double xsamples, double ysamples) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.pixelSamples(xsamples, ysamples);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelFilter(ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double)
	 */
	@Override
	public void pixelFilter(RtFilterFunc filterfunc, double xwidth,
			double ywidth) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.pixelFilter(filterfunc, xwidth, ywidth);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exposure(double, double)
	 */
	@Override
	public void exposure(double gain, double gamma) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.exposure(gain, gamma);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#imager(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void imager(RtToken name, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.imager(name, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#quantize(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int, int, double)
	 */
	@Override
	public void quantize(RtToken type, int one, int min, int max,
			double ditheramplitude) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.quantize(type, one, min, max, ditheramplitude);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#display(java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.Object[])
	 */
	@Override
	public void display(String name, RtToken type, RtToken mode,
			Object... parmaterlist) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.display(name, type, mode, parmaterlist);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hider(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void hider(RtToken type, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.hider(type, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#colorSamples(int, double[], double[])
	 */
	@Override
	public void colorSamples(int n, double[] nRGB, double[] RGBn) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.colorSamples(n, nRGB, RGBn);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#relativeDetail(double)
	 */
	@Override
	public void relativeDetail(double relativedetail) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.relativeDetail(relativedetail);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#option(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void option(RtToken name, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.option(name, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeBegin()
	 */
	@Override
	public void attributeBegin() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.attributeBegin();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeEnd()
	 */
	@Override
	public void attributeEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.attributeEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#color(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void color(RtColor color) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.color(color);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#opacity(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void opacity(RtColor color) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.opacity(color);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#textureCoordinates(double, double, double, double, double, double, double, double)
	 */
	@Override
	public void textureCoordinates(double s1, double t1, double s2, double t2,
			double s3, double t3, double s4, double t4) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.textureCoordinates(s1, t1, s2, t2, s3, t3, s4, t4);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#lightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle lightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			return inner.lightSource(shadername, params);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#areaLightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle areaLightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			return inner.areaLightSource(shadername, params);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#illuminate(ca.eandb.jmist.framework.loader.renderman.RtLightHandle, boolean)
	 */
	@Override
	public void illuminate(RtLightHandle light, boolean onoff) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.illuminate(light, onoff);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#surface(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void surface(RtToken shadername, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.surface(shadername, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#displacement(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void displacement(RtToken shadername, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.displacement(shadername, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#atmosphere(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void atmosphere(RtToken shadername, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.atmosphere(shadername, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#interior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void interior(RtToken shadername, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.interior(shadername, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exterior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void exterior(RtToken shadername, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.exterior(shadername, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingRate(double)
	 */
	@Override
	public void shadingRate(double size) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.shadingRate(size);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingInterpolation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void shadingInterpolation(RtToken type) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.shadingInterpolation(type);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#matte(boolean)
	 */
	@Override
	public void matte(boolean onoff) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.matte(onoff);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#bound(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void bound(RtBound bound) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.bound(bound);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detail(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void detail(RtBound bound) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.detail(bound);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detailRange(double, double, double, double)
	 */
	@Override
	public void detailRange(double minvisible, double lowertransition,
			double uppertransition, double maxvisible) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.detailRange(minvisible, lowertransition, uppertransition, maxvisible);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometricApproximation(ca.eandb.jmist.framework.loader.renderman.RtToken, double)
	 */
	@Override
	public void geometricApproximation(RtToken type, double value) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.geometricApproximation(type, value);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#orientation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void orientation(RtToken orientation) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.orientation(orientation);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#reverseOrientation()
	 */
	@Override
	public void reverseOrientation() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.reverseOrientation();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sides(int)
	 */
	@Override
	public void sides(int sides) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.sides(sides);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#identity()
	 */
	@Override
	public void identity() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.identity();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void transform(RtMatrix transform) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.transform(transform);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#concatTransform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void concatTransform(RtMatrix transform) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.concatTransform(transform);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#perspective(double)
	 */
	@Override
	public void perspective(double fov) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.perspective(fov);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#translate(double, double, double)
	 */
	@Override
	public void translate(double dx, double dy, double dz) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.translate(dx, dy, dz);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#scale(double, double, double)
	 */
	@Override
	public void scale(double sx, double sy, double sz) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.scale(sx, sy, sz);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#skew(double, double, double, double, double, double, double)
	 */
	@Override
	public void skew(double angle, double dx1, double dy1, double dz1,
			double dx2, double dy2, double dz2) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.skew(angle, dx1, dy1, dz1, dx2, dy2, dz2);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordinateSystem(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordinateSystem(RtToken name) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.coordinateSystem(name);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordSysTransform(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordSysTransform(RtToken name) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.coordSysTransform(name);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformPoints(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtPoint[])
	 */
	@Override
	public RtPoint[] transformPoints(RtToken fromspace, RtToken tospace,
			RtPoint[] points) {
		if (currentFrame < 0 || currentFrame == frame) {
			return inner.transformPoints(fromspace, tospace, points);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformBegin()
	 */
	@Override
	public void transformBegin() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.transformBegin();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformEnd()
	 */
	@Override
	public void transformEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.transformEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attribute(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void attribute(RtToken name, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.attribute(name, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#polygon(java.util.Map)
	 */
	@Override
	public void polygon(Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.polygon(params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#generalPolygon(int, int[], java.util.Map)
	 */
	@Override
	public void generalPolygon(int nloops, int[] nvertices,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.generalPolygon(nloops, nvertices, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsPolygons(int, int[], int[], java.util.Map)
	 */
	@Override
	public void pointsPolygons(int npolys, int[] nvertices, int[] vertices,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.pointsPolygons(npolys, nvertices, vertices, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsGeneralPolygons(int, int[], int[], int[], java.util.Map)
	 */
	@Override
	public void pointsGeneralPolygons(int npolys, int[] nloops,
			int[] nvertices, int[] vertices, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.pointsGeneralPolygons(npolys, nloops, nvertices, vertices, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#basis(ca.eandb.jmist.framework.loader.renderman.RtBasis, int, ca.eandb.jmist.framework.loader.renderman.RtBasis, int)
	 */
	@Override
	public void basis(RtBasis ubasis, int ustep, RtBasis vbasis, int vstep) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.basis(ubasis, ustep, vbasis, vstep);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patch(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patch(RtToken type, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.patch(type, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patchMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patchMesh(RtToken type, int nu, RtToken uwrap, int nv,
			RtToken vwrap, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.patchMesh(type, nu, uwrap, nv, vwrap, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#nuPatch(int, int, double[], double, double, int, int, double[], double, double)
	 */
	@Override
	public void nuPatch(int nu, int uorder, double[] uknot, double umin,
			double umax, int nv, int vorder, double[] vknot, double vmin,
			double vmax) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.nuPatch(nu, uorder, uknot, umin, umax, nv, vorder, vknot, vmin, vmax);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#trimCurve(int, int[], int[], double[], double, double, int[], double[], double[], double[])
	 */
	@Override
	public void trimCurve(int nloops, int[] ncurves, int[] order,
			double[] knot, double min, double max, int[] n, double[] u,
			double[] v, double[] w) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.trimCurve(nloops, ncurves, order, knot, min, max, n, u, v, w);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#subdivisionMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], int[], int, ca.eandb.jmist.framework.loader.renderman.RtToken[], int[], int[], double[], java.util.Map)
	 */
	@Override
	public void subdivisionMesh(RtToken scheme, int nfaces, int[] nvertices,
			int[] vertices, int ntags, RtToken[] tags, int[] nargs,
			int[] intargs, double[] doubleargs, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.subdivisionMesh(scheme, nfaces, nvertices, vertices, ntags, tags, nargs, intargs, doubleargs, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sphere(double, double, double, double, java.util.Map)
	 */
	@Override
	public void sphere(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.sphere(radius, zmin, zmax, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cone(double, double, double, java.util.Map)
	 */
	@Override
	public void cone(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.cone(height, radius, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cylinder(double, double, double, double, java.util.Map)
	 */
	@Override
	public void cylinder(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.cylinder(radius, zmin, zmax, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hyperboloid(ca.eandb.jmist.framework.loader.renderman.RtPoint, ca.eandb.jmist.framework.loader.renderman.RtPoint, double, java.util.Map)
	 */
	@Override
	public void hyperboloid(RtPoint point1, RtPoint point2, double thetamax,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.hyperboloid(point1, point2, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#paraboloid(double, double, double, double, java.util.Map)
	 */
	@Override
	public void paraboloid(double rmax, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.paraboloid(rmax, zmin, zmax, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#disk(double, double, double, java.util.Map)
	 */
	@Override
	public void disk(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.disk(height, radius, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#torus(double, double, double, double, double, java.util.Map)
	 */
	@Override
	public void torus(double majorradius, double minorradius, double phimin,
			double phimax, double thetamax, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.torus(majorradius, minorradius, phimin, phimax, thetamax, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#points(int, java.util.Map)
	 */
	@Override
	public void points(int npoints, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.points(npoints, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#curves(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void curves(RtToken type, int ncurves, int[] nvertices,
			RtToken wrap, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.curves(type, ncurves, nvertices, wrap, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#blobby(int, int, int[], int, double[], int, java.lang.String[], java.util.Map)
	 */
	@Override
	public void blobby(int nleaf, int ncode, int[] code, int ndoubles,
			double[] doubles, int nstrings, String[] strings,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.blobby(nleaf, ncode, code, ndoubles, doubles, nstrings, strings, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procedural(java.lang.Object, ca.eandb.jmist.framework.loader.renderman.RtBound, ca.eandb.jmist.framework.loader.renderman.RtProcSubdivFunc)
	 */
	@Override
	public void procedural(Object data, RtBound bound,
			RtProcSubdivFunc subdividefunc) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.procedural(data, bound, subdividefunc);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procRunProgram(java.lang.Object, double)
	 */
	@Override
	public void procRunProgram(Object data, double detail) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.procRunProgram(data, detail);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procDynamicLoad(java.lang.Object, double)
	 */
	@Override
	public void procDynamicLoad(Object data, double detail) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.procDynamicLoad(data, detail);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometry(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void geometry(RtToken type, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.geometry(type, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidBegin(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void solidBegin(RtToken operation) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.solidBegin(operation);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidEnd()
	 */
	@Override
	public void solidEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.solidEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectBegin()
	 */
	@Override
	public RtObjectHandle objectBegin() {
		if (currentFrame < 0 || currentFrame == frame) {
			return inner.objectBegin();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectEnd()
	 */
	@Override
	public void objectEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.objectEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectInstance(ca.eandb.jmist.framework.loader.renderman.RtObjectHandle)
	 */
	@Override
	public void objectInstance(RtObjectHandle handle) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.objectInstance(handle);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionBegin(int, double[])
	 */
	@Override
	public void motionBegin(int n, double... t) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.motionBegin(n, t);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionEnd()
	 */
	@Override
	public void motionEnd() {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.motionEnd();
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeTexture(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeTexture(String picturename, String texturename,
			RtToken swrap, RtToken twrap, RtFilterFunc filterfunc,
			double swidth, double twidth, Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.makeTexture(picturename, texturename, swrap, twrap, filterfunc, swidth, twidth, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeLatLongEnvironment(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeLatLongEnvironment(String picturename, String texturename,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.makeLatLongEnvironment(picturename, texturename, filterfunc, swidth, twidth, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeCubeFaceEnvironment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, double, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeCubeFaceEnvironment(String px, String nx, String py,
			String ny, String pz, String nz, String texturename, double fov,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.makeCubeFaceEnvironment(px, nx, py, ny, pz, nz, texturename, fov, filterfunc, swidth, twidth, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeShadow(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void makeShadow(String picturename, String texturename,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.makeShadow(picturename, texturename, params);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#errorHandler(ca.eandb.jmist.framework.loader.renderman.RtErrorHandler)
	 */
	@Override
	public void errorHandler(RtErrorHandler handler) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.errorHandler(handler);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#archiveRecord(ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.String, java.lang.String[])
	 */
	@Override
	public void archiveRecord(RtToken type, String format, String... arg) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.archiveRecord(type, format, arg);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#readArchive(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback, java.util.Map)
	 */
	@Override
	public void readArchive(RtToken name, RtArchiveCallback callback,
			Map<RtToken, Object> params) {
		if (currentFrame < 0 || currentFrame == frame) {
			inner.readArchive(name, callback, params);
		}
	}

}
