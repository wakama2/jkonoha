package commons.sugar;

import java.util.ArrayList;

import commons.konoha2.KModShare;
import commons.konoha2.kclass.*;

public class KModSugar extends KModShare {//joseph:kmodsugar_t in original konoha2 (/include/konoha2/sugar.h)
	
	public KClass		cToken;
	public KClass		cExpr;
	public KClass		cStmt;
	public KClass		cBlock;
	public KClass		cKonohaSpace;
	public KClass		cGamma;
	public KClass		cTokenArray;
	
	public ArrayList<String>			keywordList;
	public KMap							keywordMapNN;
	public K_Array						packageList;
	public KMap							packageMapNO;
	public K_KonohaSpace				rootks;

	public K_Method		UndefinedParseExpr;
	public K_Method		UndefinedStmtTyCheck;
	public K_Method		UndefinedExprTyCheck;
	public K_Method		ParseExpr_Term;
	public K_Method		ParseExpr_Op;
	
	//export TODO
	/*kExpr* (*Expr_setConstValue)(CTX, kExpr *expr, ktype_t ty, kObject *o);
	kExpr* (*Expr_setNConstValue)(CTX, kExpr *expr, ktype_t ty, uintptr_t ndata);
	kExpr* (*Expr_setVariable)(CTX, kExpr *expr, kexpr_t build, ktype_t ty, intptr_t index, kGamma *gma);

	kToken* (*Stmt_token)(CTX, kStmt *stmt, keyword_t kw, kToken *def);
	kExpr* (*Stmt_expr)(CTX, kStmt *stmt, keyword_t kw, kExpr *def);
	const char* (*Stmt_text)(CTX, kStmt *stmt, keyword_t kw, const char *def);
	kBlock* (*Stmt_block)(CTX, kStmt *stmt, keyword_t kw, kBlock *def);

	kExpr*     (*Expr_tyCheckAt)(CTX, kExpr *, size_t, kGamma *, ktype_t, int);
	kbool_t    (*Stmt_tyCheckExpr)(CTX, kStmt*, ksymbol_t, kGamma *, ktype_t, int);
	kbool_t    (*Block_tyCheckAll)(CTX, kBlock *, kGamma *);
	kExpr *    (*Expr_tyCheckCallParams)(CTX, kExpr *, kMethod *, kGamma *, ktype_t);
	kExpr *    (*new_TypedMethodCall)(CTX, ktype_t ty, kMethod *mtd, kGamma *gma, int n, ...);
	void       (*Stmt_toExprCall)(CTX, kStmt *stmt, kMethod *mtd, int n, ...);

	size_t     (*p)(CTX, int pe, kline_t uline, int lpos, const char *fmt, ...);
	kline_t    (*Expr_uline)(CTX, kExpr *expr, int level);
	ksyntax_t* (*KonohaSpace_syntax)(CTX, kKonohaSpace *, ksymbol_t, int);
	void       (*KonohaSpace_defineSyntax)(CTX, kKonohaSpace *, KDEFINE_SYNTAX *);

	kbool_t    (*makeSyntaxRule)(CTX, kArray*, int, int, kArray *);
	kBlock*    (*new_Block)(CTX, kKonohaSpace *, kStmt *, kArray *, int, int, int);
	void       (*Block_insertAfter)(CTX, kBlock *bk, kStmt *target, kStmt *stmt);

	kExpr*     (*Stmt_newExpr2)(CTX, kStmt *stmt, kArray *tls, int s, int e);
	kExpr*     (*new_ConsExpr)(CTX, ksyntax_t *syn, int n, ...);
	kExpr *    (*Stmt_addExprParams)(CTX, kStmt *, kExpr *, kArray *tls, int s, int e, int allowEmpty);
	kExpr *    (*Expr_rightJoin)(CTX, kExpr *, kStmt *, kArray *, int, int, int);*/
}
