/**
 * 
 */
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;

/**
 * A collection of common functions implementing the <code>Function1</code>
 * interface.
 *  
 * @author Brad Kimmel
 */
public final class Functions {
	
	public static final Function1 SIN = new Function1() {	
		private static final long serialVersionUID = -5243730437574248501L;
		public double evaluate(double x) {
			return Math.sin(x);		
		}
	};
	
	public static final Function1 COS = new Function1() {
		private static final long serialVersionUID = -861360078453347872L;
		public double evaluate(double x) {
			return Math.cos(x);
		}
	};
	
	public static final Function1 EXP = new Function1() {
		private static final long serialVersionUID = -5109332754391306969L;
		public double evaluate(double x) {
			return Math.exp(x);
		}
	};

}
