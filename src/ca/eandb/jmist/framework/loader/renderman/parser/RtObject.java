/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman.parser;

import ca.eandb.jmist.framework.loader.renderman.RtBound;
import ca.eandb.jmist.framework.loader.renderman.RtColor;
import ca.eandb.jmist.framework.loader.renderman.RtMatrix;
import ca.eandb.jmist.framework.loader.renderman.RtPoint;
import ca.eandb.jmist.framework.loader.renderman.RtToken;

/**
 * @author Brad
 *
 */
public interface RtObject {
	
	double toFloat();
	int toInt();
	RtToken toToken();
	RtBound toBound();
	RtMatrix toMatrix();
	RtPoint toPoint();
	RtColor toColor();

}
