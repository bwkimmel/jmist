/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author Brad
 *
 */
public final class TestRibTokenizer {

	
	public static void main(String[] args) {
		try {
			FileReader fr = new FileReader("C:\\Users\\Brad\\Download\\UtahTeapot.rib");
			RibTokenizer tokenizer = new RibTokenizer(fr);
		
			RibToken token;
			do {
				token = tokenizer.nextToken();
				switch (token.type) {
				case RibToken.RT_IDENTIFIER:
					System.out.print("IDENT ");
					System.out.println(token.stringValue);
					break;
					
				case RibToken.RT_STRING:
					System.out.print("STRING ");
					System.out.println(token.stringValue);
					break;
					
				case RibToken.RT_INTEGER:
					System.out.print("INTEGER ");
					System.out.println(token.intValue);
					break;
					
				case RibToken.RT_REAL:
					System.out.print("REAL ");
					System.out.println(token.realValue);
					break;
					
				case RibToken.RT_OPEN_ARRAY:
					System.out.println("OPEN ARRAY");
					break;
	
				case RibToken.RT_CLOSE_ARRAY:
					System.out.println("CLOSE ARRAY");
					break;
	
				case RibToken.RT_EOF:
					System.out.println("EOF");
					break;
	
				}
			} while (token.type != RibToken.RT_EOF);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
