/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ca.eandb.jmist.framework.AffineTransformation3;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.ConstructiveSolidGeometry;
import ca.eandb.jmist.framework.geometry.IntersectionGeometry;
import ca.eandb.jmist.framework.geometry.SubtractionGeometry;
import ca.eandb.jmist.framework.geometry.UnionGeometry;
import ca.eandb.jmist.framework.geometry.primitive.ConeGeometry;
import ca.eandb.jmist.framework.geometry.primitive.SphereGeometry;
import ca.eandb.jmist.framework.geometry.primitive.TorusGeometry;
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
import ca.eandb.jmist.framework.scene.BranchSceneElement;
import ca.eandb.jmist.framework.scene.NullSceneElement;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point3;

/**
 * @author Brad
 *
 */
public class SceneContext extends AbstractContext {
	
	private RtToken currentSolidOperation = null;
	private Stack<ConstructiveSolidGeometry> currentSolid = new Stack<ConstructiveSolidGeometry>();
	private SceneElement currentPrimitive = null;

	private SceneElement worldRoot = null;
	private int currentFrame = -1;
	
	private final AffineTransformation3 currentTransformation = new AffineTransformation3();
	private final AffineTransformation3 worldToCamera = new AffineTransformation3();
	
	private int horizontalResolution = 640;
	private int verticalResolution = 480;
	private double pixelAspectRatio = 1.0;
	private double frameAspectRatio = 4.0/3.0;
	private Box2 screenWindow = new Box2(-4.0/3.0, -1.0, 4.0/3.0, 1.0);
	private Box2 cropWindow = Box2.UNIVERSE;
	private RtToken cameraProjection = RI_ORTHOGRAPHIC;
	private Interval nearFarClipping = new Interval(MathUtil.EPSILON, Double.POSITIVE_INFINITY);
	private final List<Plane3> otherClippingPlanes = new ArrayList<Plane3>();
	private double fStop = Double.POSITIVE_INFINITY;
	private double focalDistance = Double.NaN;
	private double focalLength = Double.NaN;
	private double shutterOpen = 0.0;
	private double shutterClose = 0.0;


	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#archiveRecord(ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.String, java.lang.String[])
	 */
	@Override
	public void archiveRecord(RtToken type, String format, String... arg) {
		// TODO Auto-generated method stub
		super.archiveRecord(type, format, arg);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#areaLightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle areaLightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		return super.areaLightSource(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#atmosphere(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void atmosphere(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.atmosphere(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#attribute(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void attribute(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.attribute(name, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#attributeBegin()
	 */
	@Override
	public void attributeBegin() {
		// TODO Auto-generated method stub
		super.attributeBegin();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#attributeEnd()
	 */
	@Override
	public void attributeEnd() {
		// TODO Auto-generated method stub
		super.attributeEnd();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#basis(ca.eandb.jmist.framework.loader.renderman.RtBasis, int, ca.eandb.jmist.framework.loader.renderman.RtBasis, int)
	 */
	@Override
	public void basis(RtBasis ubasis, int ustep, RtBasis vbasis, int vstep) {
		// TODO Auto-generated method stub
		super.basis(ubasis, ustep, vbasis, vstep);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#blobby(int, int, int[], int, double[], int, java.lang.String[], java.util.Map)
	 */
	@Override
	public void blobby(int nleaf, int ncode, int[] code, int ndoubles,
			double[] doubles, int nstrings, String[] strings,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.blobby(nleaf, ncode, code, ndoubles, doubles, nstrings, strings, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#bound(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void bound(RtBound bound) {
		// TODO Auto-generated method stub
		super.bound(bound);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#clipping(double, double)
	 */
	@Override
	public void clipping(double near, double far) {
		// TODO Auto-generated method stub
		super.clipping(near, far);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#color(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void color(RtColor color) {
		// TODO Auto-generated method stub
		super.color(color);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#colorSamples(int, double[], double[])
	 */
	@Override
	public void colorSamples(int n, double[] nRGB, double[] RGBn) {
		// TODO Auto-generated method stub
		super.colorSamples(n, nRGB, RGBn);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#concatTransform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void concatTransform(RtMatrix transform) {
		// TODO Auto-generated method stub
		super.concatTransform(transform);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#cone(double, double, double, java.util.Map)
	 */
	@Override
	public void cone(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		super.cone(height, radius, thetamax, params);
		
		addPrimitive(new ConeGeometry(radius, height));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#coordinateSystem(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordinateSystem(RtToken name) {
		// TODO Auto-generated method stub
		super.coordinateSystem(name);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#coordSysTransform(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordSysTransform(RtToken name) {
		// TODO Auto-generated method stub
		super.coordSysTransform(name);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#cropWindow(double, double, double, double)
	 */
	@Override
	public void cropWindow(double xmin, double xmax, double ymin, double ymax) {
		super.cropWindow(xmin, xmax, ymin, ymax);
		this.cropWindow = new Box2(xmin, ymin, xmax, ymax);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#curves(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void curves(RtToken type, int ncurves, int[] nvertices,
			RtToken wrap, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.curves(type, ncurves, nvertices, wrap, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#cylinder(double, double, double, double, java.util.Map)
	 */
	@Override
	public void cylinder(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.cylinder(radius, zmin, zmax, thetamax, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#declare(java.lang.String, java.lang.String)
	 */
	@Override
	public RtToken declare(String name, String declaration) {
		// TODO Auto-generated method stub
		return super.declare(name, declaration);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#depthOfField(double, double, double)
	 */
	@Override
	public void depthOfField(double fstop, double focallength,
			double focaldistance) {
		// TODO Auto-generated method stub
		super.depthOfField(fstop, focallength, focaldistance);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#detail(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void detail(RtBound bound) {
		// TODO Auto-generated method stub
		super.detail(bound);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#detailRange(double, double, double, double)
	 */
	@Override
	public void detailRange(double minvisible, double lowertransition,
			double uppertransition, double maxvisible) {
		// TODO Auto-generated method stub
		super.detailRange(minvisible, lowertransition, uppertransition, maxvisible);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#disk(double, double, double, java.util.Map)
	 */
	@Override
	public void disk(double height, double radius, double thetamax,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.disk(height, radius, thetamax, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#displacement(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void displacement(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.displacement(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#display(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.Object[])
	 */
	@Override
	public void display(RtToken name, RtToken type, RtToken mode,
			Object... parmaterlist) {
		// TODO Auto-generated method stub
		super.display(name, type, mode, parmaterlist);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#errorHandler(ca.eandb.jmist.framework.loader.renderman.RtErrorHandler)
	 */
	@Override
	public void errorHandler(RtErrorHandler handler) {
		// TODO Auto-generated method stub
		super.errorHandler(handler);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#exposure(double, double)
	 */
	@Override
	public void exposure(double gain, double gamma) {
		// TODO Auto-generated method stub
		super.exposure(gain, gamma);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#exterior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void exterior(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.exterior(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#format(int, int, double)
	 */
	@Override
	public void format(int xresolution, int yresolution, double pixelaspectratio) {
		super.format(xresolution, yresolution, pixelaspectratio);
		this.horizontalResolution = xresolution;
		this.verticalResolution = yresolution;
		this.pixelAspectRatio = pixelaspectratio;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#frameAspectRatio(double)
	 */
	@Override
	public void frameAspectRatio(double frameaspectratio) {
		super.frameAspectRatio(frameaspectratio);
		this.frameAspectRatio = frameaspectratio;
		if (frameaspectratio > 1.0) {
			this.screenWindow = new Box2(-frameaspectratio, -1.0, frameaspectratio, 1.0);
		} else {
			this.screenWindow = new Box2(-1.0, -1.0 / frameaspectratio, 1.0, 1.0 / frameaspectratio);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#frameBegin(int)
	 */
	@Override
	public void frameBegin(int frame) {
		super.frameBegin(frame);
		currentFrame = frame;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#frameEnd()
	 */
	@Override
	public void frameEnd() {
		// TODO Auto-generated method stub
		super.frameEnd();
		currentFrame = -1;
		worldRoot = null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#generalPolygon(int, int[], java.util.Map)
	 */
	@Override
	public void generalPolygon(int nloops, int[] nvertices,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.generalPolygon(nloops, nvertices, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#geometricApproximation(ca.eandb.jmist.framework.loader.renderman.RtToken, double)
	 */
	@Override
	public void geometricApproximation(RtToken type, double value) {
		// TODO Auto-generated method stub
		super.geometricApproximation(type, value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#geometry(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void geometry(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.geometry(type, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#hider(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void hider(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.hider(type, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#hyperboloid(ca.eandb.jmist.framework.loader.renderman.RtPoint, ca.eandb.jmist.framework.loader.renderman.RtPoint, double, java.util.Map)
	 */
	@Override
	public void hyperboloid(RtPoint point1, RtPoint point2, double thetamax,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.hyperboloid(point1, point2, thetamax, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#identity()
	 */
	@Override
	public void identity() {
		// TODO Auto-generated method stub
		super.identity();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#illuminate(ca.eandb.jmist.framework.loader.renderman.RtLightHandle, boolean)
	 */
	@Override
	public void illuminate(RtLightHandle light, boolean onoff) {
		// TODO Auto-generated method stub
		super.illuminate(light, onoff);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#imager(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void imager(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.imager(name, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#interior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void interior(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.interior(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#lightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle lightSource(RtToken shadername,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		return super.lightSource(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#makeCubeFaceEnvironment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, double, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeCubeFaceEnvironment(String px, String nx, String py,
			String ny, String pz, String nz, String texturename, double fov,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.makeCubeFaceEnvironment(px, nx, py, ny, pz, nz, texturename, fov,
				filterfunc, swidth, twidth, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#makeLatLongEnvironment(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeLatLongEnvironment(String picturename, String texturename,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.makeLatLongEnvironment(picturename, texturename, filterfunc, swidth,
				twidth, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#makeShadow(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void makeShadow(String picturename, String texturename,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.makeShadow(picturename, texturename, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#makeTexture(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeTexture(String picturename, String texturename,
			RtToken swrap, RtToken twrap, RtFilterFunc filterfunc,
			double swidth, double twidth, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.makeTexture(picturename, texturename, swrap, twrap, filterfunc, swidth,
				twidth, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#matte(boolean)
	 */
	@Override
	public void matte(boolean onoff) {
		// TODO Auto-generated method stub
		super.matte(onoff);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#motionBegin(int, double[])
	 */
	@Override
	public void motionBegin(int n, double... t) {
		// TODO Auto-generated method stub
		super.motionBegin(n, t);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#motionEnd()
	 */
	@Override
	public void motionEnd() {
		// TODO Auto-generated method stub
		super.motionEnd();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#nuPatch(int, int, double[], double, double, int, int, double[], double, double)
	 */
	@Override
	public void nuPatch(int nu, int uorder, double[] uknot, double umin,
			double umax, int nv, int vorder, double[] vknot, double vmin,
			double vmax) {
		// TODO Auto-generated method stub
		super.nuPatch(nu, uorder, uknot, umin, umax, nv, vorder, vknot, vmin, vmax);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#objectBegin()
	 */
	@Override
	public RtObjectHandle objectBegin() {
		// TODO Auto-generated method stub
		return super.objectBegin();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#objectEnd()
	 */
	@Override
	public void objectEnd() {
		// TODO Auto-generated method stub
		super.objectEnd();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#objectInstance(ca.eandb.jmist.framework.loader.renderman.RtObjectHandle)
	 */
	@Override
	public void objectInstance(RtObjectHandle handle) {
		// TODO Auto-generated method stub
		super.objectInstance(handle);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#opacity(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void opacity(RtColor color) {
		// TODO Auto-generated method stub
		super.opacity(color);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#option(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void option(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.option(name, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#orientation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void orientation(RtToken orientation) {
		// TODO Auto-generated method stub
		super.orientation(orientation);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#paraboloid(double, double, double, double, java.util.Map)
	 */
	@Override
	public void paraboloid(double rmax, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.paraboloid(rmax, zmin, zmax, thetamax, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#patch(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patch(RtToken type, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.patch(type, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#patchMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patchMesh(RtToken type, int nu, RtToken uwrap, int nv,
			RtToken vwrap, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.patchMesh(type, nu, uwrap, nv, vwrap, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#perspective(double)
	 */
	@Override
	public void perspective(double fov) {
		// TODO Auto-generated method stub
		super.perspective(fov);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#pixelFilter(ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double)
	 */
	@Override
	public void pixelFilter(RtFilterFunc filterfunc, double xwidth,
			double ywidth) {
		// TODO Auto-generated method stub
		super.pixelFilter(filterfunc, xwidth, ywidth);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#pixelSamples(double, double)
	 */
	@Override
	public void pixelSamples(double xsamples, double ysamples) {
		// TODO Auto-generated method stub
		super.pixelSamples(xsamples, ysamples);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#pixelVariance(double)
	 */
	@Override
	public void pixelVariance(double variation) {
		// TODO Auto-generated method stub
		super.pixelVariance(variation);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#points(int, java.util.Map)
	 */
	@Override
	public void points(int npoints, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.points(npoints, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#pointsGeneralPolygons(int, int[], int[], int[], java.util.Map)
	 */
	@Override
	public void pointsGeneralPolygons(int npolys, int[] nloops,
			int[] nvertices, int[] vertices, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.pointsGeneralPolygons(npolys, nloops, nvertices, vertices, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#pointsPolygons(int, int[], int[], java.util.Map)
	 */
	@Override
	public void pointsPolygons(int npolys, int[] nvertices, int[] vertices,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.pointsPolygons(npolys, nvertices, vertices, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#polygon(int, java.util.Map)
	 */
	@Override
	public void polygon(int nvertices, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.polygon(nvertices, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#procDynamicLoad(java.lang.Object, double)
	 */
	@Override
	public void procDynamicLoad(Object data, double detail) {
		// TODO Auto-generated method stub
		super.procDynamicLoad(data, detail);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#procedural(java.lang.Object, ca.eandb.jmist.framework.loader.renderman.RtBound, ca.eandb.jmist.framework.loader.renderman.RtProcSubdivFunc)
	 */
	@Override
	public void procedural(Object data, RtBound bound,
			RtProcSubdivFunc subdividefunc) {
		// TODO Auto-generated method stub
		super.procedural(data, bound, subdividefunc);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#procRunProgram(java.lang.Object, double)
	 */
	@Override
	public void procRunProgram(Object data, double detail) {
		// TODO Auto-generated method stub
		super.procRunProgram(data, detail);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#projection(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void projection(RtToken name, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.projection(name, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#quantize(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int, int, double)
	 */
	@Override
	public void quantize(RtToken type, int one, int min, int max,
			double ditheramplitude) {
		// TODO Auto-generated method stub
		super.quantize(type, one, min, max, ditheramplitude);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#readArchive(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback, java.util.Map)
	 */
	@Override
	public void readArchive(RtToken name, RtArchiveCallback callback,
			Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.readArchive(name, callback, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#relativeDetail(double)
	 */
	@Override
	public void relativeDetail(double relativedetail) {
		// TODO Auto-generated method stub
		super.relativeDetail(relativedetail);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#reverseOrientation()
	 */
	@Override
	public void reverseOrientation() {
		// TODO Auto-generated method stub
		super.reverseOrientation();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#scale(double, double, double)
	 */
	@Override
	public void scale(double sx, double sy, double sz) {
		// TODO Auto-generated method stub
		super.scale(sx, sy, sz);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#screenWindow(double, double, double, double)
	 */
	@Override
	public void screenWindow(double left, double right, double bottom,
			double top) {
		super.screenWindow(left, right, bottom, top);
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#shadingInterpolation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void shadingInterpolation(RtToken type) {
		// TODO Auto-generated method stub
		super.shadingInterpolation(type);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#shadingRate(double)
	 */
	@Override
	public void shadingRate(double size) {
		// TODO Auto-generated method stub
		super.shadingRate(size);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#shutter(double, double)
	 */
	@Override
	public void shutter(double min, double max) {
		// TODO Auto-generated method stub
		super.shutter(min, max);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#sides(int)
	 */
	@Override
	public void sides(int sides) {
		// TODO Auto-generated method stub
		super.sides(sides);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#skew(double, double, double, double, double, double, double)
	 */
	@Override
	public void skew(double angle, double dx1, double dy1, double dz1,
			double dx2, double dy2, double dz2) {
		// TODO Auto-generated method stub
		super.skew(angle, dx1, dy1, dz1, dx2, dy2, dz2);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#solidBegin(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void solidBegin(RtToken operation) {
		super.solidBegin(operation);
	
		currentSolidOperation = operation;
		if (operation == RI_INTERSECTION) {
			currentSolid.push(new IntersectionGeometry());
		} else if (operation == RI_UNION) {
			currentSolid.push(new UnionGeometry());
		} else if (operation == RI_DIFFERENCE) {
			currentSolid.push(new SubtractionGeometry());
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#solidEnd()
	 */
	@Override
	public void solidEnd() {
		super.solidEnd();
		
		if (currentSolidOperation == RI_PRIMITIVE) {
			currentSolid.peek().addChild(
					currentPrimitive != null ? currentPrimitive
							: NullSceneElement.INSTANCE);
		} else {
			ConstructiveSolidGeometry top = currentSolid.pop();
			currentSolid.peek().addChild(top);
		}
	}
	
	private void addPrimitive(SceneElement e) {
		if (currentPrimitive == null) {
			currentPrimitive = e;
		} else if (currentPrimitive instanceof BranchSceneElement) {
			((BranchSceneElement) currentPrimitive).addChild(e);
		} else {
			currentPrimitive = new BranchSceneElement().addChild(e);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#sphere(double, double, double, double, java.util.Map)
	 */
	@Override
	public void sphere(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, Object> params) {
		super.sphere(radius, zmin, zmax, thetamax, params);
		
		// TODO: Handle zmin, zmax, thetamax.
		addPrimitive(new SphereGeometry(Point3.ORIGIN, radius));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#subdivisionMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int[], int[], int, ca.eandb.jmist.framework.loader.renderman.RtToken[], int[], int[], double[], java.util.Map)
	 */
	@Override
	public void subdivisionMesh(RtToken scheme, int nfaces, int[] nvertices,
			int[] vertices, int ntags, RtToken[] tags, int[] nargs,
			int[] intargs, double[] doubleargs, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.subdivisionMesh(scheme, nfaces, nvertices, vertices, ntags, tags, nargs,
				intargs, doubleargs, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#surface(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void surface(RtToken shadername, Map<RtToken, Object> params) {
		// TODO Auto-generated method stub
		super.surface(shadername, params);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#textureCoordinates(double, double, double, double, double, double, double, double)
	 */
	@Override
	public void textureCoordinates(double s1, double t1, double s2, double t2,
			double s3, double t3, double s4, double t4) {
		// TODO Auto-generated method stub
		super.textureCoordinates(s1, t1, s2, t2, s3, t3, s4, t4);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#torus(double, double, double, double, double, java.util.Map)
	 */
	@Override
	public void torus(double majorradius, double minorradius, double phimin,
			double phimax, double thetamax, Map<RtToken, Object> params) {
		super.torus(majorradius, minorradius, phimin, phimax, thetamax, params);
		
		// TODO: Handle phimin, phimax, thetamax
		addPrimitive(new TorusGeometry(majorradius, minorradius));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#transform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void transform(RtMatrix transform) {
		// TODO Auto-generated method stub
		super.transform(transform);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#transformBegin()
	 */
	@Override
	public void transformBegin() {
		// TODO Auto-generated method stub
		super.transformBegin();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#transformEnd()
	 */
	@Override
	public void transformEnd() {
		// TODO Auto-generated method stub
		super.transformEnd();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#transformPoints(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtPoint[])
	 */
	@Override
	public RtPoint[] transformPoints(RtToken fromspace, RtToken tospace, int n,
			RtPoint[] points) {
		// TODO Auto-generated method stub
		return super.transformPoints(fromspace, tospace, n, points);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#translate(double, double, double)
	 */
	@Override
	public void translate(double dx, double dy, double dz) {
		// TODO Auto-generated method stub
		super.translate(dx, dy, dz);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#trimCurve(int, int[], int[], double[], double, double, int[], double[], double[], double[])
	 */
	@Override
	public void trimCurve(int nloops, int[] ncurves, int[] order,
			double[] knot, double min, double max, int[] n, double[] u,
			double[] v, double[] w) {
		// TODO Auto-generated method stub
		super.trimCurve(nloops, ncurves, order, knot, min, max, n, u, v, w);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#worldBegin()
	 */
	@Override
	public void worldBegin() {
		super.worldBegin();
		worldToCamera.reset();
		worldToCamera.stretch(1.0, 1.0, -1.0);
		currentTransformation.apply(worldToCamera);
		currentTransformation.reset();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.scene.AbstractContext#worldEnd()
	 */
	@Override
	public void worldEnd() {
		// TODO Auto-generated method stub
		super.worldEnd();
	}

}
