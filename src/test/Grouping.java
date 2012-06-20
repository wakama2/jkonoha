package test;

import static org.junit.Assert.*;
import jkonoha.*;

import org.junit.Test;

public class Grouping {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt i = (KInt)k.eval(ctx, "1+2*3");
		assertEquals(i.unbox(), 7);
		i = (KInt)k.eval(ctx, "(1+2)*3");
		assertEquals(i.unbox(), 9);
	}

}
