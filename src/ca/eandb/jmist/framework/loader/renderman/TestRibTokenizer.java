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
			FileReader fr = new FileReader("C:\\Users\\Brad\\Downloads\\UtahTeapot.rib");
			RibTokenizer tokenizer = new RibTokenizer(fr);
		
			RibToken token;
			do {
				token = tokenizer.getCurrentToken();
				tokenizer.advance();
				switch (token.type) {
				case IDENTIFIER:
					System.out.printf("IDENT (%s)", token.stringValue);
					System.out.println();
					break;
					
				case STRING:
					System.out.printf("STRING (%s)", token.stringValue);
					System.out.println();
					break;
					
				case INTEGER:
					System.out.print("INTEGER ");
					System.out.println(token.intValue);
					break;
					
				case REAL:
					System.out.print("REAL ");
					System.out.println(token.realValue);
					break;
					
				case OPEN_ARRAY:
					System.out.println("OPEN ARRAY");
					break;
	
				case CLOSE_ARRAY:
					System.out.println("CLOSE ARRAY");
					break;
	
				case EOF:
					System.out.println("EOF");
					break;
	
				}
			} while (token.type != RibToken.Type.EOF);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
