package test.tokenize;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import jkonoha.*;
import jkonoha.ast.Token;
import jkonoha.ast.Tokenizer;

public class TokenizerTest {
	
	private void testTokenize(String script, String...tk) {
		CTX ctx = new CTX();
		TEnv tenv = new TEnv(script, 0, new ArrayList<Token>(), 0, null);
		Tokenizer.tokenize(ctx, tenv);
		List<Token> tls = tenv.list;
		assertEquals(tls.size(), tk.length);
		for(int i=0; i<tk.length; i++) {
			//System.out.println(tls.get(i).text);
			assertEquals(tls.get(i).text, tk[i]);
		}
	}

	@Test
	public void testTokenize() {
		testTokenize("\"if\" \"(\" $expr \")\" $block", null ,"if", "(", "$", "expr", ")", "$", "block");
		testTokenize("123 + 456", null, "123", "+", "456");
		testTokenize("\t\t  123", null, "123");
		testTokenize("\"hello world\"", null, "hello world");
		testTokenize("func(10, 1);", null, "func", "(", "10", ",", "1", ")", ";");
	}

}
