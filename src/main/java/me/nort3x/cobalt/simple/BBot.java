package me.nort3x.cobalt.simple;

import me.nort3x.cobalt.internals.bots.AbstractBot;
import me.nort3x.cobalt.internals.processors.DiscordBot;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;

// this bot is just a delegation for ABot, just a copy for demo
@DiscordBot
public class BBot extends AbstractBot {
    public static final String NAME = "BBot";
    @Autowired
    ABot aBot;

    @Override
    public String provideToken() {
        return aBot.provideToken();
    }


    @Override
    public void configure(JDABuilder jdaBuilder) {
        aBot.configure(jdaBuilder);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
