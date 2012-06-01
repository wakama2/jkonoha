package jkonoha;

import java.util.*;

class ModShare {

}

public class ModSugar extends ModShare {
	public KClass cToken;
	public KClass cExpr;
	public KClass cStmt;
	public KClass cBlock;
	public KClass cKonohaSpace;
	public KClass cGamma;
	public KClass cTokenArray;

	public List<String> keywordList;
	public Map<String, Syntax> keywordMapNN;
	public List<String> packageList;
	public Map<String, Syntax> packageMapNO;
	KonohaSpace rootks;

	KMethod UndefinedParseExpr;
	KMethod UndefinedStmtTyCheck;
	KMethod UndefinedExprTyCheck;
	KMethod ParseExpr_Term;
	KMethod ParseExpr_Op;
}
