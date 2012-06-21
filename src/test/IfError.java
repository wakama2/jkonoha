package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KInt;
import jkonoha.Konoha;

import org.junit.Test;

public class IfError {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt a = (KInt)k.eval(ctx, "void f(int n) { if n == 1 { System.p(n); } } f(1);");
		assertEquals(a.unbox(), "Error");
	}

}
