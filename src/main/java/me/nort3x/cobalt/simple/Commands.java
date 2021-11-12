package me.nort3x.cobalt.simple;

import me.nort3x.cobalt.internals.processors.Command;
import me.nort3x.cobalt.internals.processors.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// bunch of pools each for a specific context
@CommandPool
public class Commands {



    Logger l = LoggerFactory.getLogger(Commands.class);

    @Command
    public void onSomethingCool(MessageReceivedEvent event){
        l.info(event.getMessage().toString());
    }
}
