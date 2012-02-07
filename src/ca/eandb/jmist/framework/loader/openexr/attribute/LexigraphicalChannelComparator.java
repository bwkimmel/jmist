/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.util.Comparator;

/**
 * @author brad
 *
 */
/* package */ final class LexigraphicalChannelComparator implements Comparator<Channel> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Channel o1, Channel o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
