package me.nort3x.b4j.core.aspects;

import me.nort3x.b4j.core.aspects.adaptors.TriggerEventAnalyzer;
import me.nort3x.b4j.core.aspects.annotation.Trigger;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
@Aspect
@Order(2)
public class TriggerAspect {

    Logger log = LoggerFactory.getLogger(TriggerAspect.class);

    @Around("@annotation(me.nort3x.b4j.core.annotations.Command) && args(event,..)")
    public Object parseTrigger(ProceedingJoinPoint pjp, Event event) throws Throwable {
        TriggerEventAnalyzer triggerEventAnalyzer = new TriggerEventAnalyzer(event);
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method innerMethod = ms.getMethod();
        if (!innerMethod.isAnnotationPresent(Trigger.class)) // nothing to trigger
            return pjp.proceed();

        Trigger t = innerMethod.getAnnotation(Trigger.class);


        if (!triggerEventAnalyzer.isSupported() || !triggerEventAnalyzer.canBeTriggeredBy(t)) return null; // filter out
        try {

            return pjp.proceed();

        } catch (Exception e) {
            if (t.reflectExceptionMessages())
                return new MessageBuilder().append(e.getMessage()).build();
        }
        return null; // when exceptions happens but no reflection of message is needed
    }




}
