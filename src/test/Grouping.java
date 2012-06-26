package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KBoolean;
import jkonoha.Konoha;

import org.junit.Test;

public class Grouping {
	
	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KBoolean a = (KBoolean)k.eval(ctx, "1 + 2 * 3 == 7");
		assertEquals(a.unbox(), true);
		KBoolean b = (KBoolean)k.eval(ctx, "(1+2)*3 == 9");
		assertEquals(b.unbox(), true);
		
	}

}
