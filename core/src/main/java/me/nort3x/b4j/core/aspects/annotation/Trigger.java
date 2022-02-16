package me.nort3x.b4j.core.aspects.annotation;


import me.nort3x.b4j.core.annotations.Command;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trigger {
    String value() default "";
    String shebang() default "!";
    String delimiter() default " ";
    boolean reflectExceptionMessages() default true;

}
