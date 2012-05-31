package jkonoha;

import java.util.*;

class TEnv {
	
	public String source;
	public long uline;
	public List<Token> list;
	public int bol;   // begin of line
	public int indent_tab;
	public FTokenizer[] fmat;
	
	/**
	 * constructer
	 * sourcecode
	 * @param source
	 * which line
	 * @param uline
	 * tokens
	 * @param a
	 * indent
	 * @param indent_tab
	 * parser
	 * @param fmat
	 */
	
	TEnv(String source, long uline, List<Token> a, int indent_tab, KonohaSpace ks) {
		this.source = source;
		this.uline = uline;
		this.list = a;
		this.bol = 0;
		this.indent_tab = indent_tab;
		this.fmat = Tokenizer.MiniKonohaTokenMatrix();
	}
	
//	/**
//	 * This method is used to know the position of line.
//	 * @param pos
//	 * @return int
//	 */
//	
//	public final int lpos(int pos) {
//		return (this.bol == 0) ? -1 : (int)(pos - this.bol);
//	}
}