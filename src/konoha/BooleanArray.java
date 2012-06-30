package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class BooleanArray extends KObject {
	
	private boolean[] barray;
	private int capacity;
	
	public BooleanArray(int size) {
		this.barray = new boolean[size];
		this.capacity = this.barray.length;
	}
	
	public static BooleanArray BooleanArray_new(int size) {
		return new BooleanArray(size);
	}
	
	public boolean get(int n) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		return this.barray[n];
	}
	
	public void set(int n, boolean b) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		this.barray[n] = b;
	}
	
	public int getSize() {
		return this.capacity;
	}
	
	public void add(boolean b) {
		if(this.capacity == this.barray.length) {
			this.barray = Arrays.copyOf(this.barray, this.barray.length * 2);
		}
		this.barray[this.capacity] = b;
		this.capacity++;
	}
}