package me.nort3x.b4j.core.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface DiscordBot {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
