package jkonoha;

public class Tokenizer {

	public static void tokenize(CTX ctx, TEnv tenv) {
		int ch, pos = 0;
		FTokenizer fmat[] = tenv.fmat;
		Token tk = new Token(tenv.uline);
		assert(tk.tt == TK.NONE);
		tk.uline = tenv.uline;
//		tk.lpos = tenv.lpos(0);
		pos = parseINDENT.parse(ctx, tk, tenv, pos, null);
		while(pos < tenv.source.length() && (ch = kchar(tenv.source, pos)) != 0) {
			if(tk.tt != TK.NONE) {
				tenv.list.add(tk);
				tk = new Token(tenv.uline);
				tk.uline = tenv.uline;
				//tk.lpos = tenv.lpos(pos);
			}
			int pos2 = fmat[ch].parse(ctx, tk, tenv, pos, null);
			assert pos2 > pos;
			pos = pos2;
		}
		if(tk.tt != TK.NONE) {
			tenv.list.add(tk);
		}
	}

	/**
	 * This method is used to reduce character's types to 41 types.
	 * 
	 * @param t imported sourcecode
	 * @param pos position of sourcecode
	 * @return character type
	 */
	
	public static int kchar(String t, int pos) {
		int ch = t.charAt(pos);
		return (ch < 0) ? _MULTI : cMatrix[ch];
	}	
	
	/**
	 * This method returns a matrix of parser about each character code.
	 * @return matrix of parser about each character code
	 */
	
	public static FTokenizer[] MiniKonohaTokenMatrix() {
		FTokenizer[] fmat = new FTokenizer[KCHAR_MAX];
		fmat[_NULL] = parseSKIP;
		fmat[_UNDEF] = parseSKIP;
		fmat[_DIGIT] = parseNUM;
		fmat[_UALPHA] = parseUSYMBOL;
		fmat[_LALPHA] = parseSYMBOL;
		fmat[_MULTI] = parseMSYMBOL;
		fmat[_NL] = parseNL;
		fmat[_TAB] = parseSKIP;
		fmat[_SP] = parseSKIP;
		fmat[_LPAR] = parseOP1;
		fmat[_RPAR] = parseOP1;
		fmat[_LSQ] = parseOP1;
		fmat[_RSQ] = parseOP1;
		fmat[_LBR] = parseBLOCK;
		fmat[_RBR] = parseOP1;
		fmat[_LT] = parseOP;
		fmat[_GT] = parseOP;
		fmat[_QUOTE] = parseUNDEF;
		fmat[_DQUOTE] = parseDQUOTE;
		fmat[_BKQUOTE] = parseDQUOTE;
		fmat[_OKIDOKI] = parseOP;
		fmat[_SHARP] = parseOP;
		fmat[_DOLLAR] = parseOP;
		fmat[_PER] = parseOP;
		fmat[_AND] = parseOP;
		fmat[_STAR] = parseOP;
		fmat[_PLUS] = parseOP;
		fmat[_COMMA] = parseOP1;
		fmat[_MINUS] = parseOP;
		fmat[_DOT] = parseOP;
		fmat[_SLASH] = parseSLASH;
		fmat[_COLON] = parseOP;
		fmat[_SEMICOLON] = parseOP1;
		fmat[_EQ] = parseOP;
		fmat[_QUESTION] = parseOP;
		fmat[_AT] = parseOP1;
		fmat[_VAR] = parseOP;
		fmat[_CHILDER] = parseOP;
		fmat[_BKSLASH] = parseUNDEF;
		fmat[_HAT] = parseOP;
		fmat[_UNDER] = parseSYMBOL;
		return fmat;
	}
	
	/* const */
	public static final int _NULL = 0;
	public static final int _UNDEF = 1;
	public static final int _DIGIT = 2;
	public static final int _UALPHA = 3;
	public static final int _LALPHA = 4;
	public static final int _MULTI = 5;
	public static final int _NL = 6;
	public static final int _TAB = 7;
	public static final int _SP = 8;
	public static final int _LPAR = 9;
	public static final int _RPAR = 10;
	public static final int _LSQ = 11;
	public static final int _RSQ = 12;
	public static final int _LBR = 13;
	public static final int _RBR = 14;
	public static final int _LT = 15;
	public static final int _GT = 16;
	public static final int _QUOTE = 17;
	public static final int _DQUOTE = 18;
	public static final int _BKQUOTE = 19;
	public static final int _OKIDOKI = 20;
	public static final int _SHARP = 21;
	public static final int _DOLLAR = 22;
	public static final int _PER = 23;
	public static final int _AND = 24;
	public static final int _STAR = 25;
	public static final int _PLUS = 26;
	public static final int _COMMA = 27;
	public static final int _MINUS = 28;
	public static final int _DOT = 29;
	public static final int _SLASH = 30;
	public static final int _COLON = 31;
	public static final int _SEMICOLON = 32;
	public static final int _EQ = 33;
	public static final int _QUESTION = 34;
	public static final int _AT = 35;
	public static final int _VAR = 36;
	public static final int _CHILDER = 37;
	public static final int _BKSLASH = 38;
	public static final int _HAT = 39;
	public static final int _UNDER = 40;
	public static final int KCHAR_MAX = 41;
	
	public static final char[] cMatrix = {
	0/*nul*/, 1/*soh*/, 1/*stx*/, 1/*etx*/, 1/*eot*/, 1/*enq*/, 1/*ack*/, 1/*bel*/,
	1/*bs*/,  _TAB/*ht*/, _NL/*nl*/, 1/*vt*/, 1/*np*/, 1/*cr*/, 1/*so*/, 1/*si*/,
	/*	020 dle  021 dc1  022 dc2  023 dc3  024 dc4  025 nak  026 syn  027 etb*/
	1, 1, 1, 1,     1, 1, 1, 1,
	/*	030 can  031 em   032 sub  033 esc  034 fs   035 gs   036 rs   037 us*/
	1, 1, 1, 1,     1, 1, 1, 1,
	/*040 sp   041  !   042  "   043  #   044  $   045  %   046  &   047  '*/
	_SP, _OKIDOKI, _DQUOTE, _SHARP, _DOLLAR, _PER, _AND, _QUOTE,
	/*050  (   051  )   052  *   053  +   054  ,   055  -   056  .   057  /*/
	_LPAR, _RPAR, _STAR, _PLUS, _COMMA, _MINUS, _DOT, _SLASH,
	/*060  0   061  1   062  2   063  3   064  4   065  5   066  6   067  7 */
	_DIGIT, _DIGIT, _DIGIT, _DIGIT,  _DIGIT, _DIGIT, _DIGIT, _DIGIT,
	/*	070  8   071  9   072  :   073  ;   074  <   075  =   076  >   077  ? */
	_DIGIT, _DIGIT, _COLON, _SEMICOLON, _LT, _EQ, _GT, _QUESTION,
	/*100  @   101  A   102  B   103  C   104  D   105  E   106  F   107  G */
	_AT, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA,
	/*110  H   111  I   112  J   113  K   114  L   115  M   116  N   117  O */
	_UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA,
	/*120  P   121  Q   122  R   123  S   124  T   125  U   126  V   127  W*/
	_UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA, _UALPHA,
	/*130  X   131  Y   132  Z   133  [   134  \   135  ]   136  ^   137  _*/
	_UALPHA, _UALPHA, _UALPHA, _LSQ, _BKSLASH, _RSQ, _HAT, _UNDER,
	/*140  `   141  a   142  b   143  c   144  d   145  e   146  f   147  g*/
	_BKQUOTE, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA,
	/*150  h   151  i   152  j   153  k   154  l   155  m   156  n   157  o*/
	_LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA,
	/*160  p   161  q   162  r   163  s   164  t   165  u   166  v   167  w*/
	_LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA, _LALPHA,
	/*170  x   171  y   172  z   173  {   174  |   175  }   176  ~   177 del*/
	_LALPHA, _LALPHA, _LALPHA, _LBR, _VAR, _RBR, _CHILDER, 1,
	};	
	
	public static final FTokenizer parseINDENT = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int pos, KMethod thunk) {
			int ch, c = 0;
//			while((ch = tenv.source.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= tenv.source.length()) break;
				if((ch = tenv.source.charAt(pos)) == 0) break;
				if(ch == '\t') { c += tenv.indent_tab; }
				else if(ch == ' ') { c += 1; }
				break;
			}
			if(tk != null/* TODO IS_NOTNULL(tk) */) {
				tk.tt = TK.INDENT;
//				tk.lpos = 0;		
			}
			return pos - 1;
		}
	};

	public static final FTokenizer parseNL = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int pos, KMethod thunk) {
			tenv.uline += 1;
			tenv.bol = pos + 1;
			return parseINDENT.parse(ctx, tk, tenv, pos + 1, thunk);
		}
	};

	public static final FTokenizer parseNUM = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start, dot = 0;
			String ts = tenv.source;
//			while((ch = ts.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= ts.length()) break;
				if((ch = ts.charAt(pos)) == 0) break;
				if(ch == '_') continue; // nothing
				if(ch == '.') {
					if(!Character.isDigit(ts.charAt(pos))) {
						break;
					}
					dot++;
					continue;
				}
				if((ch == 'e' || ch == 'E') && (ts.charAt(pos) == '+' || ts.charAt(pos) == '-')) {
					pos++;
					continue;
				}
				if(!Character.isLetterOrDigit(ch)) break;
			}
			if(tk != null /* IS_NOTNULL(tk) */) {
//				tk.text = new KString(ts.substring(tok_start, pos - 1));
				tk.text = ts.substring(tok_start, pos);
				tk.tt = (dot == 0) ? TK.INT : TK.FLOAT;
			}
//			return pos - 1;  // next
			return pos;  // next
		}
	};

	public static final FTokenizer parseSYMBOL = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start;
			String ts = tenv.source;
//			while((ch = ts.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= ts.length()) break;
				if((ch = ts.charAt(pos)) == 0) break;
				if(ch == '_' || Character.isLetterOrDigit(ch)) continue; // nothing
				break;
			}
			Token rtk = tk;
			if(rtk != null /* IS_NOTNULL(tk) */) {
				rtk.text = ts.substring(tok_start, pos);
				assert rtk.text != null;
				tk.tt = TK.SYMBOL;
			}
			return pos;  // next
//			return pos - 1;  // next
		}
	};

	public static final FTokenizer parseUSYMBOL = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start;
			String ts = tenv.source;
//			while((ch = ts.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= ts.length()) break;
				if((ch = ts.charAt(pos)) == 0) break;
				if(ch == '_' || Character.isLetterOrDigit(ch)) continue; // nothing
				break;
			}
			Token rtk = tk;
			if(rtk != null /* IS_NOTNULL(tk) */) {
//				tk.text = new KString(ts.substring(tok_start, pos - 1));
				rtk.text = ts.substring(tok_start, pos);
				rtk.tt = TK.USYMBOL;
			}
//			return pos - 1; // next
			return pos; // next
		}
	};

	public static final FTokenizer parseMSYMBOL = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start;
			String ts = tenv.source;
//			while((ch = ts.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= ts.length()) break;
				if((ch = ts.charAt(pos)) == 0) break;
				if(!(ch < 0)) break;
			}
			Token rtk = tk;
			if(rtk != null /* IS_NOTNULL(tk) */) {
//				tk.text = new KString(ts.substring(tok_start, pos - 1));
				rtk.text = ts.substring(tok_start, pos);
				tk.tt = TK.MSYMBOL;
			}
//			return pos - 1; // next
			return pos; // next
		}
	};

	public static final FTokenizer parseOP1 = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			Token rtk = tk;
			if(rtk != null /* IS_NOTNULL(tk) */) {
				String s = tenv.source.substring(tok_start);
				rtk.text = s.substring(0, 1);
				rtk.tt = TK.OPERATOR;
				rtk.topch = s.charAt(0);
			}
			return tok_start + 1;
		}
	};

	public static final FTokenizer parseOP = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start;
//			while((ch = tenv.source.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= tenv.source.length()) break;
				if((ch = tenv.source.charAt(pos)) == 0) break;
				if(Character.isLetter(ch)) break;
				switch(ch) {
				case '<': case '>': case '@': case '$': case '#':
				case '+': case '-': case '*': case '%': case '/':
				case '=': case '&': case '?': case ':': case '.':
				case '^': case '!': case '~': case '|':
					continue;
				}
				break;
			}
			Token rtk = tk;
			if(rtk != null /* IS_NOTNULL(tk) */) {
				String s = tenv.source.substring(tok_start);
//				tk.text = new KString(s.substring(0, (pos - 1) - tok_start));
				rtk.text = s.substring(0, pos - tok_start);
				rtk.tt = TK.OPERATOR;
				if(rtk.text.length() == 1) {
					rtk.topch = rtk.text.charAt(0);
				}
			}
//			return pos - 1;
			return pos;
		}
	};

	public static final FTokenizer parseLINE = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, pos = tok_start;
//			while((ch = tenv.source.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= tenv.source.length()) break;
				if((ch = tenv.source.charAt(pos)) == 0) break;
				if(ch == '\n') break;
			}
//			return pos - 1;/*EOF*/
			return pos;/*EOF*/
		}
	};

	public static final FTokenizer parseCOMMENT = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, prev = 0, level = 1, pos = tok_start + 2;
			/*@#nnnn is line number */
			if(tenv.source.charAt(pos) == '@' && tenv.source.charAt(pos + 1) == '#' && Character.isDigit(tenv.source.charAt(pos + 2))) {
				// TODO
				// tenv->uline >>= (sizeof(kshort_t)*8);
				// tenv->uline = (tenv->uline<<(sizeof(kshort_t)*8)) | (kshort_t)strtoll(tenv->source + pos + 2, NULL, 10);
			}
//			while((ch = tenv.source.charAt(pos++)) != 0) {
			while(true) {
				pos++;
				if(pos >= tenv.source.length()) break;
				if((ch = tenv.source.charAt(pos)) == 0) break;
				if(ch == '\n') {
					tenv.uline += 1;
				}
				if(prev == '*'  && ch == '/') {
					level--;
//					if(level == 0) return pos;
					if(level == 0) return pos + 1;
				} else if(prev == '/' && ch == '*') {
					level++;
				}
				prev = ch;
			}
			if(tk != null /* CTX.IS_NOTNULL(tk) */) {
				// TODO perror.h
				// size_t errref = SUGAR_P(ERR_, tk->uline, tk->lpos, "must close with */");
				// KToken.Token_toERR(ctx, tk, errref);
			}
//			return pos - 1;/*EOF*/
			return pos;/*EOF*/
		}
	};

	public static final FTokenizer parseSLASH = new FTokenizer() {
		@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			String ts = tenv.source.substring(tok_start);
			if(ts.length() < 2) return parseOP.parse(ctx, tk, tenv, tok_start, thunk);
			if(ts.charAt(1) == '/') {
				return parseLINE.parse(ctx, tk, tenv, tok_start, thunk);
			}
			if(ts.charAt(1) == '*') {
				return parseCOMMENT.parse(ctx, tk, tenv, tok_start, thunk);
			}
			return parseOP.parse(ctx, tk, tenv, tok_start, thunk);
		}	
	};

	public static final FTokenizer parseDQUOTE = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, prev = '"', pos = tok_start + 1;
//			while((ch = tenv.source.charAt(pos++)) != 0) {
			while(true) {
				if(pos >= tenv.source.length()) break;
				if((ch = tenv.source.charAt(pos++)) == 0) break;
				if(ch == '\n') {
					break;
				}
				if(ch == '"' && prev != '\\') {
					Token rtk = tk;
					if(rtk != null /* CTX.IS_NOTNULL(tk) */) {
//						tk.text = new KString(tenv.source.substring(tok_start + 1, pos - 1));
						rtk.text = tenv.source.substring(tok_start + 1, pos-1);
						rtk.tt = TK.TEXT;
					}
					return pos;
				}
				prev = ch;
			}
			if(tk != null /* CTX.IS_NOTNULL(tk) */) {
				// TODO perror.h
				// size_t errref = SUGAR_P(ERR_, tk->uline, tk->lpos, "must close with \"");
				// KToken.Token_toERR(ctx, tk, errref);
			}
//			return pos - 1;
			return pos;
		}
	};

	public static final FTokenizer parseSKIP = new FTokenizer() {
		@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			return tok_start + 1;
		}
	};

	public static final FTokenizer parseUNDEF = new FTokenizer() {
		@Override public int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			if(tk != null /* IS_NOTNULL(tk) */) {
				// TODO
				// size_t errref = SUGAR_P(ERR_, tk->uline, tk->lpos, "undefined token character: %c", tenv->source[tok_start]);
				// KToken.Token_toERR(ctx, tk, errref);
			}
//			while(tenv.source.charAt(++tok_start) != 0);
			while(true) {
				tok_start++;
				if(tok_start >= tenv.source.length()) break;
				if(tenv.source.charAt(tok_start) != 0) break;
			}
			return tok_start;
		}
	};

	public static final FTokenizer parseBLOCK = new FTokenizer() {
		@Override public final int parse(CTX ctx,  Token tk, TEnv tenv, int tok_start, KMethod thunk) {
			int ch, level = 1, pos = tok_start + 1;
			FTokenizer[] fmat = tenv.fmat;
//			tk.lpos += 1;
			while((ch = Tokenizer.kchar(tenv.source, pos)) != 0) {
				if(ch == Tokenizer._RBR/*}*/) {
					level--;
					if(level == 0) {
						Token rtk = tk;
						if(tk != null /* IS_NOTNULL(tk) */) {
							rtk.text = tenv.source.substring(tok_start + 1, pos);
							rtk.tt = TK.CODE;
						}
						return pos + 1;
					}
					pos++;
				}
				else if(ch == Tokenizer._LBR/*'{'*/) {
					level++; pos++;
				}
				else {
					pos = fmat[ch].parse(ctx, /* TODO K_NULLTOKEN*/null, tenv, pos, null);
				}
			}
			if(tk != null /* CTX.IS_NOTNULL(tk) */) {
				// TODO
				// size_t errref = SUGAR_P(ERR_, tk->uline, tk->lpos, "must close with }");
				// Token_toERR(_ctx, tk, errref);
			}
			return pos - 1;
		}	
	};
}

