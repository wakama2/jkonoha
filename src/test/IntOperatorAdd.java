package test;

import static org.junit.Assert.*;
import jkonoha.*;

import org.junit.Test;

public class IntOperatorAdd {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt i = (KInt)k.eval(ctx, "1+2");
		assertEquals(i.unbox(), 3);
	}

}
