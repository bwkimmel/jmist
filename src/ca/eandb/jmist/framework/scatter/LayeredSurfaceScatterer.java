/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> constructed by layering child
 * <code>SurfaceScatterer</code>s.
 * @author Brad Kimmel
 */
public final class LayeredSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8457484575034697420L;

	/**
	 * A <code>List</code> of the <code>SurfaceScatterer</code>s representing
	 * the layers of this <code>SurfaceScatterer</code>.
	 */
	private final List<SurfaceScatterer> layers = new ArrayList<SurfaceScatterer>();

	/**
	 * Adds a <code>SurfaceScatterer</code> as a new layer on the top.
	 * @param e The <code>SurfaceScatterer</code> to add as the new top layer.
	 * @return This <code>LayeredSurfaceScatterer</code>.
	 */
	public LayeredSurfaceScatterer addLayerToTop(SurfaceScatterer e) {
		layers.add(0, e);
		return this;
	}

	/**
	 * Adds a <code>SurfaceScatterer</code> as a new layer on the bottom.
	 * @param e The <code>SurfaceScatterer</code> to add as the new bottom
	 * 		layer.
	 * @return This <code>LayeredSurfaceScatterer</code>.
	 */
	public LayeredSurfaceScatterer addLayerToBottom(SurfaceScatterer e) {
		layers.add(e);
		return this;
	}

	/** Removes all layers. */
	public void clear() {
		layers.clear();
	}

	/**
	 * Gets the number of layers.
	 * @return The number of layers.
	 */
	public int getNumLayers() {
		return layers.size();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {

		Vector3 N = x.getNormal();
		int	depth = (v.dot(N) > 0.0) ? (layers.size() - 1) : 0;
		int dir;

		do  {

			SurfaceScatterer layer = layers.get(depth);
			v = layer.scatter(x, v, adjoint, lambda, rnd);

			if (v == null) break;

			dir = (v.dot(N) > 0.0) ? -1 : 1;
			depth += dir;

		} while (depth >= 0 && depth < layers.size());

		return v;
	}

}
