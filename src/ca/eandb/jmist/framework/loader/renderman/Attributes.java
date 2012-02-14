/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.Set;

import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
final class Attributes implements State {

	/* Shading Attributes (Table 4.6) */
	
	private RtColor color;
	private RtColor opacity;
	private final Point2[] textureCoordinates = new Point2[4];
	private Shader surface;
	private Set<RtLightHandle> lightSources;
	
	private double effectiveShadingRate = 1.0;
	
	private RtToken shadingInterpolation = RenderManContext.RI_CONSTANT;
	
	private boolean matteSurfaceFlag = false;
	
	/* Geometry Attributes (Table 4.11) */
	private RtMatrix objectToWorld = RtMatrix.IDENTITY;
	private RtBound bound;
	private RtBound detail;
	private double[] detailRange = { 0.0, 0.0, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
	private RtToken geometricApproximationType;
	private double geometricApproximationValue;
	private RtMatrix[] cubicBasisMatrices = { null, null }; /* bezier, bezier */
	private int[] cubicBasisSteps = { 2, 2 };
	/* TODO trim curves */
	private RtToken orientation = RenderManContext.RI_OUTSIDE;
	private int numberOfSides = 2;
	/* TODO displacement */
	
	private final State scope;
	
	public Attributes() {
		this.scope = new ScopedState();
	}
	
	private Attributes(Attributes outer) {
		this.scope = outer.scope.clone();
	}
	
	public Attributes clone() {
		return new Attributes(this);
	}
	
	/**
	 * @return the color
	 */
	public RtColor getColor() {
		return color;
	}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(RtColor color) {
		this.color = color;
	}
	
	/**
	 * @return the opacity
	 */
	
	public RtColor getOpacity() {
		return opacity;
	}
	
	/**
	 * @param opacity the opacity to set
	 */
	public void setOpacity(RtColor opacity) {
		this.opacity = opacity;
	}
	
	public void setTextureCoordinates(Point2 st1, Point2 st2, Point2 st3, Point2 st4) {
		this.textureCoordinates[0] = st1;
		this.textureCoordinates[1] = st2;
		this.textureCoordinates[2] = st3;
		this.textureCoordinates[3] = st4;
	}
	
	/**
	 * @return the effectiveShadingRate
	 */
	public double getEffectiveShadingRate() {
		return effectiveShadingRate;
	}
	
	/**
	 * @param effectiveShadingRate the effectiveShadingRate to set
	 */
	public void setEffectiveShadingRate(double effectiveShadingRate) {
		this.effectiveShadingRate = effectiveShadingRate;
	}
	
	/**
	 * @return the shadingInterpolation
	 */
	public RtToken getShadingInterpolation() {
		return shadingInterpolation;
	}
	
	/**
	 * @param shadingInterpolation the shadingInterpolation to set
	 */
	public void setShadingInterpolation(RtToken shadingInterpolation) {
		this.shadingInterpolation = shadingInterpolation;
	}
	
	/**
	 * @return the matteSurfaceFlag
	 */
	public boolean getMatteSurfaceFlag() {
		return matteSurfaceFlag;
	}
	
	/**
	 * @param matteSurfaceFlag the matteSurfaceFlag to set
	 */
	public void setMatteSurfaceFlag(boolean matteSurfaceFlag) {
		this.matteSurfaceFlag = matteSurfaceFlag;
	}
	
	/**
	 * @return the objectToWorld
	 */
	public RtMatrix getObjectToWorld() {
		return objectToWorld;
	}
	
	/**
	 * @param objectToWorld the objectToWorld to set
	 */
	public void setObjectToWorld(RtMatrix objectToWorld) {
		this.objectToWorld = objectToWorld;
	}
	
	/**
	 * @return the bound
	 */
	public RtBound getBound() {
		return bound;
	}
	
	/**
	 * @param bound the bound to set
	 */
	public void setBound(RtBound bound) {
		this.bound = bound;
	}
	
	/**
	 * @return the detail
	 */
	public RtBound getDetail() {
		return detail;
	}
	
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(RtBound detail) {
		this.detail = detail;
	}
	
	/**
	 * @return the detailRange
	 */
	public double[] getDetailRange() {
		return detailRange;
	}
	
	/**
	 * @param detailRange the detailRange to set
	 */
	public void setDetailRange(double minvisible, double lowertransition, double uppertransition, double maxvisible) {
		this.detailRange[0] = minvisible;
		this.detailRange[1] = lowertransition;
		this.detailRange[2] = uppertransition;
		this.detailRange[3] = maxvisible;
	}
	
	/**
	 * @return the geometricApproximation
	 */
	public RtToken getGeometricApproximationType() {
		return geometricApproximationType;
	}
	/**
	 * @return the geometricApproximationValue
	 */
	public double getGeometricApproximationValue() {
		return geometricApproximationValue;
	}
	
	/**
	 * @param type
	 * @param value
	 */
	public void setGeometricApproximation(RtToken type, double value) {
		this.geometricApproximationType = type;
		this.geometricApproximationValue = value;
	}
	
	/**
	 * @return the cubicBasisMatrices
	 */
	public RtMatrix[] getCubicBasisMatrices() {
		return cubicBasisMatrices;
	}
	
	/**
	 * @param cubicBasisMatrices the cubicBasisMatrices to set
	 */
	public void setCubicBasisMatrices(RtMatrix[] cubicBasisMatrices) {
		this.cubicBasisMatrices = cubicBasisMatrices;
	}
	
	/**
	 * @return the cubicBasisSteps
	 */
	public int[] getCubicBasisSteps() {
		return cubicBasisSteps;
	}
	
	/**
	 * @param cubicBasisSteps the cubicBasisSteps to set
	 */
	public void setCubicBasisSteps(int[] cubicBasisSteps) {
		this.cubicBasisSteps = cubicBasisSteps;
	}
	
	/**
	 * @return the orientation
	 */
	public RtToken getOrientation() {
		return orientation;
	}
	
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(RtToken orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * @return the numberOfSides
	 */
	public int getNumberOfSides() {
		return numberOfSides;
	}
	
	/**
	 * @param numberOfSides the numberOfSides to set
	 */
	public void setNumberOfSides(int numberOfSides) {
		this.numberOfSides = numberOfSides;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.State#get(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public Object get(RtToken name, RtToken param) {
		return scope.get(name, param);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.State#set(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.Object)
	 */
	@Override
	public void set(RtToken name, RtToken param, Object value) {
		scope.set(name, param, value);		
	}
	
}
