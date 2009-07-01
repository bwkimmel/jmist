/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.DirectionalTexture3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * A uniformly colored <code>DirectionalTexture3</code>.
 * @author brad
 */
public final class UniformDirectionalTexture3 implements DirectionalTexture3 {

	/**
	 * The <code>Spectrum</code> assigned to this
	 * <code>UniformDirectionalTexture3</code>.
	 */
	private final Spectrum value;

	/**
	 * Creates a new <code>UniformDirectionalTexture3</code>.
	 * @param value The <code>Spectrum</code> assigned to this
	 * 		<code>UniformDirectionalTexture3</code>.
	 */
	public UniformDirectionalTexture3(Spectrum value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.DirectionalTexture3#evaluate(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color evaluate(Vector3 v, WavelengthPacket lambda) {
		return value.sample(lambda);
	}

}
