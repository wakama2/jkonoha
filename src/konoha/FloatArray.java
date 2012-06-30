package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class FloatArray extends KObject {
	
	private float[] farray;
	private int capacity;
	
	public FloatArray(int size) {
		this.farray = new float[size];
		this.capacity = this.farray.length;
	}
	
	public static FloatArray FloatArray_new(int size) {
		return new FloatArray(size);
	}
	
	public float get(int n) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		return this.farray[n];
	}
	
	public void set(int n, float f) {
		if(n >= this.capacity) throw new ArrayIndexOutOfBoundsException();
		this.farray[n] = f;
	}
	
	public int getSize() {
		return this.capacity;
	}
	
	public void add(int f) {
		if(this.capacity == this.farray.length) {
			this.farray = Arrays.copyOf(this.farray, this.farray.length * 2);
		}
		this.farray[this.capacity] = f;
		this.capacity++;
	}
}