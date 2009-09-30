/**
 * 
 */
package ca.eandb.jmist.framework.job.mlt;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;

/**
 * @author Brad
 *
 */
public final class LensSubpathMutator implements PathMutator {
	
	private final int nx;
	
	private final int ny;
	
	
	
	public LensSubpathMutator(int nx, int ny) {
		this.nx = nx;
		this.ny = ny;
		
		this.eyeRandom = new ThreadLocalRandom();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.job.mlt.PathMutator#getTransitionPDF(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.path.Path)
	 */
	@Override
	public double getTransitionPDF(Path from, Path to) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.job.mlt.PathMutator#mutate(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Path mutate(Path path, Random rnd) {
		// TODO Auto-generated method stub
		return null;
	}

}
