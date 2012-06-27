package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.*;

import org.junit.Test;

public class IntOperatorCmpMoreThanEqual {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		int i1 = 1;
		int i2 = 2;
//		k.eval(ctx,  "int i1 = 1;");
//		k.eval(ctx,  "int i2 = 2;");
		assertEquals(1 >= 2, ((KBoolean) k.eval(ctx, "1 >= 2")).unbox());
//		assertEquals(1 >= i1, ((KBoolean) k.eval(ctx, "1 >= i1")).unbox());
//		assertEquals(i1 >= 1, ((KBoolean) k.eval(ctx, "i1 >= 1")).unbox());
//		assertEquals(i1 >= i2, ((KBoolean) k.eval(ctx, "i1 >= i2")).unbox());
	}

}