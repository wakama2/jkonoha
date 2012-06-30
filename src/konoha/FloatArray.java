package konoha;

import java.util.Arrays;
import jkonoha.KObject;

public class FloatArray extends KObject {
	
	float[] farray;
	
	FloatArray(int size) {
		this.farray = new float[size];
	}
	
	float get(int n) {
		return farray[n];
	}
	
	void set(int n, float f) {
		farray[n] = f;
	}
	
	int getSize(int n) {
		return farray.length;
	}
	
	void add(float f) {
		farray = Arrays.copyOf(farray, farray.length + 1);
		farray[farray.length - 1] = f;
	}
}
