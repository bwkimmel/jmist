/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import javax.media.j3d.Node;

/**
 * @author Brad
 *
 */
interface Java3DVisitor {

	void visit(Node n);
	
}
