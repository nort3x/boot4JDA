package me.nort3x.b4j.core.aspects;

import me.nort3x.b4j.core.aspects.adaptors.TriggerEventAnalyzer;
import me.nort3x.b4j.core.aspects.annotation.Trigger;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
@Order(4)
public class ExecuteAspect {

    Logger log = LoggerFactory.getLogger(ExecuteAspect.class);

    @Around("@annotation(me.nort3x.b4j.core.aspects.annotation.Trigger) && args(event,..)")
    public Object changeSignatureForTrigger(ProceedingJoinPoint proceedingJoinPoint, Event event) throws Throwable {
        Method m = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        TriggerEventAnalyzer triggerEventAnalyzer = new TriggerEventAnalyzer(event);
        try {

           return proceedingJoinPoint.proceed(getParameters(
                    event,
                    m.getAnnotation(Trigger.class),
                    m,
                    triggerEventAnalyzer
            ));

        } catch (Exception e) {
            if (m.getAnnotation(Trigger.class).reflectExceptionMessages())
                return new MessageBuilder().append(e.getMessage()).build();
        }
        return null; // when exceptions happens but no reflection of message is needed
    }


    private Object[] getParameters(Event e, Trigger t, Method m, TriggerEventAnalyzer triggerEventAnalyzer) {
        Object[] params = new Object[m.getParameterCount()];
        if (params.length == 0)
            throw new IllegalStateException("Trigger methods should take event + 0 or more parameters as input");

        List<Object> rawParams = Arrays.stream(triggerEventAnalyzer.getListOfParams(t))
                .filter(x -> !x.isBlank() && !x.isEmpty())
                .collect(Collectors.toList());

        if (rawParams.size() + 1 != params.length) throw new IllegalArgumentException("arguments count doesn't match");

        rawParams.add(0, e);

        Class<?>[] sigParams = m.getParameterTypes();

        for (int i = 1; i < sigParams.length; i++) {
            if (sigParams[i] == String.class)
                params[i] = rawParams.get(i);
            else if (sigParams[i] == Long.class)
                params[i] = Long.parseLong((String) rawParams.get(i));
            else if (sigParams[i] == Double.class)
                params[i] = Double.parseDouble((String) rawParams.get(i));
            else if (sigParams[i] == Integer.class)
                params[i] = Integer.parseInt((String) rawParams.get(i));
            else if (sigParams[i] == Float.class)
                params[i] = Float.parseFloat((String) rawParams.get(i));
            else
                throw new IllegalArgumentException("unsupported parameter type, supported types are: string, long, double , int, float and event itself");
        }

        params[0] = e;
        return params;

    }

}
