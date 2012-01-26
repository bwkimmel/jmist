/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.Serializable;

/**
 * @author brad
 *
 */
public final class Half implements Comparable<Half>, Serializable {
	
	public static final int MAX_EXPONENT = 15;
	
	public static final Half MAX_VALUE = new Half((short) 0x7bff);
	
	public static final int MIN_EXPONENT = -14;
	
	public static final Half MIN_NORMAL = new Half((short) 0x0400);
	
	public static final Half MIN_VALUE = new Half((short) 0x0001);
	
	public static final Half NaN = new Half((short) 0x7e00);
	
	public static final Half NEGATIVE_INFINITY = new Half((short) 0xfc00);
	
	public static final Half POSITIVE_INFINITY = new Half((short) 0x7c00);
	
	public static final Half ZERO = new Half((short) 0x0000);
	
	public static final Half ONE = new Half((short) 0x3c00);
	
	public static final int SIZE = 16;
	
	private static final short SIGN_MASK = (short) 0x8000;
	
	private static final short EXPONENT_MASK = (short) 0x7c00;
	
	private static final short FRACTION_MASK = (short) 0x03ff;
	
	private static final int EXPONENT_SHIFT = 10;
	
	private final short bits;
	
	private Half(short bits) {
		this.bits = bits;
	}
	
	public Half() {
		this((short) 0);
	}
	
	public Half(double value) {
		
	}
	
	public Half(float value) {
		
	}
	
	public Half(String s) throws NumberFormatException {
		
	}
	
	public byte byteValue() {
		
	}
	
	public static int compare(Half h1, Half h2) {
		if (h1.isNaN() || h2.isNaN()) {
			return 0;
		}
		if ((h1.bits & ~SIGN_MASK) == 0 && (h2.bits & ~SIGN_MASK) == 0) {
			return 0;
		}
		if (h1.bits == h2.bits) {
			return 0;
		} else if (h1.bits < h2.bits) {
			return -1;
		} else {
			return 1;
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Half other) {
		return compare(this, other);
	}
	
	public double doubleValue() {
		if (isNaN()) {
			return Double.NaN;
		} else if (isInfinite()) {
			return (bits & SIGN_MASK) == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
		} else {
			long fractionBits = (long) (bits & FRACTION_MASK);
			long exponentBits = ((((long) (bits & EXPONENT_MASK)) >> 10) + 1008) << 52;
			long signBit = ((long) (bits & SIGN_MASK)) << 48;
			return Double.longBitsToDouble(signBit | exponentBits | fractionBits);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Half && equals((Half) obj);
	}
	
	public boolean equals(Half other) {
		if (isNaN() || other.isNaN()) {
			return false;
		}
		if ((bits & ~SIGN_MASK) == 0 && (other.bits & ~SIGN_MASK) == 0) {
			return true;
		}
		return bits == other.bits;
	}

	public float floatValue() {
		if (isNaN()) {
			return Float.NaN;
		} else if (isInfinite()) {
			return (bits & SIGN_MASK) == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
		} else {
			int fractionBits = (int) (bits & FRACTION_MASK);
			int exponentBits = ((((int) (bits & EXPONENT_MASK)) >> 10) + 112) << 23;
			int signBit = ((int) (bits & SIGN_MASK)) << 16;
			return Float.intBitsToFloat(signBit | exponentBits | fractionBits);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return isNaN() ? super.hashCode() : (new Short(bits)).hashCode();
	}
	
	public int intValue() {
		
	}
	
	public boolean isInfinite() {
		return (bits & EXPONENT_MASK) == EXPONENT_MASK && (bits & FRACTION_MASK) == 0;
	}
	
	public boolean isNaN() {
		return (bits & EXPONENT_MASK) == EXPONENT_MASK && (bits & FRACTION_MASK) != 0;
	}
	
	public long longValue() {
		
	}
	
	public short shortValue() {
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (isNaN()) {
			return "NaN";
		}
		
		StringBuilder s = new StringBuilder();
		if ((bits & SIGN_MASK) != 0) {
			s.append('-');
		}
		if (isInfinite()) {
			s.append("Infinity");
		} else if ((bits & ~SIGN_MASK) == 0) {
			s.append("0.0");
		} else {
			
		}
		
		return s.toString();
	}
	
	public short toShortBits() {
		return bits;
	}
	
	public static Half fromShortBits(short bits) {
		return new Half(bits);
	}
	
	public static Half add(Half a, Half b) {
		
	}
	
	public static Half sub(Half a, Half b) {
		
	}
	
	public static Half mul(Half a, Half b) {
		
	}
	
	public static Half div(Half a, Half b) {
		
	}
	
	public static Half sqrt(Half a) {
		
	}
	
	public static Half rem(Half a, Half b) {
		
	}
	
	public static Half pow(Half a, Half b) {
		
	}
	
	public static Half abs(Half a) {
		return (a.bits & SIGN_MASK) != 0 ? new Half(a.bits & ~SIGN_MASK) : a;
	}
	
	public static Half negate(Half a) {
		return new Half(a.bits ^ SIGN_MASK);
	}
	
	public static Half valueOf(double a) {
		
	}
	
	public static Half valueOf(float a) {
		
	}
	
}
