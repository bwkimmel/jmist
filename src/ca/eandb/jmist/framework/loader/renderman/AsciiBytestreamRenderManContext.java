/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Matrix4;
import ca.eandb.jmist.math.Point3;

/**
 * @author brad
 *
 */
public final class AsciiBytestreamRenderManContext implements RenderManContext {
	
	private static class Declaration implements RtToken {
		public final String name;
		public final String declaration;
		
		public Declaration(String name, String declaration) {
			this.name = name;
			this.declaration = declaration;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return name;
		}
	};
	
	private int nextLightSourceSequenceNumber = 0;
	
	private synchronized int generateLightSourceSequenceNumber() {
		return ++nextLightSourceSequenceNumber;
	}
	
	private class LightSource implements RtLightHandle {
		public final int sequenceNumber;
		public LightSource() {
			this.sequenceNumber = generateLightSourceSequenceNumber();
		}
	}
	
	private int nextObjectSequenceNumber = 0;
	
	private synchronized int generateObjectSequenceNumber() {
		return ++nextObjectSequenceNumber;
	}
	
	private class ObjectHandle implements RtObjectHandle {
		public final int sequenceNumber;
		public ObjectHandle() {
			this.sequenceNumber = generateObjectSequenceNumber();
		}
	}
	
	private final PrintStream out;
	
	private RtErrorHandler handler = RenderManContext.RiErrorAbort;
	
	public AsciiBytestreamRenderManContext(OutputStream out) {
		if (out instanceof PrintStream) {
			this.out = (PrintStream) out;
		} else {
			this.out = new PrintStream(out);
		}
	}
	
	public AsciiBytestreamRenderManContext(File file) throws FileNotFoundException {
		this(new PrintStream(file));
	}

	/**
	 * Print the parameter list.
	 * @param params The parameter list.
	 */
	private void print(Map<RtToken, RtValue> params) {
		for (Map.Entry<RtToken, RtValue> e : params.entrySet()) {
			out.printf(" \"%s\" %s", e.getKey(), e.getValue());
		}
	}
	
	private void print(int[] a) {
		out.print(" [");
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				out.print(" ");
			}
			out.print(a[i]);
		}
		out.print("]");
	}
	
	private void print(double[] a) {
		out.print(" [");
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				out.print(" ");
			}
			out.print(a[i]);
		}
		out.print("]");
	}
	
	private void print(String[] a) {
		out.print(" [");
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				out.print(" ");
			}
			out.printf("\"%s\"", a[i]);
		}
		out.print("]");
	}

	/**
	 * @param m
	 */
	private void print(Matrix4 m) {
		out.print(" [");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i > 0 || j > 0) {
					out.print(" ");
				}
				out.print(m.at(i, j));
			}
		}
		out.print("]");
	}
	
	private void print(RtColor color) {
		out.print(" [");
		for (int i = 0, n = color.size(); i < n; i++) {
			if (i > 0) {
				out.print(" ");
			}
			out.print(color.get(i));
		}
		out.print("]");
	}
	
	private String getName(RtBasis basis) {
		if (basis == RenderManContext.RiBezierBasis) {
			return "bezier";
		} else if (basis == RenderManContext.RiBSplineBasis) {
			return "b-spline";
		} else if (basis == RenderManContext.RiCatmullRomBasis) {
			return "catmull-rom";
		} else if (basis == RenderManContext.RiHermiteBasis) {
			return "hermite";
		} else if (basis == RenderManContext.RiPowerBasis) {
			return "power";
		} else {
			return null;
		}
	}
	
	private void print(RtBasis basis) {
		String name = getName(basis);
		if (name != null) {
			out.printf(" \"%s\"", name);
		} else {
			print(basis.toMatrix());
		}
	}
	
	private String getName(RtFilterFunc f) {
		if (f == RenderManContext.RiBoxFilter) {
			return "box";
		} else if (f == RenderManContext.RiCatmullRomFilter) {
			return "catmull-rom";
		} else if (f == RenderManContext.RiGaussianFilter) {
			return "gaussian";
		} else if (f == RenderManContext.RiSincFilter) {
			return "sinc";
		} else if (f == RenderManContext.RiTriangleFilter) {
			return "triangle";
		} else {
			return null;
		}
	}
	
	private void print(RtFilterFunc f) {
		String name = getName(f);
		if (name != null) {
			out.printf(" \"%s\"", name);
		} else {
			throw new IllegalArgumentException("Unknown filter function");
		}
	}

	/**
	 * @param tokens
	 */
	private void print(RtToken[] tokens) {
		out.print(" [");
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				out.print(" ");
			}
			out.printf("\"%s\"", tokens[i]);
		}
		out.print("]");
	}

	/**
	 * @param point
	 */
	private void print(RtPoint point) {
		Point3 p = point.toPoint();
		out.printf(" %f %f %f", p.x(), p.y(), p.z());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#declare(java.lang.String, java.lang.String)
	 */
	@Override
	public RtToken declare(String name, String declaration) {
		out.printf("Declare \"%s\" \"%s\"", name, declaration);
		out.println();
		return new Declaration(name, declaration);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameBegin(int)
	 */
	@Override
	public void frameBegin(int frame) {
		out.printf("FrameBegin %d", frame);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameEnd()
	 */
	@Override
	public void frameEnd() {
		out.println("FrameEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldBegin()
	 */
	@Override
	public void worldBegin() {
		out.println("WorldBegin");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#worldEnd()
	 */
	@Override
	public void worldEnd() {
		out.println("WorldEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#format(int, int, double)
	 */
	@Override
	public void format(int xresolution, int yresolution, double pixelaspectratio) {
		out.printf("Format %d %d %f", xresolution, yresolution, pixelaspectratio);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#frameAspectRatio(double)
	 */
	@Override
	public void frameAspectRatio(double frameaspectratio) {
		out.printf("FrameAspectRatio %f", frameaspectratio);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#screenWindow(double, double, double, double)
	 */
	@Override
	public void screenWindow(double left, double right, double bottom,
			double top) {
		out.printf("ScreenWindow %f %f %f %f", left, right, bottom, top);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cropWindow(double, double, double, double)
	 */
	@Override
	public void cropWindow(double xmin, double xmax, double ymin, double ymax) {
		out.printf("CropWindow %f %f %f %f", xmin, xmax, ymin, ymax);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#projection(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void projection(RtToken name, Map<RtToken, RtValue> params) {
		out.printf("Projection \"%s\"", name);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#clipping(double, double)
	 */
	@Override
	public void clipping(double near, double far) {
		out.printf("Clipping %f %f", near, far);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#clippingPlane(double, double, double, double, double, double)
	 */
	@Override
	public void clippingPlane(double x, double y, double z, double nx,
			double ny, double nz) {
		out.printf("ClippingPlane %f %f %f %f %f %f", x, y, z, nx, ny, nz);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#depthOfField(double, double, double)
	 */
	@Override
	public void depthOfField(double fstop, double focallength,
			double focaldistance) {
		out.printf("DepthOfField %f %f %f", fstop, focallength, focaldistance);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shutter(double, double)
	 */
	@Override
	public void shutter(double min, double max) {
		out.printf("Shutter %f %f", min, max);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelVariance(double)
	 */
	@Override
	public void pixelVariance(double variation) {
		out.printf("PixelVariance %f", variation);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelSamples(double, double)
	 */
	@Override
	public void pixelSamples(double xsamples, double ysamples) {
		out.printf("PixelSamples %f %f", xsamples, ysamples);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pixelFilter(ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double)
	 */
	@Override
	public void pixelFilter(RtFilterFunc filterfunc, double xwidth,
			double ywidth) {
		out.print("PixelFilter");
		print(filterfunc);
		out.printf(" %f %f", xwidth, ywidth);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exposure(double, double)
	 */
	@Override
	public void exposure(double gain, double gamma) {
		out.printf("Exposure %f %f", gain, gamma);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#imager(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void imager(RtToken name, Map<RtToken, RtValue> params) {
		out.printf("Imager \"%s\"");
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#quantize(ca.eandb.jmist.framework.loader.renderman.RtToken, int, int, int, double)
	 */
	@Override
	public void quantize(RtToken type, int one, int min, int max,
			double ditheramplitude) {
		out.printf("Quantize \"%s\" %d %d %d %f", type, one, min, max, ditheramplitude);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#display(java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void display(String name, RtToken type, RtToken mode,
			Map<RtToken, RtValue> params) {
		out.printf("Display \"%s\" \"%s\" \"%s\"", name, type, mode);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hider(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void hider(RtToken type, Map<RtToken, RtValue> params) {
		out.printf("Hider \"%s\"", type);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#colorSamples(int, double[], double[])
	 */
	@Override
	public void colorSamples(int n, double[] nRGB, double[] RGBn) {
		out.printf("ColorSamples");
		print(nRGB);
		print(RGBn);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#relativeDetail(double)
	 */
	@Override
	public void relativeDetail(double relativedetail) {
		out.printf("RelativeDetail %f", relativedetail);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#option(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void option(RtToken name, Map<RtToken, RtValue> params) {
		out.printf("Option \"%s\"", name);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeBegin()
	 */
	@Override
	public void attributeBegin() {
		out.println("AttributeBegin");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#attributeEnd()
	 */
	@Override
	public void attributeEnd() {
		out.println("AttributeEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#color(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void color(RtColor color) {
		out.print("Color");
		print(color);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#opacity(ca.eandb.jmist.framework.loader.renderman.RtColor)
	 */
	@Override
	public void opacity(RtColor color) {
		out.print("Opacity");
		print(color);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#textureCoordinates(double, double, double, double, double, double, double, double)
	 */
	@Override
	public void textureCoordinates(double s1, double t1, double s2, double t2,
			double s3, double t3, double s4, double t4) {
		out.printf("TextureCoordinates %f %f %f %f %f %f %f %f", s1, t1, s2, t2, s3, t3, s4, t4);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#lightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle lightSource(RtToken shadername,
			Map<RtToken, RtValue> params) {
		LightSource light = new LightSource();
		out.printf("LightSource \"%s\" %d", shadername, light.sequenceNumber);
		print(params);
		out.println();
		return light;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#areaLightSource(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public RtLightHandle areaLightSource(RtToken shadername,
			Map<RtToken, RtValue> params) {
		LightSource ls = new LightSource();
		out.printf("AreaLightSource \"%s\" %d", shadername, ls.sequenceNumber);
		print(params);
		out.println();
		return ls;		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#illuminate(ca.eandb.jmist.framework.loader.renderman.RtLightHandle, boolean)
	 */
	@Override
	public void illuminate(RtLightHandle light, boolean onoff) {
		LightSource ls = (LightSource) light;
		out.printf("Illuminate %d %d", ls.sequenceNumber, onoff ? 1 : 0);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#surface(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void surface(RtToken shadername, Map<RtToken, RtValue> params) {
		out.printf("Surface \"%s\"", shadername);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#displacement(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void displacement(RtToken shadername, Map<RtToken, RtValue> params) {
		out.printf("Displacement \"%s\"", shadername);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#atmosphere(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void atmosphere(RtToken shadername, Map<RtToken, RtValue> params) {
		out.printf("Atmosphere \"%s\"", shadername);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#interior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void interior(RtToken shadername, Map<RtToken, RtValue> params) {
		out.printf("Interior \"%s\"", shadername);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#exterior(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void exterior(RtToken shadername, Map<RtToken, RtValue> params) {
		out.printf("Exterior \"%s\"", shadername);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingRate(double)
	 */
	@Override
	public void shadingRate(double size) {
		out.printf("ShadingRate %f", size);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#shadingInterpolation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void shadingInterpolation(RtToken type) {
		out.printf("ShadingInterpolation \"%s\"", type);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#matte(boolean)
	 */
	@Override
	public void matte(boolean onoff) {
		out.printf("Matte %d", onoff ? 1 : 0);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#bound(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void bound(RtBound bound) {
		Box3 box = bound.toBox();
		out.printf("Bound %f %f %f %f %f %f", box.minimumX(), box.maximumX(), box.minimumY(), box.maximumY(), box.minimumZ(), box.maximumZ());
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detail(ca.eandb.jmist.framework.loader.renderman.RtBound)
	 */
	@Override
	public void detail(RtBound bound) {
		Box3 box = bound.toBox();
		out.printf("Detail %f %f %f %f %f %f", box.minimumX(), box.maximumX(), box.minimumY(), box.maximumY(), box.minimumZ(), box.maximumZ());
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#detailRange(double, double, double, double)
	 */
	@Override
	public void detailRange(double minvisible, double lowertransition,
			double uppertransition, double maxvisible) {
		out.printf("DetailRange %f %f %f %f", minvisible, lowertransition, uppertransition, maxvisible);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometricApproximation(ca.eandb.jmist.framework.loader.renderman.RtToken, double)
	 */
	@Override
	public void geometricApproximation(RtToken type, double value) {
		out.printf("GeometricApproximation \"%s\" %f", type, value);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#orientation(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void orientation(RtToken orientation) {
		out.printf("Orientation \"%s\"", orientation);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#reverseOrientation()
	 */
	@Override
	public void reverseOrientation() {
		out.println("ReverseOrientation");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sides(int)
	 */
	@Override
	public void sides(int sides) {
		out.printf("Sides %d", sides);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#identity()
	 */
	@Override
	public void identity() {
		out.println("Identity");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void transform(RtMatrix transform) {
		Matrix4 m = transform.toMatrix();
		out.print("Transform");
		print(m);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#concatTransform(ca.eandb.jmist.framework.loader.renderman.RtMatrix)
	 */
	@Override
	public void concatTransform(RtMatrix transform) {
		Matrix4 m = transform.toMatrix();
		out.print("ConcatTransform");
		print(m);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#perspective(double)
	 */
	@Override
	public void perspective(double fov) {
		out.printf("Perspective %f", fov);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#translate(double, double, double)
	 */
	@Override
	public void translate(double dx, double dy, double dz) {
		out.printf("Translate %f %f %f", dx, dy, dz);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#rotate(double, double, double, double)
	 */
	@Override
	public void rotate(double angle, double dx, double dy, double dz) {
		out.printf("Rotate %f %f %f %f", angle, dx, dy, dz);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#scale(double, double, double)
	 */
	@Override
	public void scale(double sx, double sy, double sz) {
		out.printf("Scale %f %f %f", sx, sy, sz);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#skew(double, double, double, double, double, double, double)
	 */
	@Override
	public void skew(double angle, double dx1, double dy1, double dz1,
			double dx2, double dy2, double dz2) {
		out.printf("Skew %f %f %f %f %f %f %f", angle, dx1, dy1, dz1, dx2, dy2, dz2);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordinateSystem(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordinateSystem(RtToken name) {
		out.printf("CoordinateSystem \"%s\"", name);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#coordSysTransform(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void coordSysTransform(RtToken name) {
		out.printf("CoordinateSysTransform \"%s\"", name);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformPoints(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtPoint[])
	 */
	@Override
	public RtPoint[] transformPoints(RtToken fromspace, RtToken tospace,
			RtPoint[] points) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformBegin()
	 */
	@Override
	public void transformBegin() {
		out.println("TransformBegin");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#transformEnd()
	 */
	@Override
	public void transformEnd() {
		out.println("TransformEnd");
	}

	@Override
	public void attribute(RtToken name, Map<RtToken, RtValue> params) {
		out.printf("Attribute \"%s\"", name);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#polygon(java.util.Map)
	 */
	@Override
	public void polygon(Map<RtToken, RtValue> params) {
		out.print("Polygon");
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#generalPolygon(int[], java.util.Map)
	 */
	@Override
	public void generalPolygon(int[] nvertices,
			Map<RtToken, RtValue> params) {
		out.print("GeneralPolygon");
		print(nvertices);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsPolygons(int[], int[], java.util.Map)
	 */
	@Override
	public void pointsPolygons(int[] nvertices, int[] vertices,
			Map<RtToken, RtValue> params) {
		out.print("PointsPolygon");
		print(nvertices);
		print(vertices);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#pointsGeneralPolygons(int[], int[], int[], java.util.Map)
	 */
	@Override
	public void pointsGeneralPolygons(int[] nloops,
			int[] nvertices, int[] vertices, Map<RtToken, RtValue> params) {
		out.print("PointsGeneralPolygon");
		print(nloops);
		print(nvertices);
		print(vertices);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#basis(ca.eandb.jmist.framework.loader.renderman.RtBasis, int, ca.eandb.jmist.framework.loader.renderman.RtBasis, int)
	 */
	@Override
	public void basis(RtBasis ubasis, int ustep, RtBasis vbasis, int vstep) {
		out.print("Basis");
		print(ubasis);
		out.print(" ");
		out.print(ustep);
		print(vbasis);
		out.print(" ");
		out.print(vstep);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patch(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patch(RtToken type, Map<RtToken, RtValue> params) {
		out.printf("Patch \"%s\"", type);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#patchMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, int, ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void patchMesh(RtToken type, int nu, RtToken uwrap, int nv,
			RtToken vwrap, Map<RtToken, RtValue> params) {
		out.printf("PatchMesh \"%s\" %d \"%s\" %d \"%s\"", type, nu, uwrap, nv, vwrap);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#nuPatch(int, int, double[], double, double, int, int, double[], double, double)
	 */
	@Override
	public void nuPatch(int nu, int uorder, double[] uknot, double umin,
			double umax, int nv, int vorder, double[] vknot, double vmin,
			double vmax) {
		out.print("NuPatch");
		out.printf(" %d %d", nu, uorder);
		print(uknot);
		out.printf(" %f %f", umin, umax);
		out.printf(" %d %d", nv, vorder);
		print(vknot);
		out.printf(" %f %f", vmin, vmax);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#trimCurve(int[], int[], double[], double, double, int[], double[], double[], double[])
	 */
	@Override
	public void trimCurve(int[] ncurves, int[] order,
			double[] knot, double min, double max, int[] n, double[] u,
			double[] v, double[] w) {
		out.print("TrimCurve");
		print(ncurves);
		print(order);
		print(knot);
		out.printf(" %f %f", min, max);
		print(n);
		print(u);
		print(v);
		print(w);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#subdivisionMesh(ca.eandb.jmist.framework.loader.renderman.RtToken, int[], int[], ca.eandb.jmist.framework.loader.renderman.RtToken[], int[], int[], double[], java.util.Map)
	 */
	@Override
	public void subdivisionMesh(RtToken scheme, int[] nvertices,
			int[] vertices, RtToken[] tags, int[] nargs,
			int[] intargs, double[] doubleargs, Map<RtToken, RtValue> params) {
		out.printf("SubdivisionMesh \"%s\"", scheme);
		print(nvertices);
		print(vertices);
		print(tags);
		print(nargs);
		print(intargs);
		print(doubleargs);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#sphere(double, double, double, double, java.util.Map)
	 */
	@Override
	public void sphere(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, RtValue> params) {
		out.printf("Sphere %f %f %f %f", radius, zmin, zmax, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cone(double, double, double, java.util.Map)
	 */
	@Override
	public void cone(double height, double radius, double thetamax,
			Map<RtToken, RtValue> params) {
		out.printf("Cone %f %f %f", height, radius, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#cylinder(double, double, double, double, java.util.Map)
	 */
	@Override
	public void cylinder(double radius, double zmin, double zmax,
			double thetamax, Map<RtToken, RtValue> params) {
		out.printf("Cylinder %f %f %f %f", radius, zmin, zmax, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#hyperboloid(ca.eandb.jmist.framework.loader.renderman.RtPoint, ca.eandb.jmist.framework.loader.renderman.RtPoint, double, java.util.Map)
	 */
	@Override
	public void hyperboloid(RtPoint point1, RtPoint point2, double thetamax,
			Map<RtToken, RtValue> params) {
		out.print("Hyperboloid");
		print(point1);
		print(point2);
		out.print(" ");
		out.print(thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#paraboloid(double, double, double, double, java.util.Map)
	 */
	@Override
	public void paraboloid(double rmax, double zmin, double zmax,
			double thetamax, Map<RtToken, RtValue> params) {
		out.printf("Paraboloid %f %f %f %f", rmax, zmin, zmax, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#disk(double, double, double, java.util.Map)
	 */
	@Override
	public void disk(double height, double radius, double thetamax,
			Map<RtToken, RtValue> params) {
		out.printf("Disk %f %f %f", height, radius, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#torus(double, double, double, double, double, java.util.Map)
	 */
	@Override
	public void torus(double majorradius, double minorradius, double phimin,
			double phimax, double thetamax, Map<RtToken, RtValue> params) {
		out.printf("Torus %f %f %f %f %f", majorradius, minorradius, phimin, phimax, thetamax);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#points(java.util.Map)
	 */
	@Override
	public void points(Map<RtToken, RtValue> params) {
		out.print("Points");
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#curves(ca.eandb.jmist.framework.loader.renderman.RtToken, int[], ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void curves(RtToken type, int[] nvertices,
			RtToken wrap, Map<RtToken, RtValue> params) {
		out.printf("Curves \"%s\"", type);
		print(nvertices);
		out.printf(" \"%s\"", wrap);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#blobby(int, int, int[], int, double[], int, java.lang.String[], java.util.Map)
	 */
	@Override
	public void blobby(int nleaf, int[] code, double[] doubles,
			String[] strings, Map<RtToken, RtValue> params) {
		out.printf("Blobby %d", nleaf);
		print(code);
		print(doubles);
		print(strings);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procedural(java.lang.Object, ca.eandb.jmist.framework.loader.renderman.RtBound, ca.eandb.jmist.framework.loader.renderman.RtProcSubdivFunc)
	 */
	@Override
	public void procedural(Object data, RtBound bound,
			RtProcSubdivFunc subdividefunc) {
		// TODO implement this
		handler.apply(RtErrorType.UNIMPLEMENT, RtErrorSeverity.WARNING, "Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procRunProgram(java.lang.Object, double)
	 */
	@Override
	public void procRunProgram(Object data, double detail) {
		// TODO implement this
		handler.apply(RtErrorType.UNIMPLEMENT, RtErrorSeverity.WARNING, "Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#procDynamicLoad(java.lang.Object, double)
	 */
	@Override
	public void procDynamicLoad(Object data, double detail) {
		// TODO implement this
		handler.apply(RtErrorType.UNIMPLEMENT, RtErrorSeverity.WARNING, "Not yet implemented");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#geometry(ca.eandb.jmist.framework.loader.renderman.RtToken, java.util.Map)
	 */
	@Override
	public void geometry(RtToken type, Map<RtToken, RtValue> params) {
		out.printf("Geometry \"%s\"", type);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidBegin(ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public void solidBegin(RtToken operation) {
		out.printf("SolidBegin \"%s\"", operation);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#solidEnd()
	 */
	@Override
	public void solidEnd() {
		out.println("SolidEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectBegin()
	 */
	@Override
	public RtObjectHandle objectBegin() {
		ObjectHandle obj = new ObjectHandle();
		out.printf("ObjectBegin %d", obj.sequenceNumber);
		out.println();
		return obj;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectEnd()
	 */
	@Override
	public void objectEnd() {
		out.println("ObjectEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#objectInstance(ca.eandb.jmist.framework.loader.renderman.RtObjectHandle)
	 */
	@Override
	public void objectInstance(RtObjectHandle handle) {
		ObjectHandle obj = (ObjectHandle) handle;
		out.printf("ObjectInstance %d", obj.sequenceNumber);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionBegin(double[])
	 */
	@Override
	public void motionBegin(double... t) {
		out.print("MotionBegin");
		print(t);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#motionEnd()
	 */
	@Override
	public void motionEnd() {
		out.println("MotionEnd");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeTexture(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeTexture(String picturename, String texturename,
			RtToken swrap, RtToken twrap, RtFilterFunc filterfunc,
			double swidth, double twidth, Map<RtToken, RtValue> params) {
		out.printf("MakeTexture \"%s\" \"%s\" \"%s\" \"%s\"",
				picturename, texturename, swrap, twrap);
		print(filterfunc);
		out.printf(" %f %f", swidth, twidth);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeLatLongEnvironment(java.lang.String, java.lang.String, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeLatLongEnvironment(String picturename, String texturename,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, RtValue> params) {
		out.printf("MakeLatLongEnvironment \"%s\" \"%s\"",
				picturename, texturename);
		print(filterfunc);
		out.printf(" %f %f", swidth, twidth);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeCubeFaceEnvironment(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, double, ca.eandb.jmist.framework.loader.renderman.RtFilterFunc, double, double, java.util.Map)
	 */
	@Override
	public void makeCubeFaceEnvironment(String px, String nx, String py,
			String ny, String pz, String nz, String texturename, double fov,
			RtFilterFunc filterfunc, double swidth, double twidth,
			Map<RtToken, RtValue> params) {
		out.printf("MakeCubeFaceEnvironment \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" %f",
				px, nx, py, ny, pz, nz, texturename, fov);
		print(filterfunc);
		out.printf(" %f %f", swidth, twidth);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#makeShadow(java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void makeShadow(String picturename, String texturename,
			Map<RtToken, RtValue> params) {
		out.printf("MakeShadow \"%s\" \"%s\"", picturename, texturename);
		print(params);
		out.println();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#errorHandler(ca.eandb.jmist.framework.loader.renderman.RtErrorHandler)
	 */
	@Override
	public void errorHandler(RtErrorHandler handler) {
		this.handler = handler;
		if (handler == RenderManContext.RiErrorAbort) {
			out.println("ErrorHandler \"abort\"");
		} else if (handler == RenderManContext.RiErrorIgnore) {
			out.println("ErrorHandler \"ignore\"");
		} else if (handler == RenderManContext.RiErrorPrint) {
			out.println("ErrorHandler \"print\"");
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#archiveRecord(ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.String, java.lang.String[])
	 */
	@Override
	public void archiveRecord(RtToken type, String format, Object... args) {
		String message = String.format(format, args);
		if (type == RenderManContext.RI_COMMENT) {
			out.print("#");
			out.print(message);
			out.println();
		} else if (type == RenderManContext.RI_STRUCTURE) {
			out.print("##");
			out.print(message);
			out.println();
		} else if (type == RenderManContext.RI_VERBATIM) {
			out.print(message);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RenderManContext#readArchive(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtArchiveCallback, java.util.Map)
	 */
	@Override
	public void readArchive(RtToken name, RtArchiveCallback callback,
			Map<RtToken, RtValue> params) {
		out.printf("ReadArchive \"%s\"", name);
		out.println();
	}

}
