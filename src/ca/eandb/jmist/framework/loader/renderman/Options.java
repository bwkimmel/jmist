/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.Set;

import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad
 *
 */
final class Options implements State {
	
	private static final RtToken INTERNAL = new InternalToken("option");

	/* Camera Options (Table 4.1) */
	private static final RtToken HORIZONTAL_RESOLUTION = new InternalToken("horizontal_resolution");
	private static final RtToken VERTICAL_RESOLUTION = new InternalToken("vertical_resolution");
	private static final RtToken PIXEL_ASPECT_RATIO = new InternalToken("pixel_aspect_ratio");
	private static final RtToken CROP_WINDOW = new InternalToken("crop_window");
	private static final RtToken FRAME_ASPECT_RATIO = new InternalToken("frame_aspect_ratio");
	private static final RtToken SCREEN_WINDOW = new InternalToken("screen_window");
	private static final RtToken CAMERA_PROJECTION = new InternalToken("camera_projection");
	private static final RtToken WORLD_TO_CAMERA = new InternalToken("world_to_camera");
	private static final RtToken CLIPPING_INTERVAL = new InternalToken("clipping_interval");
	private static final RtToken CLIPPING_PLANES = new InternalToken("clipping_planes");
	private static final RtToken FSTOP = new InternalToken("f_stop");
	private static final RtToken FOCAL_LENGTH = new InternalToken("focal_length");
	private static final RtToken FOCAL_DISTANCE = new InternalToken("focal_distance");
	private static final RtToken SHUTTER = new InternalToken("shutter");
	
//	private int horizontalResolution = 640;
//	private int verticalResolution = 480;
//	private double pixelAspectRatio = 1.0;
//	private Box2 cropWindow = Box2.UNIT;
//	private double frameAspectRatio = 4.0 / 3.0;
//	private Box2 screenWindow = new Box2(-frameAspectRatio, -1.0, frameAspectRatio, 1.0);
//	private RtToken cameraProjection = RenderManContext.RI_ORTHOGRAPHIC;
//	private RtMatrix worldToCamera = null; /* identity */
//	private Interval clippingInterval = Interval.greaterThan(MathUtil.EPSILON);
//	private final Set<Plane3> clippingPlanes = Collections.emptySet();
//	private double fStop = Double.POSITIVE_INFINITY;
//	private double focalLength = Double.NaN;
//	private double focalDistance = Double.NaN;
//	private Interval shutter = new Interval(0, 0);
	
	/* Display Options (Table 4.3) */
	private static final RtToken PIXEL_VARIANCE = new InternalToken("pixel_variance");
	private static final RtToken HORIZONTAL_SAMPLING_RATE = new InternalToken("horizontal_sampling_rate");
	private static final RtToken VERTICAL_SAMPLING_RATE = new InternalToken("vertical_sampling_rate");
	private static final RtToken PIXEL_FILTER = new InternalToken("pixel_filter");
	private static final RtToken HORIZONTAL_FILTER_WIDTH = new InternalToken("horizontal_filter_width");
	private static final RtToken VERTICAL_FILTER_WIDTH = new InternalToken("vertical_filter_width");
	private static final RtToken EXPOSURE_GAIN = new InternalToken("exposure_gain");
	private static final RtToken EXPOSURE_GAMMA = new InternalToken("exposure_gamma");
	private static final RtToken IMAGER = new InternalToken("imager");
	private static final RtToken COLOR_QUANTIZER_ONE = new InternalToken("color_quantizer_one");
	private static final RtToken COLOR_QUANTIZER_MINIMUM = new InternalToken("color_quantizer_minimum");
	private static final RtToken COLOR_QUANTIZER_MAXIMUM = new InternalToken("color_quantizer_maximum");
	private static final RtToken COLOR_QUANTIZER_DITHER_AMPLITUDE = new InternalToken("color_quantizer_dither_amplitude");
	private static final RtToken DEPTH_QUANTIZER_ONE = new InternalToken("depth_quantizer_one");
	private static final RtToken DEPTH_QUANTIZER_MINIMUM = new InternalToken("depth_quantizer_minimum");
	private static final RtToken DEPTH_QUANTIZER_MAXIMUM = new InternalToken("depth_quantizer_maximum");
	private static final RtToken DEPTH_QUANTIZER_DITHER_AMPLITUDE = new InternalToken("depth_quantizer_dither_amplitude");
	private static final RtToken DISPLAY_TYPE = new InternalToken("display_type");
	private static final RtToken DISPLAY_NAME = new InternalToken("display_name");
	private static final RtToken DISPLAY_MODE = new InternalToken("display_mode");
	
//	private double pixelVariance;
//	private double horizontalSamplingRate;
//	private double verticalSamplingRate;
//	
//	private RtFilterFunc pixelFilter = RenderManContext.RiGaussianFilter;
//	private double horizontalFilterWidth;
//	private double verticalFilterWidth;
//	
//	private double exposureGain;
//	private double exposureGamma;
//	
//	/* TODO imager */
//	
//	private int colorQuantizerOne = 255;
//	private int colorQuantizerMinimum = 0;
//	private int colorQuantizerMaximum = 255;
//	private double colorQuantizerDitherAmplitude = 0.5;
//	
//	private int depthQuantizerOne = 0;
//	private int depthQuantizerMinimum = 0;
//	private int depthQuantizerMaximum = 0;
//	private double depthQuantizerDitherAmplitude = 0.0;
//	
//	private RtToken displayType;
//	private String displayName;
//	private RtToken displayMode = RenderManContext.RI_FRAMEBUFFER;

	private static final RtToken HIDER = new InternalToken("hider");
	private static final RtToken COLOR_SAMPLES = new InternalToken("color_samples");
	private static final RtToken RELATIVE_DETAIL = new InternalToken("relative_detail");
//	
//	/* Additional Options (4.4) */
//	private RtToken hider = RenderManContext.RI_HIDDEN;
//	private int colorSamples = 3;
//	private double relativeDetail = 1.0;
	
	private final State scope;
	
	public Options() {
		this.scope = new ScopedState();
	}
	
	private Options(Options outer) {
		this.scope = outer.scope.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Options clone() {
		return new Options(this);
	}

	
	/* Camera Options (Table 4.1) */

	/**
	 * @return the horizontalResolution
	 */
	public int getHorizontalResolution() {
		return (Integer) scope.get(INTERNAL, HORIZONTAL_RESOLUTION);
	}


	/**
	 * @param horizontalResolution the horizontalResolution to set
	 */
	public void setHorizontalResolution(int horizontalResolution) {
		scope.set(INTERNAL, HORIZONTAL_RESOLUTION, horizontalResolution);
	}


	/**
	 * @return the verticalResolution
	 */
	public int getVerticalResolution() {
		return (Integer) scope.get(INTERNAL, VERTICAL_RESOLUTION);
	}


	/**
	 * @param verticalResolution the verticalResolution to set
	 */
	public void setVerticalResolution(int verticalResolution) {
		scope.set(INTERNAL, VERTICAL_RESOLUTION, verticalResolution);
	}


	/**
	 * @return the pixelAspectRatio
	 */
	public double getPixelAspectRatio() {
		return (Double) scope.get(INTERNAL, PIXEL_ASPECT_RATIO);
	}


	/**
	 * @param pixelAspectRatio the pixelAspectRatio to set
	 */
	public void setPixelAspectRatio(double pixelAspectRatio) {
		scope.set(INTERNAL, PIXEL_ASPECT_RATIO, pixelAspectRatio);
	}


	/**
	 * @return the cropWindow
	 */
	public Box2 getCropWindow() {
		return (Box2) scope.get(INTERNAL, CROP_WINDOW);
	}


	/**
	 * @param cropWindow the cropWindow to set
	 */
	public void setCropWindow(Box2 cropWindow) {
		scope.set(INTERNAL, CROP_WINDOW, cropWindow);
	}


	/**
	 * @return the frameAspectRatio
	 */
	public double getFrameAspectRatio() {
		return (Double) scope.get(INTERNAL, FRAME_ASPECT_RATIO);
	}


	/**
	 * @param frameAspectRatio the frameAspectRatio to set
	 */
	public void setFrameAspectRatio(double frameAspectRatio) {
		scope.set(INTERNAL, FRAME_ASPECT_RATIO, frameAspectRatio);
	}


	/**
	 * @return the screenWindow
	 */
	public Box2 getScreenWindow() {
		return (Box2) scope.get(INTERNAL, SCREEN_WINDOW);
	}


	/**
	 * @param screenWindow the screenWindow to set
	 */
	public void setScreenWindow(Box2 screenWindow) {
		scope.set(INTERNAL, SCREEN_WINDOW, screenWindow);
	}


	/**
	 * @return the cameraProjection
	 */
	public RtToken getCameraProjection() {
		return (RtToken) scope.get(INTERNAL, CAMERA_PROJECTION);
	}


	/**
	 * @param cameraProjection the cameraProjection to set
	 */
	public void setCameraProjection(RtToken cameraProjection) {
		scope.set(INTERNAL, CAMERA_PROJECTION, cameraProjection);
	}


	/**
	 * @return the worldToCamera
	 */
	public RtMatrix getWorldToCamera() {
		return (RtMatrix) scope.get(INTERNAL, WORLD_TO_CAMERA);
	}


	/**
	 * @param worldToCamera the worldToCamera to set
	 */
	public void setWorldToCamera(RtMatrix worldToCamera) {
		scope.set(INTERNAL, WORLD_TO_CAMERA, worldToCamera);
	}


	/**
	 * @return the clippingInterval
	 */
	public Interval getClippingInterval() {
		return (Interval) scope.get(INTERNAL, CLIPPING_INTERVAL);
	}


	/**
	 * @param clippingInterval the clippingInterval to set
	 */
	public void setClippingInterval(Interval clippingInterval) {
		scope.set(INTERNAL, CLIPPING_INTERVAL, clippingInterval);
	}


	/**
	 * @return the clippingPlanes
	 */
	public Set<Plane3> getClippingPlanes() {
		return clippingPlanes;
	}


	/**
	 * @return the fStop
	 */
	public double getfStop() {
		return (Double) scope.get(INTERNAL, FSTOP);
	}


	/**
	 * @param fStop the fStop to set
	 */
	public void setfStop(double fStop) {
		scope.set(INTERNAL, FSTOP, fStop);
	}


	/**
	 * @return the focalLength
	 */
	public double getFocalLength() {
		return (Double) scope.get(INTERNAL, FOCAL_LENGTH);
	}


	/**
	 * @param focalLength the focalLength to set
	 */
	public void setFocalLength(double focalLength) {
		scope.set(INTERNAL, FOCAL_LENGTH, focalLength);
	}


	/**
	 * @return the focalDistance
	 */
	public double getFocalDistance() {
		return (Double) scope.get(INTERNAL, FOCAL_DISTANCE);
	}


	/**
	 * @param focalDistance the focalDistance to set
	 */
	public void setFocalDistance(double focalDistance) {
		scope.set(INTERNAL, FOCAL_DISTANCE, focalDistance);
	}


	/**
	 * @return the shutter
	 */
	public Interval getShutter() {
		return (Interval) scope.get(INTERNAL, SHUTTER);
	}


	/**
	 * @param shutter the shutter to set
	 */
	public void setShutter(Interval shutter) {
		scope.set(INTERNAL, SHUTTER, shutter);
	}


	/**
	 * @return the pixelVariance
	 */
	public double getPixelVariance() {
		return (Double) scope.get(INTERNAL, PIXEL_VARIANCE);
	}


	/**
	 * @param pixelVariance the pixelVariance to set
	 */
	public void setPixelVariance(double pixelVariance) {
		scope.set(INTERNAL, PIXEL_VARIANCE, pixelVariance);
	}


	/**
	 * @return the horizontalSamplingRate
	 */
	public double getHorizontalSamplingRate() {
		return (Double) scope.get(INTERNAL, HORIZONTAL_SAMPLING_RATE);
	}


	/**
	 * @param horizontalSamplingRate the horizontalSamplingRate to set
	 */
	public void setHorizontalSamplingRate(double horizontalSamplingRate) {
		scope.set(INTERNAL, HORIZONTAL_SAMPLING_RATE,
				Math.max(horizontalSamplingRate, 1.0));
	}


	/**
	 * @return the verticalSamplingRate
	 */
	public double getVerticalSamplingRate() {
		return (Double) scope.get(INTERNAL, VERTICAL_SAMPLING_RATE);
	}


	/**
	 * @param verticalSamplingRate the verticalSamplingRate to set
	 */
	public void setVerticalSamplingRate(double verticalSamplingRate) {
		scope.set(INTERNAL, VERTICAL_SAMPLING_RATE, verticalSamplingRate);
	}


	/**
	 * @return the filter
	 */
	public RtFilterFunc getPixelFilter() {
		return (RtFilterFunc) scope.get(INTERNAL, PIXEL_FILTER);
	}


	/**
	 * @param pixelFilter the filter to set
	 */
	public void setPixelFilter(RtFilterFunc pixelFilter) {
		scope.set(INTERNAL, PIXEL_FILTER, pixelFilter);
	}


	/**
	 * @return the horizontalFilterWidth
	 */
	public double getHorizontalFilterWidth() {
		return (Double) scope.get(INTERNAL, HORIZONTAL_FILTER_WIDTH);
	}


	/**
	 * @param horizontalFilterWidth the horizontalFilterWidth to set
	 */
	public void setHorizontalFilterWidth(double horizontalFilterWidth) {
		scope.set(INTERNAL, HORIZONTAL_FILTER_WIDTH, horizontalFilterWidth);
	}


	/**
	 * @return the verticalFilterWidth
	 */
	public double getVerticalFilterWidth() {
		return (Double) scope.get(INTERNAL, VERTICAL_FILTER_WIDTH);
	}


	/**
	 * @param verticalFilterWidth the verticalFilterWidth to set
	 */
	public void setVerticalFilterWidth(double verticalFilterWidth) {
		scope.set(INTERNAL, VERTICAL_FILTER_WIDTH, verticalFilterWidth);
	}


	/**
	 * @return the exposureGain
	 */
	public double getExposureGain() {
		return (Double) scope.get(INTERNAL, EXPOSURE_GAIN);
	}


	/**
	 * @param exposureGain the exposureGain to set
	 */
	public void setExposureGain(double exposureGain) {
		scope.set(INTERNAL, EXPOSURE_GAIN, exposureGain);
	}


	/**
	 * @return the exposureGamma
	 */
	public double getExposureGamma() {
		return (Double) scope.get(INTERNAL, EXPOSURE_GAMMA);
	}


	/**
	 * @param exposureGamma the exposureGamma to set
	 */
	public void setExposureGamma(double exposureGamma) {
		scope.set(INTERNAL, EXPOSURE_GAMMA, exposureGamma);
	}


	/**
	 * @return the colorQuantizerOne
	 */
	public int getColorQuantizerOne() {
		return (Integer) scope.get(INTERNAL, COLOR_QUANTIZER_ONE);
	}


	/**
	 * @param colorQuantizerOne the colorQuantizerOne to set
	 */
	public void setColorQuantizerOne(int colorQuantizerOne) {
		scope.set(INTERNAL, COLOR_QUANTIZER_ONE, colorQuantizerOne);
	}


	/**
	 * @return the colorQuantizerMinimum
	 */
	public int getColorQuantizerMinimum() {
		return (Integer) scope.get(INTERNAL, COLOR_QUANTIZER_MINIMUM);
	}


	/**
	 * @param colorQuantizerMinimum the colorQuantizerMinimum to set
	 */
	public void setColorQuantizerMinimum(int colorQuantizerMinimum) {
		scope.set(INTERNAL, COLOR_QUANTIZER_MINIMUM, colorQuantizerMinimum);
	}


	/**
	 * @return the colorQuantizerMaximum
	 */
	public int getColorQuantizerMaximum() {
		return (Integer) scope.get(INTERNAL, COLOR_QUANTIZER_MAXIMUM);
	}


	/**
	 * @param colorQuantizerMaximum the colorQuantizerMaximum to set
	 */
	public void setColorQuantizerMaximum(int colorQuantizerMaximum) {
		scope.set(INTERNAL, COLOR_QUANTIZER_MAXIMUM, colorQuantizerMaximum);
	}


	/**
	 * @return the colorQuantizerDitherAmplitude
	 */
	public double getColorQuantizerDitherAmplitude() {
		return (Integer) scope.get(INTERNAL, COLOR_QUANTIZER_DITHER_AMPLITUDE);
	}


	/**
	 * @param colorQuantizerDitherAmplitude the colorQuantizerDitherAmplitude to set
	 */
	public void setColorQuantizerDitherAmplitude(
			double colorQuantizerDitherAmplitude) {
		scope.set(INTERNAL, COLOR_QUANTIZER_DITHER_AMPLITUDE, colorQuantizerDitherAmplitude);
	}


	/**
	 * @return the depthQuantizerOne
	 */
	public int getDepthQuantizerOne() {
		return (Integer) scope.get(INTERNAL, DEPTH_QUANTIZER_ONE);
	}


	/**
	 * @param depthQuantizerOne the depthQuantizerOne to set
	 */
	public void setDepthQuantizerOne(int depthQuantizerOne) {
		scope.set(INTERNAL, DEPTH_QUANTIZER_ONE, depthQuantizerOne);
	}


	/**
	 * @return the depthQuantizerMinimum
	 */
	public int getDepthQuantizerMinimum() {
		return (Integer) scope.get(INTERNAL, DEPTH_QUANTIZER_MINIMUM);
	}


	/**
	 * @param depthQuantizerMinimum the depthQuantizerMinimum to set
	 */
	public void setDepthQuantizerMinimum(int depthQuantizerMinimum) {
		scope.set(INTERNAL, DEPTH_QUANTIZER_MINIMUM, depthQuantizerMinimum);
	}


	/**
	 * @return the depthQuantizerMaximum
	 */
	public int getDepthQuantizerMaximum() {
		return (Integer) scope.get(INTERNAL, DEPTH_QUANTIZER_MAXIMUM);
	}


	/**
	 * @param depthQuantizerMaximum the depthQuantizerMaximum to set
	 */
	public void setDepthQuantizerMaximum(int depthQuantizerMaximum) {
		scope.set(INTERNAL, DEPTH_QUANTIZER_MAXIMUM, depthQuantizerMaximum);
	}


	/**
	 * @return the depthQuantizerDitherAmplitude
	 */
	public double getDepthQuantizerDitherAmplitude() {
		return (Integer) scope.get(INTERNAL, DEPTH_QUANTIZER_DITHER_AMPLITUDE);
	}


	/**
	 * @param depthQuantizerDitherAmplitude the depthQuantizerDitherAmplitude to set
	 */
	public void setDepthQuantizerDitherAmplitude(
			double depthQuantizerDitherAmplitude) {
		scope.set(INTERNAL, DEPTH_QUANTIZER_DITHER_AMPLITUDE, depthQuantizerDitherAmplitude);
	}


	/**
	 * @return the displayType
	 */
	public RtToken getDisplayType() {
		return (RtToken) scope.get(INTERNAL, DISPLAY_TYPE);
	}


	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(RtToken displayType) {
		scope.set(INTERNAL, DISPLAY_TYPE, displayType);
	}


	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return (String) scope.get(INTERNAL, DISPLAY_NAME);
	}


	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		scope.set(INTERNAL, DISPLAY_NAME, displayName);
	}


	/**
	 * @return the displayMode
	 */
	public RtToken getDisplayMode() {
		return (RtToken) scope.get(INTERNAL, DISPLAY_MODE);
	}


	/**
	 * @param displayMode the displayMode to set
	 */
	public void setDisplayMode(RtToken displayMode) {
		scope.set(INTERNAL, DISPLAY_MODE, displayMode);
	}


	/**
	 * @return the hider
	 */
	public RtToken getHider() {
		return (RtToken) scope.get(INTERNAL, HIDER);
	}


	/**
	 * @param hider the hider to set
	 */
	public void setHider(RtToken hider) {
		scope.set(INTERNAL, HIDER, hider);
	}


	/**
	 * @return the colorSamples
	 */
	public int getColorSamples() {
		return (Integer) scope.get(INTERNAL, COLOR_SAMPLES);
	}


	/**
	 * @param colorSamples the colorSamples to set
	 */
	public void setColorSamples(int colorSamples) {
		scope.set(INTERNAL, COLOR_SAMPLES, colorSamples);
	}


	/**
	 * @return the relativeDetail
	 */
	public double getRelativeDetail() {
		return (Double) scope.get(INTERNAL, RELATIVE_DETAIL);
	}


	/**
	 * @param relativeDetail the relativeDetail to set
	 */
	public void setRelativeDetail(double relativeDetail) {
		scope.set(INTERNAL, RELATIVE_DETAIL, relativeDetail);
	}

	@Override
	public Object get(RtToken name, RtToken param) {
		return scope.get(name, param);
	}

	@Override
	public void set(RtToken name, RtToken param, Object value) {
		scope.set(name, param, value);
	}
	
}
