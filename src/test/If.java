package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KInt;
import jkonoha.Konoha;

import org.junit.Test;

public class If {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt a = (KInt)k.eval(ctx, "int f(int n) { if (n == 0) { return n; } } f(0);");
		assertEquals(a.unbox(), 0);
	}

}
