package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.*;

import org.junit.Test;

public class IntOperatorCmpMoreThan {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		int i1 = 1;
		int i2 = 2;
		assertEquals(1 > 2, ((KBoolean) k.eval(ctx, "1 > 2")).unbox());
		k.eval(ctx, "boolean f(){int i1 = 1; return 1 > i1;}");
		assertEquals(1 > i1, ((KBoolean) k.eval(ctx, "f()")).unbox());
		k.eval(ctx, "boolean f(){int i1 = 1; retrun i1 > 1;}");
		assertEquals(i1 > 1, ((KBoolean) k.eval(ctx, "f()")).unbox());
		k.eval(ctx, "boolean f(){int i1 = 1; int i2 = 2; retrun i1 > i2;}");
		assertEquals(i1 > i2, ((KBoolean) k.eval(ctx, "f()")).unbox());
	}
}