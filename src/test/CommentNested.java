package test;

import static org.junit.Assert.*;
import jkonoha.CTX;
import jkonoha.KInt;
import jkonoha.Konoha;

import org.junit.Test;

public class CommentNested {

	@Test
	public void test() {
		CTX ctx = new CTX();
		Konoha k = new Konoha(ctx);
		KInt a = (KInt)k.eval(ctx, "/** this program is ... /** nested comment is allowed in konoha // **/");
		assertEquals(a.unbox(), "");
	}

}
