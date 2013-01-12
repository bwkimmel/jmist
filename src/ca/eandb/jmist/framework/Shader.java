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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;

/**
 * Computes a <code>Color</code> from a <code>ShadingContext</code>, which
 * represents the state of the current ray-intersection event.  How this
 * done depends on the specific <code>Shader</code>, but this will typically
 * involve scattering the incident ray using a <code>Material</code> and
 * recursively ray-tracing the scattered ray, or illuminating the material
 * directly from a light source, etc.
 * @author Brad Kimmel
 */
public interface Shader extends Serializable {

	/** 
	 * Computes a <code>Color</code> based on the provided context representing
	 * the state of the current ray-intersection event.
	 * @param sc The <code>ShadingContext</code>.
	 * @return The resulting <code>Color</code>.
	 */
	Color shade(ShadingContext sc);

	/** A dummy <code>Shader</code> that always returns black. */
	public static final Shader BLACK = new Shader() {
		private static final long serialVersionUID = 7697217516861477920L;
		public Color shade(ShadingContext sc) {
			return sc.getWavelengthPacket().getColorModel().getBlack(
					sc.getWavelengthPacket());
		}
	};

}
