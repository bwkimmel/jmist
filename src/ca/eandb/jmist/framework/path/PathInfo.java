/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.scene.EmptyScene;

/**
 * @author Brad
 *
 */
public final class PathInfo {

	private final Scene scene;

	private final WavelengthPacket wavelengthPacket;
	
	private final double time;

	public PathInfo(WavelengthPacket wavelengthPacket) {
		this(EmptyScene.INSTANCE, wavelengthPacket, 0.0);
	}

	public PathInfo(WavelengthPacket wavelengthPacket, double time) {
		this(EmptyScene.INSTANCE, wavelengthPacket, time);
	}

	public PathInfo(Scene scene, WavelengthPacket wavelengthPacket, double time) {
		this.scene = scene;
		this.wavelengthPacket = wavelengthPacket;
		this.time = time;
	}

	public PathInfo(Scene scene, WavelengthPacket wavelengthPacket) {
		this(scene, wavelengthPacket, 0.0);
	}

	public ColorModel getColorModel() {
		return wavelengthPacket.getColorModel();
	}

	public WavelengthPacket getWavelengthPacket() {
		return wavelengthPacket;
	}

	public Scene getScene() {
		return scene;
	}
	
	public double getTime() {
		return time;
	}

}
