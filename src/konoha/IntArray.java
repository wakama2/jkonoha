package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class IntArray extends KObject {
	
	private int[] iarray;
	private int capacity;
	
	public IntArray(int size) {
		this.iarray = new int[size];
		this.capacity = this.iarray.length;
	}
	
	public static IntArray IntArray_new(int size) {
		return new IntArray(size);
	}
	
	public int get(int n) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		return this.iarray[n];
	}
	
	public void set(int n, int i) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		this.iarray[n] = i;
	}
	
	public int getSize(int n) {
		return this.capacity;
	}
	
	public void add(int i) {
		if(this.capacity == this.iarray.length) {
			this.iarray = Arrays.copyOf(this.iarray, this.iarray.length * 2);
		}
		this.iarray[this.capacity] = i;
		this.capacity++;
	}
}