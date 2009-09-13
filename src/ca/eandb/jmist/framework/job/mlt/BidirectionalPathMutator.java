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

package ca.eandb.jmist.framework.job.mlt;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.path.PathNode;

/**
 * @author brad
 *
 */
public final class BidirectionalPathMutator implements PathMutator {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.job.mlt.PathMutator#getTransitionPDF(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.path.Path)
	 */
	public double getTransitionPDF(Path from, Path to) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.job.mlt.PathMutator#mutate(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.Random)
	 */
	public Path mutate(Path path, Random rnd) {

		int s = ???;
		int t = ???;

		path = path.slice(s, t);

		PathNode newLightTail = path.getLightTail();
		PathNode newEyeTail = path.getEyeTail();

		int l1 = ???;
		int m1 = ???;

		while (l1-- > 0) {
			newLightTail = newLightTail.expand(rnd);
		}

		while (m1-- > 0) {
			newEyeTail = newEyeTail.expand(rnd);
		}

		return new Path(newLightTail, newEyeTail);

	}
	
	private int getDeletedPathLength(Random rnd) {
		double x = rnd.next();
		if (x < 0.25) {
			return 1;
		} else if (x < 0.75) {
			return 2;
		} else {
			int i = 3;
			x = 4.0 * (x - 0.75);
			while (x >= 0.5) i++;
			return i;
		}
	}

}
