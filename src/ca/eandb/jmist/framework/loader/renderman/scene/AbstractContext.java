/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman.scene;

import java.util.Map;

import ca.eandb.jmist.framework.loader.renderman.RenderManContext;
import ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback;
import ca.eandb.jmist.framework.loader.renderman.RtBasis;
import ca.eandb.jmist.framework.loader.renderman.RtBound;
import ca.eandb.jmist.framework.loader.renderman.RtColor;
import ca.eandb.jmist.framework.loader.renderman.RtErrorHandler;
import ca.eandb.jmist.framework.loader.renderman.RtFilterFunc;
import ca.eandb.jmist.framework.loader.renderman.RtLightHandle;
import ca.eandb.jmist.framework.loader.renderman.RtMatrix;
import ca.eandb.jmist.framework.loader.renderman.RtObjectHandle;
import ca.eandb.jmist.framework.loader.renderman.RtPoint;
import ca.eandb.jmist.framework.loader.renderman.RtProcSubdivFunc;
import ca.eandb.jmist.framework.loader.renderman.RtToken;

/**
 * @author Brad
 *
 */
public abstract class AbstractContext implements RenderManContext {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#archiveRecord(ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.String, java.lang.String[])
	 */
	@Override
	public void archiveRecord(RtToken type, String format, String... arg) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#areaLightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle areaLightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#atmosphere(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void atmosphere(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attribute(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void attribute(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeBegin()
	 */
	@Override
	public void attributeBegin() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeEnd()
	 */
	@Override
	public void attributeEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#basis(ca.eandb.jmist.framework.loader.renderman.RtBasis, int, ca.eandb.jmist.framework.loader.renderman.RtBasis, int)
	 */
	@Override
	public void basis(RtBasis ubasis, int ustep, RtBasis vbasis, int vstep) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#blobby(int, int, int[], int, double[], int, java.lang.String[], java.util.Map)
	 */
	@Override
	public void blobby(int nleaf, int ncode, int[] code, int ndoubles,
			double[] doubles, int nstrings, String[] strings,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#bound(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void bound(RtBound bound) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#clipping(double, double)
	 */
	@Override
	public void clipping(double near, double far) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#color(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void color(RtColor color) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#colorSamples(int, double[], double[])
	 */
	@Override
	public void colorSamples(int n, double[] nRGB, double[] RGBn) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#concatTransform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void concatTransform(RtMatrix transform) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cone(double, double, double, java.util.Map)
	 */
	@Override
	public void cone(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordSysTransform(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordSysTransform(RtToken name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordinateSystem(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordinateSystem(RtToken name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cropWindow(double, double, double, double)
	 */
	@Override
	public void cropWindow(double xmin, double xmax, double ymin, double ymax) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#curves(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void curves(RtToken type, int ncurves, int[] nvertices,
			RtToken wrap, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cylinder(double, double, double, double, java.util.Map)
	 */
	@Override
	public void cylinder(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#declare(java.lang.String, java.lang.String)
	 */
	@Override
	public RtToken declare(String name, String declaration) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#depthOfField(double, double, double)
	 */
	@Override
	public void depthOfField(double fstop, double focallength,
			double focaldistance) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detail(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void detail(RtBound bound) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detailRange(double, double, double, double)
	 */
	@Override
	public void detailRange(double minvisible, double lowertransition,
			double uppertransition, double maxvisible) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#disk(double, double, double, java.util.Map)
	 */
	@Override
	public void disk(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#displacement(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void displacement(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#display(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.Object[])
	 */
	@Override
	public void display(RtToken name, RtToken type, RtToken mode,
			Object... parmaterlist) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#errorHandler(ca.eandb.jmist.framework.loader.renderman.RtErrorHandler)
	 */
	@Override
	public void errorHandler(RtErrorHandler handler) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exposure(double, double)
	 */
	@Override
	public void exposure(double gain, double gamma) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exterior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void exterior(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#format(int, int, double)
	 */
	@Override
	public void format(int xresolution, int yresolution, double pixelaspectratio) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameAspectRatio(double)
	 */
	@Override
	public void frameAspectRatio(double frameaspectratio) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameBegin(int)
	 */
	@Override
	public void frameBegin(int frame) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameEnd()
	 */
	@Override
	public void frameEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#generalPolygon(int, int[], java.util.Map)
	 */
	@Override
	public void generalPolygon(int nloops, int[] nvertices,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometricApproximation(ca.eandb.jmist.framework.loader.renderman.RtToken, double)
	 */
	@Override
	public void geometricApproximation(RtToken type, double value) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometry(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void geometry(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hider(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void hider(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hyperboloid(ca.eandb.jmist.framework.loader.renderman.RtPoint, ca.eandb.jmist.framework.loader.renderman.RtPoint, double, java.util.Map)
	 */
	@Override
	public void hyperboloid(RtPoint point1, RtPoint point2, double thetamax,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#identity()
	 */
	@Override
	public void identity() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#illuminate(ca.eandb.jmist.framework.loader.renderman.RtLightHandle, boolean)
	 */
	@Override
	public void illuminate(RtLightHandle light, boolean onoff) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#imager(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void imager(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#interior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void interior(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#lightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle lightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeCubeFaceEnvironment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, double, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeCubeFaceEnvironment(String px, String nx, String py,
			String ny, String pz, String nz, String texturename, double fov,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeLatLongEnvironment(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeLatLongEnvironment(String picturename, String texturename,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeShadow(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void makeShadow(String picturename, String texturename,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeTexture(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeTexture(String picturename, String texturename,
			RtToken swrap, RtToken twrap, RtFilterFunc filterfunc,
			double swidth, double twidth, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#matte(boolean)
	 */
	@Override
	public void matte(boolean onoff) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionBegin(int, double[])
	 */
	@Override
	public void motionBegin(int n, double... t) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionEnd()
	 */
	@Override
	public void motionEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#nuPatch(int, int, double[], double, double, int, int, double[], double, double)
	 */
	@Override
	public void nuPatch(int nu, int uorder, double[] uknot, double umin,
			double umax, int nv, int vorder, double[] vknot, double vmin,
			double vmax) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectBegin()
	 */
	@Override
	public RtObjectHandle objectBegin() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectEnd()
	 */
	@Override
	public void objectEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectInstance(ca.eandb.jmist.framework.loader.renderman.RtObjectHandle)
	 */
	@Override
	public void objectInstance(RtObjectHandle handle) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#opacity(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void opacity(RtColor color) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#option(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void option(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#orientation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void orientation(RtToken orientation) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#paraboloid(double, double, double, double, java.util.Map)
	 */
	@Override
	public void paraboloid(double rmax, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patch(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patch(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patchMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patchMesh(RtToken type, int nu, RtToken uwrap, int nv,
			RtToken vwrap, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#perspective(double)
	 */
	@Override
	public void perspective(double fov) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelFilter(ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double)
	 */
	@Override
	public void pixelFilter(RtFilterFunc filterfunc, double xwidth,
			double ywidth) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelSamples(double, double)
	 */
	@Override
	public void pixelSamples(double xsamples, double ysamples) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelVariance(double)
	 */
	@Override
	public void pixelVariance(double variation) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#points(int, java.util.Map)
	 */
	@Override
	public void points(int npoints, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsGeneralPolygons(int, int[], int[], int[], java.util.Map)
	 */
	@Override
	public void pointsGeneralPolygons(int npolys, int[] nloops,
			int[] nvertices, int[] vertices, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsPolygons(int, int[], int[], java.util.Map)
	 */
	@Override
	public void pointsPolygons(int npolys, int[] nvertices, int[] vertices,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#polygon(int, java.util.Map)
	 */
	@Override
	public void polygon(int nvertices, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procDynamicLoad(java.lang.Object, double)
	 */
	@Override
	public void procDynamicLoad(Object data, double detail) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procRunProgram(java.lang.Object, double)
	 */
	@Override
	public void procRunProgram(Object data, double detail) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procedural(java.lang.Object, ca.eandb.jmist.framework.loader.renderman.RtBound, ca.eandb.jmist.framework.loader.renderman.RtProcSubdivFunc)
	 */
	@Override
	public void procedural(Object data, RtBound bound,
			RtProcSubdivFunc subdividefunc) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#projection(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void projection(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#quantize(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int, int, double)
	 */
	@Override
	public void quantize(RtToken type, int one, int min, int max,
			double ditheramplitude) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#readArchive(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback, java.util.Map)
	 */
	@Override
	public void readArchive(RtToken name, RtArchiveCallback callback,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#relativeDetail(double)
	 */
	@Override
	public void relativeDetail(double relativedetail) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#reverseOrientation()
	 */
	@Override
	public void reverseOrientation() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#scale(double, double, double)
	 */
	@Override
	public void scale(double sx, double sy, double sz) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#screenWindow(double, double, double, double)
	 */
	@Override
	public void screenWindow(double left, double right, double bottom,
			double top) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingInterpolation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void shadingInterpolation(RtToken type) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingRate(double)
	 */
	@Override
	public void shadingRate(double size) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shutter(double, double)
	 */
	@Override
	public void shutter(double min, double max) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sides(int)
	 */
	@Override
	public void sides(int sides) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#skew(double, double, double, double, double, double, double)
	 */
	@Override
	public void skew(double angle, double dx1, double dy1, double dz1,
			double dx2, double dy2, double dz2) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidBegin(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void solidBegin(RtToken operation) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidEnd()
	 */
	@Override
	public void solidEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sphere(double, double, double, double, java.util.Map)
	 */
	@Override
	public void sphere(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#subdivisionMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], int[], int, ca.eandb.jmist.framework.loader.renderman.RtToken[], int[], int[], double[], java.util.Map)
	 */
	@Override
	public void subdivisionMesh(RtToken scheme, int nfaces, int[] nvertices,
			int[] vertices, int ntags, RtToken[] tags, int[] nargs,
			int[] intargs, double[] doubleargs, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#surface(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void surface(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#textureCoordinates(double, double, double, double, double, double, double, double)
	 */
	@Override
	public void textureCoordinates(double s1, double t1, double s2, double t2,
			double s3, double t3, double s4, double t4) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#torus(double, double, double, double, double, java.util.Map)
	 */
	@Override
	public void torus(double majorradius, double minorradius, double phimin,
			double phimax, double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void transform(RtMatrix transform) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformBegin()
	 */
	@Override
	public void transformBegin() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformEnd()
	 */
	@Override
	public void transformEnd() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformPoints(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtPoint[])
	 */
	@Override
	public RtPoint[] transformPoints(RtToken fromspace, RtToken tospace, int n,
			RtPoint[] points) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#translate(double, double, double)
	 */
	@Override
	public void translate(double dx, double dy, double dz) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#trimCurve(int, int[], int[], double[], double, double, int[], double[], double[], double[])
	 */
	@Override
	public void trimCurve(int nloops, int[] ncurves, int[] order,
			double[] knot, double min, double max, int[] n, double[] u,
			double[] v, double[] w) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldBegin()
	 */
	@Override
	public void worldBegin() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldEnd()
	 */
	@Override
	public void worldEnd() {
		// TODO Auto-generated method stub

	}

}
