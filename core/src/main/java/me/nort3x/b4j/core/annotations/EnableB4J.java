package me.nort3x.b4j.core.annotations;


import me.nort3x.b4j.core.B4JContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Import({B4JContext.class})
@Configuration
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableB4J {
}
