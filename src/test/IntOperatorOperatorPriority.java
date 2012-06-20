package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KInt;
import jkonoha.Konoha;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntOperatorOperatorPriority {
	
	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt a = (KInt)k.eval(ctx, "1 + 2 - 3");
		assertEquals(a.unbox(), 0);
		KInt b= (KInt)k.eval(ctx, "1 + 2 * 3");
		assertEquals(b.unbox(), 7);
		KInt c = (KInt)k.eval(ctx, "1 + 2 / 3");
		assertEquals(c.unbox(), 1);
		KInt d = (KInt)k.eval(ctx, "1 + 2 % 3");
		assertEquals(d.unbox(), 3);
		KInt e = (KInt)k.eval(ctx, "1 - 2 + 3");
		assertEquals(e.unbox(), 2);
		KInt f = (KInt)k.eval(ctx, "1 - 2 * 3");
		assertEquals(f.unbox(), -5);
		KInt g = (KInt)k.eval(ctx, "1 - 2 / 3");
		assertEquals(g.unbox(), 1);
		KInt h = (KInt)k.eval(ctx, "1 - 2 % 3");
		assertEquals(h.unbox(), -1);
		KInt i = (KInt)k.eval(ctx, "1 * 2 + 3");
		assertEquals(i.unbox(), 5);
		KInt j = (KInt)k.eval(ctx, "1 * 2 - 3");
		assertEquals(j.unbox(), -1);
		KInt l = (KInt)k.eval(ctx, "1 * 2 / 3");
		assertEquals(l.unbox(), 0);
		KInt m = (KInt)k.eval(ctx, "1 * 2 % 3");
		assertEquals(m.unbox(), 2);
		KInt n = (KInt)k.eval(ctx, "1 / 2 + 3");
		assertEquals(n.unbox(), 3);
		KInt o = (KInt)k.eval(ctx, "1 / 2 - 3");
		assertEquals(o.unbox(), -3);
		KInt p = (KInt)k.eval(ctx, "1 / 2 * 3");
		assertEquals(p.unbox(), 0);
		KInt q = (KInt)k.eval(ctx, "1 / 2 % 3");
		assertEquals(q.unbox(), 0);
		KInt r = (KInt)k.eval(ctx, "1 % 2 + 3");
		assertEquals(r.unbox(), 4);
		KInt s = (KInt)k.eval(ctx, "1 % 2 - 3");
		assertEquals(s.unbox(), -2);
		KInt t = (KInt)k.eval(ctx, "1 % 2 * 3");
		assertEquals(t.unbox(), 3);
		KInt u = (KInt)k.eval(ctx, "1 % 2 / 3");
		assertEquals(u.unbox(), 0);
		
	}

}
