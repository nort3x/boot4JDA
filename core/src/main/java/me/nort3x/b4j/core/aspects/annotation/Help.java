package me.nort3x.b4j.core.aspects.annotation;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Help {
    String description() default "";
    String[] parameters() default {};


}
