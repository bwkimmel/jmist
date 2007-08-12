/**
 * 
 */
package org.jmist.toolkit;

import java.util.Random;

/**
 * @author bkimmel
 * TODO: Add comments.
 */
public final class Permutation {
	
	public Permutation() {
		this.sequence = new int[0];
	}
	
	public Permutation(int[] sequence) {
		this.sequence = (int[]) sequence.clone();
	}
	
	private Permutation(int size) {
		this.sequence = new int[size];
	}
	
	public int at(int index) {
		return this.sequence[index];
	}
	
	public int size() {
		return this.sequence.length;
	}
	
	public void swap(int m, int n) {
		int temp = this.sequence[m];
		this.sequence[m] = this.sequence[n];
		this.sequence[n] = temp;
	}

	public Permutation inverse() {
		Permutation p = new Permutation(this.sequence.length);
		
		for (int i = 0; i < this.sequence.length; i++) {
			p.sequence[this.sequence[i]] = i;
		}
		
		return p;
	}
	
	public Permutation divide(Permutation other) throws Exception {
		return this.times(other.inverse());
	}
	
	public Permutation times(Permutation other) throws Exception {
		Permutation p;
		
		if (this.size() != other.size()) {
			throw new Exception("Exceptions must be of the same length.");
		}
		
		p = new Permutation(this.size());
		
		for (int i = 0; i < this.sequence.length; i++) {
			p.sequence[i] = this.sequence[other.sequence[i]];
		}
		
		return p;
	}
	
	public static Permutation getIdentity(int size) {
		Permutation p = new Permutation(size);
		
		for (int i = 0; i < size; i++) {
			p.sequence[i] = i;
		}
		
		return p;
	}
	
	public static Permutation getRandom(int size) {
		Permutation p = Permutation.getIdentity(size);
		Random rnd = new Random();
		int j, temp;
		
		for (int i = size - 1; i > 0; i--) {
			j = rnd.nextInt(i + 1);
			
			temp = p.sequence[i];
			p.sequence[i] = p.sequence[j];
			p.sequence[j] = temp;
		}
		
		return p;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Permutation(this.sequence);
	}
	
	private final int[] sequence;

}
