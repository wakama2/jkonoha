package jkonoha;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface KonohaPackageAnnotation {

	Class<? extends KonohaPackage> getInitClass();
	
}
