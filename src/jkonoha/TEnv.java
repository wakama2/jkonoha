package jkonoha;

import java.util.*;

public class TEnv {
	
	public String source;
	public long uline;
	public List<Token> list;
	public int bol;   // begin of line
	public int indent_tab;
	public FTokenizer[] fmat;
	
	public TEnv(String source, long uline, List<Token> a, int indent_tab, KonohaSpace ks) {
		this.source = source;
		this.uline = uline;
		this.list = a;
		this.bol = 0;
		this.indent_tab = indent_tab;
		this.fmat = Tokenizer.MiniKonohaTokenMatrix();
	}
	
}