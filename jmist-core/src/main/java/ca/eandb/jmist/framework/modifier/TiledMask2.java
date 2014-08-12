/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.modifier;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.math.Point2;

/**
 * A decorator <code>Mask2</code> that tiles the decorated <code>Mask2</code>.
 */
public final class TiledMask2 implements Mask2 {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2040730054854181920L;

	/** The <code>Mask2</code> to be tiled. */
	private final Mask2 inner;

	/**
	 * Creates a new <code>TiledMask2</code>.
	 * @param inner The <code>Mask2</code> to be tiled.
	 */
	public TiledMask2(Mask2 inner) {
		this.inner = inner;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Mask2#opacity(ca.eandb.jmist.math.Point2)
	 */
	@Override
	public double opacity(Point2 p) {
		p = new Point2(p.x() - Math.floor(p.x()), p.y() - Math.floor(p.y()));
		return inner.opacity(p);
	}

}
