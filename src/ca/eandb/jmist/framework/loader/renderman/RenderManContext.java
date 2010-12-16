/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.Map;

/**
 * @author Brad
 *
 */
public interface RenderManContext {
	
	public static final RtToken RI_NULL = null;

	public static final RtToken RI_FRAMEBUFFER = TokenFactory.INSTANCE.create("framebuffer");
	public static final RtToken RI_FILE = TokenFactory.INSTANCE.create("file");
	
	public static final RtToken RI_RGB = TokenFactory.INSTANCE.create("rgb");
	public static final RtToken RI_RGBA = TokenFactory.INSTANCE.create("rgba");
	public static final RtToken RI_RGBZ = TokenFactory.INSTANCE.create("rgbz");
	public static final RtToken RI_RGBAZ = TokenFactory.INSTANCE.create("rgbaz");
	public static final RtToken RI_A = TokenFactory.INSTANCE.create("a");
	public static final RtToken RI_Z = TokenFactory.INSTANCE.create("z");
	public static final RtToken RI_AZ = TokenFactory.INSTANCE.create("az");
	
	public static final RtToken RI_PERSPECTIVE = TokenFactory.INSTANCE.create("perspective");
	public static final RtToken RI_ORTHOGRAPHIC = TokenFactory.INSTANCE.create("orthographic");

	public static final RtToken RI_HIDDEN = TokenFactory.INSTANCE.create("hidden");
	public static final RtToken RI_PAINT = TokenFactory.INSTANCE.create("paint");
	
	public static final RtToken RI_CONSTANT = TokenFactory.INSTANCE.create("constant");
	public static final RtToken RI_SMOOTH = TokenFactory.INSTANCE.create("smooth");
	
	public static final RtToken RI_FLATNESS = TokenFactory.INSTANCE.create("flatness");
	public static final RtToken RI_FOV = TokenFactory.INSTANCE.create("fov");

	public static final RtToken RI_AMBIENTLIGHT = TokenFactory.INSTANCE.create("ambientlight");
	public static final RtToken RI_POINTLIGHT = TokenFactory.INSTANCE.create("pointlight");
	public static final RtToken RI_DISTANTLIGHT = TokenFactory.INSTANCE.create("distantlight");
	public static final RtToken RI_SPOTLIGHT = TokenFactory.INSTANCE.create("spotlight");

	public static final RtToken RI_INTENSITY = TokenFactory.INSTANCE.create("intensity");
	public static final RtToken RI_LIGHTCOLOR = TokenFactory.INSTANCE.create("lightcolor");
	public static final RtToken RI_FROM = TokenFactory.INSTANCE.create("from");
	public static final RtToken RI_TO = TokenFactory.INSTANCE.create("to");
	public static final RtToken RI_CONEANGLE = TokenFactory.INSTANCE.create("coneangle");
	public static final RtToken RI_CONEDELTAANGLE = TokenFactory.INSTANCE.create("conedeltaangle");
	public static final RtToken RI_BEAMDISTRIBUTION = TokenFactory.INSTANCE.create("beamdistribution");
	
	public static final RtToken RI_MATTE = TokenFactory.INSTANCE.create("matte");
	public static final RtToken RI_METAL = TokenFactory.INSTANCE.create("metal");
	public static final RtToken RI_SHINYMETAL = TokenFactory.INSTANCE.create("shinymetal");
	public static final RtToken RI_PLASTIC = TokenFactory.INSTANCE.create("plastic");
	public static final RtToken RI_PAINTEDPLASTIC = TokenFactory.INSTANCE.create("paintedplastic");
	
	public static final RtToken RI_KA = TokenFactory.INSTANCE.create("ka");
	public static final RtToken RI_KD = TokenFactory.INSTANCE.create("kd");
	public static final RtToken RI_KS = TokenFactory.INSTANCE.create("ks");
	public static final RtToken RI_ROUGHNESS = TokenFactory.INSTANCE.create("roughness");
	public static final RtToken RI_KR = TokenFactory.INSTANCE.create("kr");
	public static final RtToken RI_TEXTURENAME = TokenFactory.INSTANCE.create("texturename");
	public static final RtToken RI_SPECULARCOLOR = TokenFactory.INSTANCE.create("specularcolor");
	
	public static final RtToken RI_DEPTHCUE = TokenFactory.INSTANCE.create("depthcue");
	public static final RtToken RI_FOG = TokenFactory.INSTANCE.create("fog");
	public static final RtToken RI_BUMPY = TokenFactory.INSTANCE.create("bumpy");
	
	public static final RtToken RI_MINDISTANCE = TokenFactory.INSTANCE.create("mindistance");
	public static final RtToken RI_MAXDISTANCE = TokenFactory.INSTANCE.create("maxdistance");
	public static final RtToken RI_BACKGROUND = TokenFactory.INSTANCE.create("background");
	public static final RtToken RI_DISTANCE = TokenFactory.INSTANCE.create("distance");
	public static final RtToken RI_AMPLITUDE = TokenFactory.INSTANCE.create("amplitude");
	
	public static final RtToken RI_RASTER = TokenFactory.INSTANCE.create("raster");
	public static final RtToken RI_SCREEN = TokenFactory.INSTANCE.create("screen");
	public static final RtToken RI_CAMERA = TokenFactory.INSTANCE.create("camera");
	public static final RtToken RI_WORLD = TokenFactory.INSTANCE.create("world");
	public static final RtToken RI_OBJECT = TokenFactory.INSTANCE.create("object");
	
	public static final RtToken RI_INSIDE = TokenFactory.INSTANCE.create("inside");
	public static final RtToken RI_OUTSIDE = TokenFactory.INSTANCE.create("outside");
	public static final RtToken RI_LH = TokenFactory.INSTANCE.create("lh");
	public static final RtToken RI_RH = TokenFactory.INSTANCE.create("rh");
	
	public static final RtToken RI_P = TokenFactory.INSTANCE.create("p");
	public static final RtToken RI_PZ = TokenFactory.INSTANCE.create("pz");
	public static final RtToken RI_PW = TokenFactory.INSTANCE.create("pw");
	public static final RtToken RI_N = TokenFactory.INSTANCE.create("n");
	public static final RtToken RI_NP = TokenFactory.INSTANCE.create("np");
	public static final RtToken RI_CS = TokenFactory.INSTANCE.create("cs");
	public static final RtToken RI_OS = TokenFactory.INSTANCE.create("os");
	public static final RtToken RI_S = TokenFactory.INSTANCE.create("s");
	public static final RtToken RI_T = TokenFactory.INSTANCE.create("t");
	public static final RtToken RI_ST = TokenFactory.INSTANCE.create("st");
	
	public static final RtToken RI_BILINEAR = TokenFactory.INSTANCE.create("bilinear");
	public static final RtToken RI_BICUBIC = TokenFactory.INSTANCE.create("bicubic");
	
	public static final RtToken RI_LINEAR = TokenFactory.INSTANCE.create("linear");
	public static final RtToken RI_CUBIC = TokenFactory.INSTANCE.create("cubic");
	
	public static final RtToken RI_PRIMITIVE = TokenFactory.INSTANCE.create("primitive");
	public static final RtToken RI_INTERSECTION = TokenFactory.INSTANCE.create("intersection");
	public static final RtToken RI_UNION = TokenFactory.INSTANCE.create("union");
	public static final RtToken RI_DIFFERENCE = TokenFactory.INSTANCE.create("difference");
	
	public static final RtToken RI_PERIODIC = TokenFactory.INSTANCE.create("periodic");
	public static final RtToken RI_NONPERIODIC = TokenFactory.INSTANCE.create("nonperiodic");
	public static final RtToken RI_CLAMP = TokenFactory.INSTANCE.create("clamp");
	public static final RtToken RI_BLACK = TokenFactory.INSTANCE.create("black");
	
	public static final RtToken RI_IGNORE = TokenFactory.INSTANCE.create("ignore");
	public static final RtToken RI_PRINT = TokenFactory.INSTANCE.create("print");
	public static final RtToken RI_ABORT = TokenFactory.INSTANCE.create("abort");
	public static final RtToken RI_HANDLER = TokenFactory.INSTANCE.create("handler");
	
	public static final RtToken RI_COMMENT = TokenFactory.INSTANCE.create("comment");
	public static final RtToken RI_STRUCTURE = TokenFactory.INSTANCE.create("structure");
	public static final RtToken RI_VERBATIM = TokenFactory.INSTANCE.create("verbatim");
	
	public static final RtToken RI_IDENTIFIER = TokenFactory.INSTANCE.create("identifier");
	public static final RtToken RI_NAME = TokenFactory.INSTANCE.create("name");
	public static final RtToken RI_SHADINGGROUP = TokenFactory.INSTANCE.create("shadinggroup");
	
	public static final RtToken RI_WIDTH = TokenFactory.INSTANCE.create("width");
	public static final RtToken RI_CONSTANTWIDTH = TokenFactory.INSTANCE.create("constantwidth");

	public static final RtBasis RiBezierBasis = new RtBasis() {};
	public static final RtBasis RiBSplineBasis = new RtBasis() {};
	public static final RtBasis RiCatmullRomBasis = new RtBasis() {};
	public static final RtBasis RiHermiteBasis = new RtBasis() {};
	public static final RtBasis RiPowerBasis = new RtBasis() {};
	
	public static final int RI_BEZIERSTEP = 3;
	public static final int RI_BSPLINESTEP = 1;
	public static final int RI_CATMULLROMSTEP = 1;
	public static final int RI_HERMITESTEP = 2;
	public static final int RI_POWERSTEP = 4;
	
	
	public static final RtFilterFunc RiBoxFilter = new RtFilterFunc() {
		public double apply(double x, double y, double xwidth, double ywidth) {
			return 1.0;
		}
	};
	public static final RtFilterFunc RiTriangleFilter = new RtFilterFunc() {
		public double apply(double x, double y, double xwidth, double ywidth) {
			return ((1.0 - Math.abs(x)) / (xwidth * 0.5))
				* ((1.0 - Math.abs(y)) / (ywidth * 0.5));
		}
	};
	public static final RtFilterFunc RiCatmullRomFilter = new RtFilterFunc() {
		public double apply(double x, double y, double xwidth, double ywidth) {
			double r2 = x * x + y * y;
			double r = Math.sqrt(r2);
			return (r >= 2.0) ? 0.0 :
				(r < 1.0) ? (3.0*r*r2 - 5.0*r2 + 2.0) : (-r*r2 + 5.0*r2 - 8.0*r + 4.0);
		}
	};
	public static final RtFilterFunc RiGaussianFilter = new RtFilterFunc() {
		public double apply(double x, double y, double xwidth, double ywidth) {
			x *= 2.0 / xwidth;
			y *= 2.0 / ywidth;
			return Math.exp(-2.0 * (x*x + y*y));
		}
	};
	public static final RtFilterFunc RiSincFilter = new RtFilterFunc() {
		public double apply(double x, double y, double xwidth, double ywidth) {
			double s, t;
			if (x > -0.001 && x < 0.001) {
				s = 1.0;
			} else {
				s = Math.sin(x) / x;
			}
			if (y > -0.001 && y < 0.001) {
				t = 1.0;
			} else {
				t = Math.sin(y) / y;
			}
			return s * t;
		}
	};

	public static final RtErrorHandler RiErrorIgnore = new RtErrorHandler() {
		public void apply(int code, int severity, String message) {
		}
	};
	
	public static final RtErrorHandler RiErrorPrint = new RtErrorHandler() {
		public void apply(int code, int severity, String message) {
			System.err.printf("ERROR(%d,%d): %s", code, severity, message);
			System.err.println();
		}
	};
	public static final RtErrorHandler RiErrorAbort = new RtErrorHandler() {
		public void apply(int code, int severity, String message) {
			System.err.printf("ERROR(%d,%d): %s", code, severity, message);
			System.err.println();
			System.exit(code);
		}
	};
	
	/*
	Error Codes
	1 - 10 System and File Errors
	11 - 20 Program Limitations
	21 - 40 State Errors
	41 - 60 Parameter and Protocol Errors
	61 - 80 Execution Errors
	*/
	public static final int RIE_NOERROR = 0;
	public static final int RIE_NOMEM = 1; /* Out of memory */
	public static final int RIE_SYSTEM = 2; /* Miscellaneous system error */
	public static final int RIE_NOFILE = 3; /* File nonexistent */
	public static final int RIE_BADFILE = 4; /* Bad file format */
	public static final int RIE_VERSION = 5; /* File version mismatch */
	public static final int RIE_DISKFULL = 6; /* Target disk is full */
	public static final int RIE_INCAPABLE = 11; /* Optional RI feature */
	public static final int RIE_UNIMPLEMENT = 12; /* Unimplemented feature */
	public static final int RIE_LIMIT = 13; /* Arbitrary program limit */
	public static final int RIE_BUG = 14; /* Probably a bug in renderer */
	public static final int RIE_NOTSTARTED = 23; /* RiBegin not called */
	public static final int RIE_NESTING = 24; /* Bad begin-end nesting */
	public static final int RIE_NOTOPTIONS = 25; /* Invalid state for options */
	public static final int RIE_NOTATTRIBS = 26; /* Invalid state for attribs */
	public static final int RIE_NOTPRIMS = 27; /* Invalid state for primitives */
	public static final int RIE_ILLSTATE = 28; /* Other invalid state */
	public static final int RIE_BADMOTION = 29; /* Badly formed motion block */
	public static final int RIE_BADSOLID = 30; /* Badly formed solid block */
	public static final int RIE_BADTOKEN = 41; /* Invalid token for request */
	public static final int RIE_RANGE = 42; /* Parameter out of range */
	public static final int RIE_CONSISTENCY = 43; /* Parameters inconsistent */
	public static final int RIE_BADHANDLE = 44; /* Bad object/light handle */
	public static final int RIE_NOSHADER = 45; /* Can’t load requested shader */
	public static final int RIE_MISSINGDATA = 46; /* Required parameters not provided */
	public static final int RIE_SYNTAX = 47; /* Declare type syntax error */
	public static final int RIE_MATH = 61; /* Zerodivide, noninvert matrix, etc. */
	/* Error severity levels */
	public static final int RIE_INFO = 0; /* Rendering stats and other info */
	public static final int RIE_WARNING = 1; /* Something seems wrong, maybe okay */
	public static final int RIE_ERROR = 2; /* Problem. Results may be wrong */
	public static final int RIE_SEVERE = 3; /* So bad you should probably abort */
	
	/* 4. Graphics State */

	RtToken declare(String name, String declaration);
	void frameBegin(int frame);
	void frameEnd();
	void worldBegin();
	void worldEnd();
	
	/* 4.1 Options */
	void format(int xresolution, int yresolution, double pixelaspectratio);
	void frameAspectRatio(double frameaspectratio);
	void screenWindow(double left, double right, double bottom, double top);
	void cropWindow(double xmin, double xmax, double ymin, double ymax);
	void projection(RtToken name, Map<RtToken, Object> params);
	void clipping(double near, double far);
	void depthOfField(double fstop, double focallength, double focaldistance);
	void shutter(double min, double max);
	void pixelVariance(double variation);
	void pixelSamples(double xsamples, double ysamples);
	void pixelFilter(RtFilterFunc filterfunc, double xwidth, double ywidth);
	void exposure(double gain, double gamma);
	void imager(RtToken name, Map<RtToken, Object> params);
	void quantize(RtToken type, int one, int min, int max, double ditheramplitude);
	void display(RtToken name, RtToken type, RtToken mode, Object... parmaterlist);
	void hider(RtToken type, Map<RtToken, Object> params);
	void colorSamples(int n, double[] nRGB, double[] RGBn);
	void relativeDetail(double relativedetail);
	void option(RtToken name, Map<RtToken, Object> params);
	
	/* 4.2 Attributes */
	void attributeBegin();
	void attributeEnd();
	void color(RtColor color);
	void opacity(RtColor color);
	void textureCoordinates(double s1, double t1, double s2, double t2, double s3, double t3, double s4, double t4);
	RtLightHandle lightSource(RtToken shadername, Map<RtToken, Object> params);
	RtLightHandle areaLightSource(RtToken shadername, Map<RtToken, Object> params);
	void illuminate(RtLightHandle light, boolean onoff);
	void surface(RtToken shadername, Map<RtToken, Object> params);
	void displacement(RtToken shadername, Map<RtToken, Object> params);
	void atmosphere(RtToken shadername, Map<RtToken, Object> params);
	void interior(RtToken shadername, Map<RtToken, Object> params);
	void exterior(RtToken shadername, Map<RtToken, Object> params);
	void shadingRate(double size);
	void shadingInterpolation(RtToken type);
	void matte(boolean onoff);
	void bound(RtBound bound);
	void detail(RtBound bound);
	void detailRange(double minvisible, double lowertransition, double uppertransition, double maxvisible);
	void geometricApproximation(RtToken type, double value);
	void orientation(RtToken orientation);
	void reverseOrientation();
	void sides(int sides);
	
	/* 4.3 Transformations */
	void identity();
	void transform(RtMatrix transform);
	void concatTransform(RtMatrix transform);
	void perspective(double fov);
	void translate(double dx, double dy, double dz);
	void scale(double sx, double sy, double sz);
	void skew(double angle, double dx1, double dy1, double dz1, double dx2, double dy2, double dz2);
	void coordinateSystem(RtToken name);
	void coordSysTransform(RtToken name);
	RtPoint[] transformPoints(RtToken fromspace, RtToken tospace, int n /* not necessary.. use points.length */, RtPoint[] points);
	void transformBegin();
	void transformEnd();
	
	/* 4.4 Implementation-specific Attributes */
	void attribute(RtToken name, Map<RtToken, Object> params);
	
	/* 5. Geometric Primitives */
	
	/* 5.1 Polygons */
	void polygon(int nvertices /* not necessary */, Map<RtToken, Object> params);
	void generalPolygon(int nloops, int[] nvertices, Map<RtToken, Object> params);
	void pointsPolygons(int npolys, int[] nvertices, int[] vertices, Map<RtToken, Object> params);
	void pointsGeneralPolygons(int npolys, int[] nloops, int[] nvertices, int[] vertices, Map<RtToken, Object> params);
	
	/* 5.2 Patches */
	void basis(RtBasis ubasis, int ustep, RtBasis vbasis, int vstep);
	void patch(RtToken type, Map<RtToken, Object> params);
	void patchMesh(RtToken type, int nu, RtToken uwrap, int nv, RtToken vwrap, Map<RtToken, Object> params);
	void nuPatch(int nu, int uorder, double[] uknot, double umin, double umax, int nv, int vorder, double[] vknot, double vmin, double vmax);
	void trimCurve(int nloops, int[] ncurves, int[] order, double[] knot, double min, double max, int[] n, double[] u, double[] v, double[] w);
	
	/* 5.3 Subdivision Surfaces */
	void subdivisionMesh(RtToken scheme, int nfaces, int[] nvertices, int[] vertices, int ntags, RtToken[] tags, int[] nargs, int[] intargs, double[] doubleargs, Map<RtToken, Object> params);
	
	/* 5.4 Quadrics */
	void sphere(double radius, double zmin, double zmax, double thetamax, Map<RtToken, Object> params);
	void cone(double height, double radius, double thetamax, Map<RtToken, Object> params);
	void cylinder(double radius, double zmin, double zmax, double thetamax, Map<RtToken, Object> params);
	void hyperboloid(RtPoint point1, RtPoint point2, double thetamax, Map<RtToken, Object> params);
	void paraboloid(double rmax, double zmin, double zmax, double thetamax, Map<RtToken, Object> params);
	void disk(double height, double radius, double thetamax, Map<RtToken, Object> params);
	void torus(double majorradius, double minorradius, double phimin, double phimax, double thetamax, Map<RtToken, Object> params);
	
	/* 5.5 Point and Curve Primitives */
	void points(int npoints, Map<RtToken, Object> params);
	void curves(RtToken type, int ncurves, int[] nvertices, RtToken wrap, Map<RtToken, Object> params);
	
	/* 5.6 Blobby Implicit Surfaces */
	void blobby(int nleaf, int ncode, int[] code, int ndoubles, double[] doubles, int nstrings, String[] strings, Map<RtToken, Object> params);
	
	/* 5.7 Procedural Primitives */
	void procedural(Object data, RtBound bound, RtProcSubdivFunc subdividefunc);
	void procRunProgram(Object data, double detail);
	void procDynamicLoad(Object data, double detail);
	
	/* 5.8 Implementation-specific Geometric Primitives */
	void geometry(RtToken type, Map<RtToken, Object> params);
	
	/* 5.9 Solids and Spatial Set Operations */
	void solidBegin(RtToken operation);
	void solidEnd();
	
	/* 5.10 Retained Geometry */
	RtObjectHandle objectBegin();
	void objectEnd();
	void objectInstance(RtObjectHandle handle);
	
	
	/* 6. Motion */
	void motionBegin(int n, double... t);
	void motionEnd();
	
	
	/* 7. External Resources */
	
	/* 7.1 Texture Map Utilities */
	void makeTexture(String picturename, String texturename, RtToken swrap, RtToken twrap, RtFilterFunc filterfunc, double swidth, double twidth, Map<RtToken, Object> params);
	void makeLatLongEnvironment(String picturename, String texturename, RtFilterFunc filterfunc, double swidth, double twidth, Map<RtToken, Object> params);
	void makeCubeFaceEnvironment(String px, String nx, String py, String ny, String pz, String nz, String texturename, double fov, RtFilterFunc filterfunc, double swidth, double twidth, Map<RtToken, Object> params);
	void makeShadow(String picturename, String texturename, Map<RtToken, Object> params);
	
	/* 7.2 Errors */
	void errorHandler(RtErrorHandler handler);
	
	/* 7.3 Archive Files */
	void archiveRecord(RtToken type, String format, String... arg);
	void readArchive(RtToken name, RtArchiveCallback callback, Map<RtToken, Object> params);
	
	
}
