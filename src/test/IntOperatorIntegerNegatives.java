package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KBoolean;
import jkonoha.Konoha;

import org.junit.Test;

public class IntOperatorIntegerNegatives {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KBoolean a = (KBoolean)k.eval(ctx, "(-1) == -1");
		assertEquals(a.unbox(), true);
	}

}
