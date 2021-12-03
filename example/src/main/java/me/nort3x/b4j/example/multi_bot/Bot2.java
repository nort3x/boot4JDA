package me.nort3x.b4j.example.multi_bot;

import me.nort3x.b4j.core.annotations.DiscordBot;
import me.nort3x.b4j.core.bots.AbstractBot;
import net.dv8tion.jda.api.JDABuilder;

@DiscordBot
public class Bot2 extends AbstractBot {
    @Override
    protected String provideToken() {
        return "OTA4NjgyMTYwNjQxMjg2MTg1.YY5SNQ.OcaS0iseQjTy0urpeBrFA3Jln0s";
    }

    @Override
    protected void configure(JDABuilder jdaBuilder) {

    }

    @Override
    public String provideName() {
        return Names.BOT_2_NAME;
    }
}
