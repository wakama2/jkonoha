package commons.sugar;

import commons.konoha2.kclass.*;

import sugar.*;
import sugar.token.codeParser.*;

/**
 * tenv_t in original konoha2  
 * @author okachin
 *
 */

public class TEnv {
	
	public String source;
	public int uline;
	public K_Array<K_Token> list;
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
	
	public TEnv(String source, int uline, K_Array<K_Token> a, int indent_tab, FTokenizer[] fmat) {
		this.source = source;
		this.uline = uline;
		this.list = a;
		this.bol = 0;
		this.indent_tab = indent_tab;
		this.fmat = fmat;
	}
	
	/**
	 * This method is used to know the position of line.
	 * @param pos
	 * @return int
	 */
	
	public final int lpos(int pos) {
		return (this.bol == 0) ? -1 : (int)(pos - this.bol);
	}
}