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
			FileReader fr = new FileReader("C:\\Users\\Brad\\Downloads\\cornell.rib");
			RenderManContext context = new JMistRenderManContext();
			RibReader rib = new RibReader();
			
			rib.read(fr, context);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
