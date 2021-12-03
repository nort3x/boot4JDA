package me.nort3x.b4j.example.echo;

import me.nort3x.b4j.core.annotations.DiscordBot;
import me.nort3x.b4j.core.bots.AbstractBot;
import net.dv8tion.jda.api.JDABuilder;

@DiscordBot
public class EchoBot extends AbstractBot {
    @Override
    protected String provideToken() {
        return "OTA4NjgyMTYwNjQxMjg2MTg1.YY5SNQ.OcaS0iseQjTy0urpeBrFA3Jln0s";
    }

    @Override
    protected void configure(JDABuilder jdaBuilder) {
        // I don't need to further configure jda-builder
    }


    @Override
    public String provideName() {
        // it's better to read this from a constant, see multi-bot example to find out why
        return "name";
    }
}
