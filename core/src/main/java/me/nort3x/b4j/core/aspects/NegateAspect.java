package me.nort3x.b4j.core.aspects;

import me.nort3x.b4j.core.aspects.adaptors.UserAnalyzer;
import net.dv8tion.jda.api.events.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class NegateAspect {

    Logger log = LoggerFactory.getLogger(NegateAspect.class);
    @Around("@annotation(me.nort3x.b4j.core.aspects.annotation.NotSelf) && args(event,..)")
    public Object notBeingSelf(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        UserAnalyzer authorAnalysisEvent = new UserAnalyzer(event);
        if(!authorAnalysisEvent.isAuthorAware()){
            return joinPoint.proceed();
        }else if (!authorAnalysisEvent.getAuthor().equals(event.getJDA().getSelfUser())) {
            return joinPoint.proceed();
        }
        return null;
    }

    @Around("@annotation(me.nort3x.b4j.core.aspects.annotation.NotBot) && args(event,..) ")
    public Object notBeingBot(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        UserAnalyzer authorAnalysisEvent = new UserAnalyzer(event);
        if(!authorAnalysisEvent.isAuthorAware()){
            return joinPoint.proceed();
        }else if (!authorAnalysisEvent.getAuthor().isBot()) {
            return joinPoint.proceed();
        }
        return null;
    }
}
