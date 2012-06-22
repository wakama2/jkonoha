package test;

import static org.junit.Assert.*;
import jkonoha.*;

import org.junit.Test;

public class FuncSucc {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		k.eval(ctx, "int succ (int n) { return n + 1; }");
		KInt a = (KInt)k.eval(ctx, "succ(1);");
		assertEquals(a.unbox(), 2);
	}

}
