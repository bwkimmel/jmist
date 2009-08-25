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

package ca.eandb.jmist.framework.lens;

import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public abstract class SingularApertureLens extends AbstractLens {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.lens.AbstractLens#project(ca.eandb.jmist.math.Vector3)
	 */
	public abstract Projection project(Vector3 p);

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#project(ca.eandb.jmist.math.Point3)
	 */
	public final Projection project(Point3 p) {
		return project(p.vectorFromOrigin());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#areaOfAperture()
	 */
	public final double areaOfAperture() {
		return 0.0;
	}

}
