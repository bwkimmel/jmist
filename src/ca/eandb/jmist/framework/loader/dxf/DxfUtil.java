/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class DxfUtil {

	public static DxfReader createDxfReader(Reader reader) {
		return new AsciiDxfReader(reader);
	}
	
	public static Basis3 getBasisFromArbitraryAxis(Vector3 N) {
		Vector3 Wy = Vector3.J;
		Vector3 Wz = Vector3.K;
		Vector3 Ax;
		if (Math.abs(N.x()) < (1.0 / 64.0) && Math.abs(N.y()) < (1.0 / 64.0)) {
			Ax = Wy.cross(N);
		} else {
			Ax = Wz.cross(N);
		}
		return Basis3.fromWU(N, Ax);
	}
	
	public static void advanceToGroupCode(int groupCode, DxfReader dxf) {
		while (dxf.getCurrentElement().getGroupCode() != groupCode) {
			dxf.advance();
		}
	}
	
	public static void advanceToEntity(String name, DxfReader dxf) {
		while (true) {
			advanceToGroupCode(0, dxf);
			if (dxf.getCurrentElement().getStringValue().equals(name)) {
				break;
			}
			
			dxf.advance();
		}
	}
	
	public static void advanceToSection(String name, DxfReader dxf) {
		do {
			advanceToEntity("SECTION", dxf);
			advanceToGroupCode(2, dxf);
		} while (dxf.getCurrentElement().getStringValue() != name);
		dxf.advance();
	}
	
	public static Map<String, DxfElement> parseHeader(DxfReader dxf) {
		advanceToSection("HEADER", dxf);
		
		Map<String, DxfElement> header = new HashMap<String, DxfElement>();
		
		while (true) {
			DxfElement elem = dxf.getCurrentElement();
			
			if (elem.getGroupCode() == 0 && elem.getStringValue().equals("ENDSEC")) {
				break;
			} else if (elem.getGroupCode() == 9) {
				String var = elem.getStringValue();
				dxf.advance();
				header.put(var, dxf.getCurrentElement());
			}
			
			dxf.advance();
		}
		
		return header;
	}
	
}
