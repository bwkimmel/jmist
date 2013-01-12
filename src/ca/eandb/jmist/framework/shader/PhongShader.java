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
package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PhongShader implements Shader {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -7555950836943828911L;

	private final Painter diffuse;

	private final Painter specular;

	private final Painter ambient;

	private final Painter exponent;

	/**
	 * @param diffuse
	 * @param specular
	 * @param ambient
	 * @param exponent
	 */
	public PhongShader(Painter kd, Painter ks, Painter ka, Painter n) {
		this.diffuse = kd;
		this.specular = ks;
		this.ambient = ka;
		this.exponent = n;
	}

	/**
	 * @param diffuse
	 * @param specular
	 * @param ambient
	 * @param exponent
	 */
	public PhongShader(Spectrum kd, Spectrum ks, Spectrum ka, Spectrum n) {
		this(new UniformPainter(kd), new UniformPainter(ks), new UniformPainter(ka), new UniformPainter(n));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	public Color shade(ShadingContext sc) {

		WavelengthPacket lambda = sc.getWavelengthPacket();
		ColorModel cm = sc.getColorModel();

		Color kd = diffuse.getColor(sc, lambda);
		Color ks = specular.getColor(sc, lambda);
		Color ka = ambient.getColor(sc, lambda);
		Color n = exponent.getColor(sc, lambda);

		Vector3 N = sc.getShadingNormal();
		Vector3 V = sc.getIncident().opposite();

		Color d = cm.getBlack(lambda);
		Color s = d;
		Color a = sc.getAmbientLight();

		for (LightSample sample : sc.getLightSamples()) {
			Vector3 L = sample.getDirToLight();
			Vector3 H = L.plus(V).unit();
			Color I = sample.getRadiantIntensity();

			d = d.plus(I.times(N.dot(L)));
			s = s.plus(I.times(cm.getGray(N.dot(H), lambda).pow(n)));
		}

		d = d.times(kd);
		s = s.times(ks);
		a = a.times(ka);

		return d.plus(s).plus(a);

	}

}
