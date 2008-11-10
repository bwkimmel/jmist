/**
 *
 */
package org.jmist.packages.geometry;

import org.jmist.framework.BoundingBoxHierarchy3;
import org.jmist.framework.Geometry;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Visitor;
import org.jmist.toolkit.Ray3;

/**
 * @author bkimmel
 *
 */
public final class BoundingBoxHierarchyGeometry extends CompositeGeometry {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(final Ray3 ray, final IntersectionRecorder recorder) {

		this.bbh.intersect(ray, recorder.interval(), new Visitor() {

			/* (non-Javadoc)
			 * @see org.jmist.framework.Visitor#visit(java.lang.Object)
			 */
			public boolean visit(Object item) {
				Geometry geometry = (Geometry) item;
				geometry.intersect(ray, recorder);
				return true;
			}

		});

	}

	/* (non-Javadoc)
	 * @see org.jmist.packages.CompositeGeometry#addChild(org.jmist.framework.Geometry)
	 */
	@Override
	public CompositeGeometry addChild(Geometry child) {
		this.bbh.addItem(child, child.boundingBox());
		return super.addChild(child);
	}

	private final BoundingBoxHierarchy3 bbh = new BoundingBoxHierarchy3();

}
