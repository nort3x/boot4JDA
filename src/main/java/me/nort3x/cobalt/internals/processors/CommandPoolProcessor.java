package me.nort3x.cobalt.internals.processors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandPoolProcessor implements BeanPostProcessor {


    private static final Map<String, Set<Method>> bots_commands_map = new ConcurrentHashMap<>();

    static final private Logger logger = LoggerFactory.getLogger(CommandPoolProcessor.class);

    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        Object obj = BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        assert obj != null;
        if (obj.getClass().isAnnotationPresent(CommandPool.class)) {

            logger.warn("Discovered CommandPool: " + obj.getClass().getSimpleName());
            var commands = Arrays.stream(obj.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Command.class))
                    .peek(method -> method.setAccessible(true))
                    .flatMap(method -> decideWhichCommandForWhichBot(obj,method))
                    .peek(method -> logger.info("Discovered Command: " + method.getCommand().getName() + " for " + method.getName()))
                    .collect(
                            Collectors.groupingBy(
                                    BotCommandWithName::getName
                            )
                    );


            commands.forEach((botName, commandSet) -> bots_commands_map.computeIfAbsent(botName, o -> Collections.synchronizedSet(new HashSet<>()))
                    .addAll(commandSet.stream().map(BotCommandWithName::getCommand).toList()));
            changed();
        }

        return obj;
    }


    private static Stream<BotCommandWithName> decideWhichCommandForWhichBot(Object commandPoolObject,Method command){

        String[] forPools =  commandPoolObject.getClass().getAnnotation(CommandPool.class).forBot();
        String[] forCommand = command.getAnnotation(Command.class).forBot();

        if(Arrays.asList(forPools).contains("ALL")){
            return Arrays.stream(forCommand).map(forBot->new BotCommandWithName(forBot,command));
        }else{
            return Arrays.stream(forPools)
                    .filter(forBot-> Arrays.asList(forCommand).contains(forBot) || Arrays.asList(forCommand).contains("ALL"))
                    .map(forBot->new BotCommandWithName(forBot,command));
        }
    }

    public static synchronized void changed() {
        bot_listeners.forEach((name, set) -> {

            var commands = bots_commands_map.get(name);
            Set<Method> finalSet = new HashSet<>(bots_commands_map.computeIfAbsent("ALL", o -> new HashSet<>()));
            if (commands != null)
                finalSet.addAll(commands);
            set.forEach(watcher -> watcher.accept(finalSet));


        });
    }

    private static final Map<String, Set<Consumer<Set<Method>>>> bot_listeners = new ConcurrentHashMap<>();

    public static void observe(@Nullable String bot_name, Consumer<Set<Method>> commandChangeListener) {
        bot_listeners.computeIfAbsent(
                        bot_name == null ? "ALL" : bot_name,
                        o -> Collections.synchronizedSet(new HashSet<>()))
                .add(commandChangeListener);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class BotCommandWithName {
        String name;
        Method command;
    }


    public static void remove(String bot_name, Consumer<Set<Method>> commandChangeListener) {
        bot_listeners.computeIfAbsent(bot_name, o -> Collections.synchronizedSet(new HashSet<>()))
                .remove(commandChangeListener);
    }
}
