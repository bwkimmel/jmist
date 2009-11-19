/**
 * 
 */
package ca.eandb.jmist.framework.job.mdt;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.EyeTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.ScatteringNode;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PartialFrameLens implements Lens {
	
	private final Lens inner;
	
	private final double fraction;

	/**
	 * @param inner
	 */
	public PartialFrameLens(Lens inner, double fraction) {
		this.inner = inner;
		this.fraction = fraction;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#rayAt(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd) {
		return inner.rayAt(p, lambda, rnd); 
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Lens#sample(ca.eandb.jmist.math.Point2, ca.eandb.jmist.framework.path.PathInfo, double, double, double)
	 */
	public EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv,
			double rj) {
		return new Node(inner.sample(p, pathInfo, ru, rv, rj));
	}
	
	private final class Node extends EyeTerminalNode {
		
		private final EyeNode inner;
		
		public Node(EyeNode inner) {
			super(inner.getPathInfo(), inner.getRU(), inner.getRV(), inner.getRJ());
			this.inner = inner;
		}

		public Point2 project(HPoint3 x) {
			return inner.project(x);
		}

		public double getCosine(Vector3 v) {
			return inner.getCosine(v);
		}

		public double getPDF() {
			return inner.getPDF() / fraction;
		}

		public double getPDF(Vector3 v) {
			return inner.getPDF(v) / fraction;
		}

		public HPoint3 getPosition() {
			return inner.getPosition();
		}

		public boolean isSpecular() {
			return inner.isSpecular();
		}

		public ScatteredRay sample(double ru, double rv, double rj) {
			ScatteredRay sr = inner.sample(ru, rv, rj);
			return new ScatteredRay(sr.getRay(), sr.getColor(), sr.getType(), sr.getPDF() / fraction, sr.isTransmitted());
		}

		public Color scatter(Vector3 v) {
			return inner.scatter(v);
		}
		
	}

}
