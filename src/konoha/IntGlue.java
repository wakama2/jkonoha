package konoha;

import jkonoha.CTX;
import jkonoha.KonohaPackageInitializer;
import jkonoha.KonohaSpace;
import jkonoha.ast.OpSyntax;
import jkonoha.ast.Syntax;

public class IntGlue implements KonohaPackageInitializer {
	
	private final Syntax opLShiftSyntax = new OpSyntax("<<") {
		{
			this.op2 = "opLSHIFT";
			this.priority = 128;
		}
	};
	private final Syntax opRShiftSyntax = new OpSyntax(">>") {
		{
			this.op2 = "opRSHIFT";
			this.priority = 128;
		}
	};

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		Syntax[] syndef = {
				opLShiftSyntax,
				opRShiftSyntax,
		};
		ks.defineSyntax(ctx, syndef);	
	}

}
