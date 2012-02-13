/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brad
 *
 */
public final class TokenFactory {

	public static final TokenFactory BUILTIN = new TokenFactory();
	
	private static final class Token implements RtToken {
		private final String name;
		public Token(String name) {
			this.name = name;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return name;
		}
	};
	
	private final Map<String, RtToken> tokens = new HashMap<String, RtToken>();
	
	public TokenFactory() {
	}
	
	public RtToken lookup(String name) {
		return tokens.get(name);
	}
	
	public RtToken create(String name) {
		RtToken token = lookup(name);
		if (token == null) {
			token = new Token(name);
			tokens.put(name, token);
		}
		return token;
	}
	
}
