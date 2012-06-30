package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class IntArray extends KObject {
	
	private int[] iarray;
	private int capacity;
	
	public IntArray(int size) {
		this.capacity = size;
		if(size < 10) size = 10;
		this.iarray = new int[size];
	}
	
	public static IntArray newArray(int size) {
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
	
	public int getSize() {
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