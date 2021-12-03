package me.nort3x.b4j.core.processors;

import me.nort3x.b4j.core.annotations.CommandPool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CommandPoolProvider {
    Map<String,Object> commandPools;

    public CommandPoolProvider(ApplicationContext context){
        commandPools = context.getBeansWithAnnotation(CommandPool.class);
    }

    public Map<String, Object> getCommandPools() {
        return commandPools;
    }
}
