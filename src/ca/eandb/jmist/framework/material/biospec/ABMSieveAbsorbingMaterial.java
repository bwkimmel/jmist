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
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> representing a absorptive layer with sieve effects
 * in the ABM-B/ABM-U light transport models.
 * @author Brad Kimmel
 * @see ABMMaterial
 */
public final class ABMSieveAbsorbingMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2192497635802551618L;

	/** The absorption coefficient of the medium (in m<sup>-1</sup>). */
	private final Spectrum absorptionCoefficient;

	/** The thickness of the medium (in meters). */
	private final double thickness;

	/**
	 * Creates a new <code>AbsorbingSurfaceScatterer</code>.
	 * @param absorptionCoefficient The absorption coefficient of the medium
	 * 		(in m<sup>-1</sup>).
	 * @param thickness The thickness of the medium (in meters).
	 */
	public ABMSieveAbsorbingMaterial(Spectrum absorptionCoefficient,
			double thickness) {
		this.absorptionCoefficient = absorptionCoefficient;
		this.thickness = thickness;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		
		Color col = absorptionCoefficient.sample(lambda);
//		double abs = ColorUtil.getMeanChannelValue(col);
//		
//		if (abs > MathUtil.EPSILON) {
//			double p = -Math.log(1.0 - rnd.next()) * Math.cos(Math.abs(x.getNormal().dot(v))) / abs;
//			
//			col = col.times(-thickness).exp();
//			col = col.divide(ColorUtil.getMeanChannelValue(col));
//			
//			if (p > thickness) {
//				return ScatteredRay.transmitSpecular(new Ray3(x.getPosition(), v), col, 1.0);
//			}
//		}
//		
//		return null;
		
		col = col.times(-thickness / Math.cos(Math.abs(x.getNormal().dot(v)))).exp();
		return ScatteredRay.transmitSpecular(new Ray3(x.getPosition(), v), col, 1.0);		
		
	}

}
