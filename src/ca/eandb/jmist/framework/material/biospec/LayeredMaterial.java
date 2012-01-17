/**
 *
 */
package ca.eandb.jmist.framework.material.biospec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> constructed by layering child
 * <code>SurfaceScatterer</code>s.
 * @author Brad Kimmel
 */
public final class LayeredMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8457484575034697420L;

	/**
	 * A <code>List</code> of the <code>SurfaceScatterer</code>s representing
	 * the layers of this <code>SurfaceScatterer</code>.
	 */
	private final List<Material> layers = new ArrayList<Material>();
	
	private final Random rnd = new ThreadLocalRandom(new SimpleRandom());

	/**
	 * Adds a <code>SurfaceScatterer</code> as a new layer on the top.
	 * @param e The <code>SurfaceScatterer</code> to add as the new top layer.
	 * @return This <code>LayeredSurfaceScatterer</code>.
	 */
	public LayeredMaterial addLayerToTop(Material e) {
		layers.add(0, e);
		return this;
	}

	/**
	 * Adds a <code>SurfaceScatterer</code> as a new layer on the bottom.
	 * @param e The <code>SurfaceScatterer</code> to add as the new bottom
	 * 		layer.
	 * @return This <code>LayeredSurfaceScatterer</code>.
	 */
	public LayeredMaterial addLayerToBottom(Material e) {
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
	
	/**
	 * Gets the <code>List</code> of layers that comprise this
	 * <code>LayeredSurfaceScatterer</code>.  The returned list is
	 * unmodifiable.
	 * @return The <code>List</code> of layers that comprise this
	 * 		<code>LayeredSurfaceScatterer</code>.
	 */
	public List<Material> getLayers() {
		return Collections.unmodifiableList(layers);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {

		Vector3 N = x.getNormal();
		int	depth = (v.dot(N) > 0.0) ? (layers.size() - 1) : 0;
		int dir;
		
		ScatteredRay sr;
		Color col = lambda.getColorModel().getWhite(lambda);

		do  {

			Material layer = layers.get(depth);
			sr = layer.scatter(x, v, adjoint, lambda, rnd.next(), rnd.next(), rnd.next());

			if (sr == null) return null;
			v = sr.getRay().direction();

			dir = (v.dot(N) > 0.0) ? -1 : 1;
			depth += dir;
			
			col = col.times(sr.getColor());

		} while (depth >= 0 && depth < layers.size());

		return depth < 0
			? ScatteredRay.diffuse(sr.getRay(), col, 1.0)
			: ScatteredRay.transmitDiffuse(sr.getRay(), col, 1.0);
	}

}
