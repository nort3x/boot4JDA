package me.nort3x.cobalt.internals.processors;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface Command {
    String[] forBot() default {"ALL"};
}
