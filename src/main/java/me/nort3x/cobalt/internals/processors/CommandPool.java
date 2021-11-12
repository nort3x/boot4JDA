package me.nort3x.cobalt.internals.processors;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Documented
public @interface CommandPool {
    @AliasFor(annotation = Component.class)
    String value() default "";

    String forBot() default "ALL";
}
