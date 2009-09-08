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

	public PathInfo(WavelengthPacket wavelengthPacket) {
		this(EmptyScene.INSTANCE, wavelengthPacket);
	}

	public PathInfo(Scene scene, WavelengthPacket wavelengthPacket) {
		this.scene = scene;
		this.wavelengthPacket = wavelengthPacket;
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

}
