package me.nort3x.b4j.core.processors;


import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import me.nort3x.b4j.core.aspects.annotation.Help;
import me.nort3x.b4j.core.bots.IBot;
import me.nort3x.b4j.core.configurations.HelpBannerGenerator;
import me.nort3x.b4j.core.events.BasicListener;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class CommandPoolProcessor {


    private final Logger logger = LoggerFactory.getLogger(CommandPoolProcessor.class);
    private final Map<String, Set<CommandWithNameAndInstance>> bots_commands_map = new ConcurrentHashMap<>();
    private final Map<String, Set<HelpBannerGenerator.DescribableMethod>> bots_description = new ConcurrentHashMap<>();


    public CommandPoolProcessor(CommandPoolProvider provider, List<? extends IBot> bots) {
        analysis(provider.getCommandPools());
        bots.forEach(this::register);
    }

    Map<JDA, IBot> botJDARelation = new HashMap<>();

    private void register(IBot bot) {

        assert bot.provideJDA() != null : "provided JDA is Null";
        botJDARelation.put(bot.provideJDA(), bot);

        Set<CommandWithNameAndInstance> commandsToRegister = new HashSet<>(bots_commands_map.getOrDefault(bot.provideName(), new HashSet<>()));
        commandsToRegister.addAll(bots_commands_map.getOrDefault("ALL", new HashSet<>()));

        var listener = new BasicListener();
        commandsToRegister.forEach(method -> listener.addRule(method.getCommand(), method.getInstance()));
        bot.provideJDA().addEventListener(listener);
    }

    public Map<JDA, IBot> getBotJDARelation() {
        return botJDARelation;
    }

    private void analysis(Map<String, Object> commandPools) {

        commandPools.forEach((name, pool) -> {

            // just briefly introduce what had been discovered
            logger.info(
                    String.format(
                            "Discovered CommandPool: %s a.k.a %s",
                            pool.getClass().getSimpleName(),
                            name
                    )
            );

            getCommands(pool).forEach(
                    (botName, set_methods) -> {
                        var bot_map = bots_commands_map
                                .computeIfAbsent(botName, o -> ConcurrentHashMap.newKeySet());

                        bot_map.addAll(set_methods);

                        var describable = set_methods.stream()
                                .filter(x->x.command.isAnnotationPresent(Help.class))
                                .map(x->new HelpBannerGenerator.DescribableMethod(x.command))
                                .collect(Collectors.toSet());

                        bots_description.computeIfAbsent(botName,o->ConcurrentHashMap.newKeySet())
                                .addAll(describable);
                    }
            );
        });


    }

    private static boolean isProxy(Class<?> o){
        return o.getSimpleName().contains("$$EnhancerBySpringCGLIB") || o.getSimpleName().contains("$Proxy");
    }

    private Map<String, List<CommandWithNameAndInstance>> getCommands(Object commandPoolInstance) {

        Class<?> clazz = commandPoolInstance.getClass();
        while(isProxy(clazz)) {
            clazz = clazz.getSuperclass();
        }


        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Command.class))
                .peek(method -> method.setAccessible(true))
                .flatMap(method -> decideWhichCommandForWhichBot(commandPoolInstance, method))
                .peek(method -> logger.info("Discovered Command: " + method.getCommand().getName() + " for " + method.getName()))
                .collect(
                        Collectors.groupingBy(
                                CommandWithNameAndInstance::getName
                        )
                );
    }

    private static Stream<CommandWithNameAndInstance> decideWhichCommandForWhichBot(Object commandPoolObject, Method command) {

        Class<?> clazz = commandPoolObject.getClass();
        while(isProxy(clazz)) {
            clazz = clazz.getSuperclass();
        }

        String[] forPools = clazz.getAnnotation(CommandPool.class).forBot();
        String[] forCommand = command.getAnnotation(Command.class).forBot();

        if (Arrays.asList(forPools).contains("ALL")) {
            return Arrays.stream(forCommand).map(forBot -> new CommandWithNameAndInstance(forBot, command, commandPoolObject));
        } else {
            return Arrays.stream(forPools)
                    .filter(forBot -> Arrays.asList(forCommand).contains(forBot) || Arrays.asList(forCommand).contains("ALL"))
                    .map(forBot -> new CommandWithNameAndInstance(forBot, command, commandPoolObject));
        }
    }

    public Set<HelpBannerGenerator.DescribableMethod> getBotDescribeCommands(IBot bot) {
        Set<HelpBannerGenerator.DescribableMethod> botSpecificCommands = bots_description.getOrDefault(bot.provideName(),new HashSet<>());
        Set<HelpBannerGenerator.DescribableMethod> generalCommands = bots_description.getOrDefault("ALL",new HashSet<>());
        HashSet<HelpBannerGenerator.DescribableMethod> merge = new HashSet<HelpBannerGenerator.DescribableMethod>(generalCommands);
        merge.addAll(botSpecificCommands);
        return merge;
    }
}
