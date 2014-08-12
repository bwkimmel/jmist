/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.scene.EmptyScene;

/**
 * Represents the context in which a path is created.
 * @author Brad Kimmel
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
