package konoha;

import jkonoha.CTX;
import jkonoha.KClass;
import jkonoha.KonohaPackageInitializer;
import jkonoha.KonohaSpace;
import jkonoha.ast.Syntax;

public class FloatGlue implements KonohaPackageInitializer {

	private final Syntax floatSyntax = new Syntax("float") {
		{
			this.ty = KClass.floatClass;
		}
	};
	
	private final Syntax doubleSyntax = new Syntax("double") {
		{
			this.ty = KClass.floatClass;
		}
	};

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef= {
				floatSyntax,
				doubleSyntax
		};
		ks.defineSyntax(ctx, syndef);
	}

}
