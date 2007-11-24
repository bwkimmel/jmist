/**
 *
 */
package org.jmist.packages.light;

import org.jmist.framework.Illuminable;
import org.jmist.framework.Light;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;

/**
 * @author bkimmel
 *
 */
public final class DayLight implements Light {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	@Override
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			Illuminable target) {

	}

}
