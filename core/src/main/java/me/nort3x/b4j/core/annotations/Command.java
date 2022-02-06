package me.nort3x.b4j.core.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
@Inherited
public @interface Command {
    String[] forBot() default {"ALL"};
}
