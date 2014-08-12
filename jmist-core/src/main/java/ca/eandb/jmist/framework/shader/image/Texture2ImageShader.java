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
package ca.eandb.jmist.framework.shader.image;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * An <code>ImageShader</code> that adapts a <code>Texture2</code>.
 * @author Brad Kimmel
 */
public final class Texture2ImageShader implements ImageShader {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -5238069418249067354L;
	
	/** The <code>Texture2</code> to adapt. */
	private final Texture2 texture;
	
	/**
	 * Creates a new <code>Texture2ImageShader</code>.
	 * @param texture The <code>Texture2</code> to adapt.
	 */
	public Texture2ImageShader(Texture2 texture) {
		this.texture = texture;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ImageShader#shadeAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color shadeAt(Point2 p, WavelengthPacket lambda) {
		return texture.evaluate(p, lambda);
	}

}
