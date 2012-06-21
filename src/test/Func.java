package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KInt;
import jkonoha.Konoha;

import org.junit.Test;

public class Func {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt a = (KInt)k.eval(ctx, "int succ (int n) { return n + 1; } int f() {Func[int, int] g = succ; return g(1);} f();");
		assertEquals(a.unbox(), 2);
	}
}
