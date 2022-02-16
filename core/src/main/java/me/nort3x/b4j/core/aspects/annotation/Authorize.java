package me.nort3x.b4j.core.aspects.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.ANNOTATION_TYPE)
public @interface Authorize {
    String value() default "";
    AuthorizationDetailType authorizationDetailType() default AuthorizationDetailType.BY_NAME;
    AuthorizationPrinciple authorizationPrinciple() default AuthorizationPrinciple.USER;

}
