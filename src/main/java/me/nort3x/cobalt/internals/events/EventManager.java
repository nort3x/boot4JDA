package me.nort3x.cobalt.internals.events;

import net.dv8tion.jda.api.JDA;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Consumer;

public class EventManager implements Consumer<Set<Method>> {

    JDA botInstance;
    BasicListener listener;
    ApplicationContext applicationContext;
    public EventManager(JDA botInstance, ApplicationContext applicationContext){
        this.botInstance = botInstance;
        this.applicationContext = applicationContext;
    }

    @Override
    public synchronized void accept(Set<Method> methods) {
        if(listener!=null){
            botInstance.removeEventListener(listener);
        }
        listener = new BasicListener();
        methods.forEach(method -> listener.addRule(
                method,
                applicationContext.getBean(method.getDeclaringClass())
        ));
        botInstance.addEventListener(listener);
    }
}
