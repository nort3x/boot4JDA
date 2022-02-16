package me.nort3x.b4j.core.configurations;

import me.nort3x.b4j.core.annotations.CommandPool;
import me.nort3x.b4j.core.aspects.annotation.Authorization;
import me.nort3x.b4j.core.aspects.annotation.Help;
import me.nort3x.b4j.core.aspects.annotation.Trigger;
import me.nort3x.b4j.core.bots.IBot;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

public interface HelpBannerGenerator {

    boolean supportHelpForBotName(IBot bot);

    Object generateHelpForBot(IBot bot, Set<DescribableMethod> methods);

    static public class DescribableMethod {
        Help helpAnnotation;
        Optional<Trigger> trigger;
        Optional<Authorization> authorization;
        Method method;


        public DescribableMethod(Method method){
            this.method = method;

            this.helpAnnotation =  method.getAnnotation(Help.class);

            this.authorization = method.isAnnotationPresent(Authorization.class) ?
                    Optional.of(method.getAnnotation(Authorization.class) ) : Optional.empty();

            this.trigger = method.isAnnotationPresent(Trigger.class) ?
                    Optional.of(method.getAnnotation(Trigger.class) ) : Optional.empty();
        }

        public Help getHelpAnnotation() {
            return helpAnnotation;
        }

        public Optional<Trigger> getTrigger() {
            return trigger;
        }

        public Optional<Authorization> getAuthorization() {
            return authorization;
        }

        public Method getMethod() {
            return method;
        }
    }
}
