package konoha;

import jkonoha.*;

public class KonohaGlue implements KonohaPackageInitializer {

	@Override
	public void init(CTX ctx, KonohaSpace ks) {
		new ClassGlue().init(ctx, ks);
		new FloatGlue().init(ctx, ks);
		new ArrayGlue().init(ctx, ks);
		new WhileGlue().init(ctx, ks);
		new AssignmentGlue().init(ctx, ks);
		new IntGlue().init(ctx, ks);
		new FloatGlue().init(ctx, ks);
	}

}
