package test;

import static org.junit.Assert.*;
import jkonoha.*;

import org.junit.Test;

public class LineControl {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KString a = (KString)k.eval (ctx, "System.p(\"line=14\");");
		assertEquals(a.unbox(), "line=14");
	}
}
