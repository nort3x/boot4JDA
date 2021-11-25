package me.nort3x.cobalt.simple;

import me.nort3x.cobalt.internals.bots.AbstractBot;
import me.nort3x.cobalt.internals.events.EventManager;
import me.nort3x.cobalt.internals.processors.CommandPoolProcessor;
import me.nort3x.cobalt.internals.processors.DiscordBot;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// once and for all
@DiscordBot
public class ABot extends AbstractBot {

    public static final String NAME = "ABot";
    @Autowired
    ApplicationContext context;
    @Value("${cobalt.token}") String token;

    @Override
    protected String provideToken() {
        return token;
    }

    @PostConstruct
    void construct(){
        CommandPoolProcessor.observe(getName(),new EventManager(provideJDA(),context));
    }

    @Override
    protected void configure(JDABuilder jdaBuilder) {
    }

    @Override
    public String getName() {
        return NAME;
    }
}
