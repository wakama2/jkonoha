package test;

import static org.junit.Assert.*;
import jkonoha.*;

import org.junit.Test;

public class IntOperatorBoundaryOperation {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		int max = Integer.MAX_VALUE;
		int min = Integer.MIN_VALUE;
		k.eval(ctx, "int max = Integer.MAX_VALUE;");
		k.eval(ctx, "int min = Integer.MIN_VALUE;");
		assertEquals(max + min, ((KInt) k.eval(ctx, "max + min")).unbox());
		assertEquals(min + max, ((KInt) k.eval(ctx, "min + max")).unbox());
		assertEquals(min + min, ((KInt) k.eval(ctx, "min + min")).unbox());
		assertEquals(max - max, ((KInt) k.eval(ctx, "max - max")).unbox());
		assertEquals(max - min, ((KInt) k.eval(ctx, "max - min")).unbox());
		assertEquals(min - max, ((KInt) k.eval(ctx, "min - max")).unbox());
		assertEquals(min - min, ((KInt) k.eval(ctx, "min - min")).unbox());
		assertEquals(max / max, ((KInt) k.eval(ctx, "max / max")).unbox());
		assertEquals(max / min, ((KInt) k.eval(ctx, "max / min")).unbox());
		assertEquals(min / max, ((KInt) k.eval(ctx, "min / max")).unbox());
		assertEquals(min / min, ((KInt) k.eval(ctx, "min / min")).unbox());

	}

}
