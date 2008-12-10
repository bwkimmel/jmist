/**
 *
 */
package ca.eandb.jmist.packages.geometry;

import ca.eandb.jmist.framework.BoundingBoxHierarchy3;
import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Visitor;
import ca.eandb.jmist.toolkit.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public final class BoundingBoxHierarchyGeometry extends CompositeGeometry {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(final Ray3 ray, final IntersectionRecorder recorder) {

		this.bbh.intersect(ray, recorder.interval(), new Visitor() {

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.Visitor#visit(java.lang.Object)
			 */
			public boolean visit(Object item) {
				Geometry geometry = (Geometry) item;
				geometry.intersect(ray, recorder);
				return true;
			}

		});

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.packages.CompositeGeometry#addChild(ca.eandb.jmist.framework.Geometry)
	 */
	@Override
	public CompositeGeometry addChild(Geometry child) {
		this.bbh.addItem(child, child.boundingBox());
		return super.addChild(child);
	}

	private final BoundingBoxHierarchy3 bbh = new BoundingBoxHierarchy3();

}
