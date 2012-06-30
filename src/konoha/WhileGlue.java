package konoha;

import jkonoha.*;
import jkonoha.ast.*;

public class WhileGlue implements KonohaPackageInitializer {

	private final Syntax whileSyntax = new Syntax("while") {
		{
			this.flag = (SYNFLAG.StmtJumpAhead|SYNFLAG.StmtJumpSkip);
			this.rule = "\"while\" \"(\" $expr \")\" $block";
		}

		@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			ctx.DBG_P("while statement .. ");
			boolean ret = false;
			if(stmt.tyCheckExpr(ctx, KW.Expr, gamma, KClass.booleanClass, 0)) {
				Block bk = stmt.block(ctx, KW.Block, null);
				ret = bk.tyCheckAll(ctx, gamma);
				stmt.typed(TSTMT.LOOP);
			}
			return ret;
		}

	};

	private final Syntax breakSyntax = new Syntax("break") {
		{
			this.rule = "\"break\" [ $USYMBOL ]";
		}
		@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			Stmt p = stmt;
			while((p = p.parentNULL.parentNULL) != null) {
				if((p.syntax.flag & SYNFLAG.StmtJumpSkip) == SYNFLAG.StmtJumpSkip) {
					stmt.setObject(stmt.syntax.kw, p);
					stmt.typed(TSTMT.JUMP);
					return true;
				}
			}
			ctx.SUGAR_P(System.err, 0, 0, "break statement not within a loop");
			return false;
		}

	};

	private final Syntax continueSyntax = new Syntax("continue") {
		{
			this.rule = "\"continue\" [ $USYMBOL ]";
		}

		@Override public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			Stmt p = stmt;
			while((p = p.parentNULL.parentNULL) != null) {
				if((p.syntax.flag & SYNFLAG.StmtJumpSkip) == SYNFLAG.StmtJumpSkip) {
					stmt.setObject(stmt.syntax.kw, p);
					stmt.typed(TSTMT.JUMP);
					return true;
				}
			}
			ctx.SUGAR_P(System.err, 0, 0, "continue statement not within a loop");
			return false;
		}

	};

	private final Syntax forSyntax = new Syntax("for") {
		{
			this.rule = "\"for\" \"(\" var: $block \";\" $expr \";\" each: $block \")\" $block";
		}

		@Override
		public boolean stmtTyCheck(CTX ctx, Stmt stmt, Gamma gamma) {
			ctx.DBG_P("for statement .. ");
			boolean ret = false;
			if(stmt.tyCheckExpr(ctx, KW.Expr, gamma, KClass.booleanClass, 0)) {
				Block bk = stmt.block(ctx, KW.Block, null);
				ret = bk.tyCheckAll(ctx, gamma);
				stmt.typed(TSTMT.LOOP);
			}
			return ret;
		}

	};

	// --------------------------------------------------------------------------

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				whileSyntax,
				breakSyntax,
				continueSyntax,
				forSyntax,
		};
		ks.defineSyntax(ctx, syndef);
	}
}