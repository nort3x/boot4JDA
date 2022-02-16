package me.nort3x.b4j.core.aspects;

import me.nort3x.b4j.core.aspects.adaptors.ResponseSender;
import net.dv8tion.jda.api.events.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Aspect
@Order(0)
public class IOAspect {

    Logger log = LoggerFactory.getLogger(IOAspect.class);

    @AfterReturning(pointcut = "@annotation(me.nort3x.b4j.core.annotations.Command) && args(event,..)" ,returning = "retVal", argNames = "event,retVal")
    public void response(Event event,Object retVal) throws Throwable {
        if(retVal == null) return;
        ResponseSender responseSender = new ResponseSender(event,retVal);
        responseSender.sendResponse();
    }

}
