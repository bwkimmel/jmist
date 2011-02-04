/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * @author Brad
 *
 */
public final class RibTokenizer {
	
	private final PushbackReader reader;
	
	public RibTokenizer(Reader reader) {
		this.reader = new PushbackReader(reader);
	}
	
	public RibToken nextToken() throws IOException {
		
		while (true) {
			int c = reader.read();
			
			switch (c) {
			case -1: // eof
				return RibToken.eof();
				
			case '#': // comment
				do {
					c = reader.read();
				} while (c >= 0 && c != '\n');
				if (c == -1) {
					return RibToken.eof();
				}
				break;
				
			case '"': // string
				return parseString();
				
			case '[': // open array
				return RibToken.openArray();
				
			case ']': // close array
				return RibToken.closeArray();
				
			case ' ': // whitespace
			case '\t':
			case '\n':
				break;
				
			default:
				if (('0' <= c && c <= '9') || c == '+' || c == '-' || c == '.') {
					reader.unread(c);
					return parseNumber();
				} else { // identifier
					reader.unread(c);
					return parseIdentifier();
				}
			}
		}
		
	}
	
	private RibToken parseIdentifier() throws IOException {
	
		StringBuilder builder = new StringBuilder();
				
		while (true) {
			int c = reader.read();
			switch (c) {
			case '#': // comment
			case '"': // string
			case '[': // open array
			case ']': // close array
			case ' ': // whitespace
			case '\t':
			case '\n':
				reader.unread(c);
				/* fall through */
				
			case -1:
				return RibToken.identifier(builder.toString()); 
				
			default:
				builder.append((char) c);
			}
		}
		
	}
	
	private RibToken parseNumber() throws IOException {

		StringBuilder builder = new StringBuilder();
		boolean fp = false;
		
		while (true) {
			int c = reader.read();
						
			if (c == '.' || c == 'E' || c == 'e') {
				fp = true;
				builder.append((char) c);
			} else if (('0' <= c && c <= '9') || c == '+' || c == '-') {
				builder.append((char) c);
			} else {
				reader.unread(c);
				break;
			}
		}
		
		if (fp) {
			return RibToken.real(Double.parseDouble(builder.toString()));
		} else {
			return RibToken.integer(Integer.parseInt(builder.toString()));
		}
		
	}
	
	private RibToken parseString() throws IOException {
		
		StringBuilder builder = new StringBuilder();

		while (true) {
			int c = reader.read();
			switch (c) {
			case -1:
				throw new EOFException("Unexpected end of file in string literal");
				
			case '"': // close quote
				return RibToken.string(builder.toString());
				
			case '\\': // escape sequence
				switch (c) {
				case 'n': // linefeed (newline)
					builder.append('\n');
					break;
					
				case 'r': // carriage return
					builder.append('\r');
					break;
					
				case 't': // horizontal tab
					builder.append('\t');
					break;
					
				case 'b': // backspace
					builder.append('\b');
					break;
					
				case 'f': // form feed
					builder.append('\f');
					break;
					
				case '\\': // backslash
				case '"': // double quote
					builder.append((char) c);
					
				case '\n': // no character
					break;
					
				default:
					if ('0' <= c && c < '8') { // octal code
						int value = c - '0';
						for (int i = 0; i < 2; i++) {
							c = reader.read();
							if ('0' <= c && c < '8') {
								value = (value << 3) | (c - '0');
							} else {
								reader.unread(value);
								break;
							}
						}
						value = value & 0xff;
						builder.append((char) value);
					} else { // ignore the escape character
						reader.unread(c);
					}
				}
				break;
				
			default:
				builder.append((char) c);
			}
		}
		
	}

}
