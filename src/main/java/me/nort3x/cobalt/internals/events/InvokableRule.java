package me.nort3x.cobalt.internals.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokableRule {
    Method m;
    Object instance;
    Logger logger;
    public InvokableRule(Method m, Object instance) {
        this.m = m;
        this.instance = instance;
        this.logger = LoggerFactory.getLogger(m.getDeclaringClass());
    }

    void invoke(Object parameter) {
        try {
            m.invoke(instance, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.warn(
                    "Cannot Invoke Rule: " + m.getDeclaringClass().getName() + "." + m.getName() + " Thrown Exception: " ,e
            );
        }
    }
}