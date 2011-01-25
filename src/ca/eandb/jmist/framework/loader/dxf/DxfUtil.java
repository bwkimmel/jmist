/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

import java.io.Reader;

import ca.eandb.jmist.framework.SceneElement;

/**
 * @author Brad
 *
 */
public final class DxfUtil {

	public static DxfReader createDxfReader(Reader reader) {
		return new AsciiDxfReader(reader);
	}
	
	public static SceneElement createScene(Reader reader) {
		DxfReader dxf = createDxfReader(reader);
		DxfElement elem, next = null;
		String section = null;
		
		while (true) {
			
			elem = (next != null) ? next : dxf.getNextElement();
			if (elem == null) {
				break;
			}
			
			if (elem.getGroupCode() == 0) {
				String value = elem.getStringValue();
				if (value.equals("SECTION")) {
					elem = dxf.getNextElement();
					if (elem.getGroupCode() != 2) {
						throw new RuntimeException(String.format("Expected group code 2, got %d", elem.getGroupCode()));
					}
					section = elem.getStringValue();
				} else if (value.equals("ENDSEC")) {
				 	section = null;
				} else if (value.equals("EOF")) {
					break;
				} else if (value.equals("POLYLINE")) {
					
				} else if (value.equals("VERTEX")) {
					
				} else if (value.equals("MATERIAL")) {
					
				}
				
			}
			
		}
		
		
	}
	
}
