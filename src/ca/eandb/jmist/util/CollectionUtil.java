/**
 * 
 */
package ca.eandb.jmist.util;

import java.util.Collections;
import java.util.List;

/**
 * General purpose collections utility methods.
 * 
 * @author Brad Kimmel
 */
public final class CollectionUtil {
	
	/**
	 * Merges two consecutive sorted lists in place.
	 * The two sub-lists
	 * <code>list.get(0), ..., list.get(split-1)</code> and
	 * <code>list.get(split), ..., list.get(list.size() - 1)</code>
	 * must be sorted.  The results are undefined otherwise.
	 * @param list The <code>List</code> to sort.
	 * @param split The number of elements in the first sub-list.
	 */
	public static <T extends Comparable<? super T>> void merge(List<T> list, int split) {
		int n = list.size();
		if (0 < split && split < n) {
			merge(list, 0, split, n - 1, (int) Math.sqrt(split), 0);
		}
	}

	/**
	 * Merges two consecutive sorted lists in place.
	 * 
	 * <p>This implementation is based on:</p>
	 * 
	 * <p>J. Chen, "<a href="http://dx.doi.org/10.1016/j.ipl.2005.11.018">A
	 * simple algorithm for in-place merging</a>", Information Processing
	 * Letters 98:34-40, 2006.</p>.
	 * 
	 * This algorithm is a direct transcription of Fig. 3 on page 37 of this
	 * paper, plus the two modifications indicated in the last paragraph of
	 * Sec. 2 (page 38) to increase the efficiency using recursion and to
	 * allow the algorithm to "work correctly when y<sub>0</sub> - x<sub>0</sub>
	 * &lt; 2k".
	 * 
	 * @param A The <code>List</code> to be merged.
	 * @param x0 The index of the start of the first sub-list.
	 * @param y0 The index of the start of the second sub-list.
	 * @param yn The index of the end of the second sub-list.
	 * @param k The block size.
	 * @param depth The recursion depth.
	 */
	private static <T extends Comparable<? super T>> void merge(List<T> A, int x0, int y0, int yn, int k, int depth) {
		int f = (y0 - x0) % k;
		int x = (f == 0) ? (y0 - 2 * k) : (y0 - k - f);
		if (x < x0) { x = x0; }
		T t = A.get(x); A.set(x, A.get(x0));
		int z = x0, y = y0, b1 = x + 1, b2 = y0 - k;
		while ((y - z) > (2 * k)) {
			if (y > yn || A.get(x).compareTo(A.get(y)) <= 0) {
				A.set(z, A.get(x)); A.set(x, A.get(b1)); x++;
				if (((x - x0) % k) == f) {
					if (z < (x - k)) { b2 = x - k; }
					x = findNextXBlock(A, x0, z, y, k, f, b1, b2);
				}
			} else {
				A.set(z, A.get(y)); A.set(y, A.get(b1)); y++;
				if (((y - y0) % k) == 0) { b2 = y - k; }
			}
			z++; A.set(b1, A.get(z));
			if (z == x) { x = b1; }
			if (z == b2) { b2 = -1; }
			b1++;
			if (((b1 - x0) % k) == f) {
				if (b2 == -1) { b1 -= k; } else { b1 = b2; }
			}
		}
		A.set(z, t);
		if (depth > 0) {
			mergeBandY(A, z, y, yn);
		} else {
			Collections.sort(A.subList(z, y)); merge(A, z, y, yn, (int) Math.sqrt(k), 1);
		}
	}
	
	/**
	 * Auxilliary method required for merging two consecutive sorted lists in
	 * place.
	 * 
	 * <p>This implementation is based on:</p>
	 * 
	 * <p>J. Chen, "<a href="http://dx.doi.org/10.1016/j.ipl.2005.11.018">A
	 * simple algorithm for in-place merging</a>", Information Processing
	 * Letters 98:34-40, 2006.</p>.
	 * 
	 * This method is a direct transcription of Fig. 4.
	 */
	private static <T extends Comparable<? super T>> int findNextXBlock(List<T> A,
			int x0, int z, int y, int k, int f, int b1, int b2) {
		T min1 = null, min2 = null; int m = (int) Math.floor(((z - x0 - f) / (double) k)) * k + f + x0;
		if (m <= z) { m += k; }
		int i = m;
		int j, x = i;
		while ((i + k) <= y) {
			if ((i != b1) && (i != b2)) {
				if ((i < b1) && (b1 < i + k)) { j = m - 1; } else { j = i + k - 1; }
				if (min1 == null || (A.get(i).compareTo(min1) <= 0 && A.get(j).compareTo(min2) <= 0)) {
					x = i; min1 = A.get(i); min2 = A.get(j);
				}
			}
			i += k;
		}
		return x;
	}
	
	/**
	 * Auxilliary method required for merging two consecutive sorted lists in
	 * place.
	 * 
	 * <p>This implementation is based on:</p>
	 * 
	 * <p>J. Chen, "<a href="http://dx.doi.org/10.1016/j.ipl.2005.11.018">A
	 * simple algorithm for in-place merging</a>", Information Processing
	 * Letters 98:34-40, 2006.</p>.
	 * 
	 * This method is a direct transcription of Fig. 5.
	 */
	private static <T extends Comparable<? super T>> void mergeBandY(List<T> A, int z, int y, int yn) {
		while (z < y && y <= yn) {
			int j = z + indexOfMin(A.subList(z, y));
			if (A.get(j).compareTo(A.get(y)) <= 0) { Collections.swap(A, z, j); }
			else { Collections.swap(A, z, y); y++; }
			z++;
		}
		if (z < y) { Collections.sort(A.subList(z, yn + 1)); }
	}
	
	/**
	 * Finds the first index of a minimal element in a list.
	 * @param list The <code>List</code> to search.
	 * @return The first index of a minimal element in the list.
	 */
	public static <T extends Comparable<? super T>> int indexOfMin(List<T> list) {
		T min = list.get(0);
		int index = 0;
		for (int i = 0, n = list.size(); i < n; i++) {
			T value = list.get(i);
			if (value.compareTo(min) < 0) {
				min = value;
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Finds the first index of a maximal element in a list.
	 * @param list The <code>List</code> to search.
	 * @return The first index of a maximal element in the list.
	 */
	public static <T extends Comparable<? super T>> int indexOfMax(List<T> list) {
		T min = list.get(0);
		int index = 0;
		for (int i = 0, n = list.size(); i < n; i++) {
			T value = list.get(i);
			if (value.compareTo(min) > 0) {
				min = value;
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Determines if the given list is sorted (in ascending order).
	 * @param list The <code>List</code> to examine.
	 * @return A value indciating whether the list is sorted in ascending
	 * 		order.
	 */
	public static <T extends Comparable<? super T>> boolean isSorted(List<T> list) {
		for (int i = 1, n = list.size(); i < n; i++) {
			if (list.get(i - 1).compareTo(list.get(i)) > 0) {
				return false;
			}
		}
		return true;
	}
	
	/** Default constructor. */
	private CollectionUtil() {}
	
}
