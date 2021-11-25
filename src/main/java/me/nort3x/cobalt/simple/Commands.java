package me.nort3x.cobalt.simple;

import me.nort3x.cobalt.internals.processors.Command;
import me.nort3x.cobalt.internals.processors.CommandPool;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// bunch of pools each for a specific context
@CommandPool(forBot = {ABot.NAME, BBot.NAME})
public class Commands {

    Logger l = LoggerFactory.getLogger(Commands.class);

    @Command(forBot = {ABot.NAME})
    public void onSomethingCool(MessageReceivedEvent event) {
        l.info("fromA:" + event.getMessage().toString());
    }

    @Command
    void commandForBothBots(SlashCommandEvent event){
        // do something with it
    }

    @Command(forBot = {BBot.NAME})
    public void onSomethingCool2(MessageReceivedEvent event) {
        l.info("fromB:" + event.getMessage().toString());
    }
}
