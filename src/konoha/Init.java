package konoha;

import jkonoha.*;
import jkonoha.ast.Syntax;

public class Init implements KonohaPackage {

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				new NewSyntax(),
				new ClassSyntax(),
				//TODO
		};
		ks.defineSyntax(ctx, syndef);
	}
	
}