package me.nort3x.b4j.core.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * as a simple tuple can be used for storing a method with an instance of object providing it
 * @see BasicListener#addRule(Method, Object)
 */
public class InvokableRule {
    Method m;
    Object instance;
    Logger logger;
    public InvokableRule(Method m, Object instance) {
        this.m = m;
        this.instance = instance;
        this.logger = LoggerFactory.getLogger(m.getDeclaringClass());
    }

   public void invoke(Object parameter) {
        try {
            m.invoke(instance, parameter);
        } catch (Exception e) {
            logger.warn(
                    "Cannot Invoke Rule: " + m.getDeclaringClass().getName() + "." + m.getName() + " Thrown Exception: " ,e
            );
        }
    }
}