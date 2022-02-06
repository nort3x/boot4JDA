package me.nort3x.b4j.core.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Documented
@Inherited
public @interface CommandPool {
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * will assign each command for given bot (default ALL bots) unless configured for specific bot
     * by name
     * @return name of the bot to assign for
     */
    String[] forBot() default {"ALL"};
}
