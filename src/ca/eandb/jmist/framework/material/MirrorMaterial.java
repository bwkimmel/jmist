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
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An ideally reflective <code>Material</code>.
 * @author Brad Kimmel
 */
public final class MirrorMaterial extends OpaqueMaterial {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3451068808276962270L;

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Painter</code> of this mirror.
	 */
	public MirrorMaterial(Painter reflectance) {
		this.reflectance = reflectance;
	}

	/**
	 * Creates a new <code>MirrorMaterial</code>.
	 * @param reflectance The reflectance <code>Spectrum</code> of this mirror.
	 */
	public MirrorMaterial(Spectrum reflectance) {
		this(new UniformPainter(reflectance));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, double ru, double rv, double rj) {

		Vector3 out = Optics.reflect(v, x.getShadingNormal());

		return ScatteredRay.specular(new Ray3(x.getPosition(), out),
				reflectance.getColor(x, lambda), 1.0);

	}

	/**
	 * The reflectance <code>Painter</code> of this
	 * <code>MirrorMaterial</code>.
	 */
	private final Painter reflectance;

}
