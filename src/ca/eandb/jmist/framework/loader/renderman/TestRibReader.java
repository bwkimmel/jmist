/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.FileReader;

/**
 * @author Brad
 *
 */
public final class TestRibReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("/home/brad/Documents/Models/cornell.rib");
			RenderManContext context = new AsciiBytestreamRenderManContext(System.out);
			RibReader rib = new RibReader();
			
			rib.read(fr, context);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
