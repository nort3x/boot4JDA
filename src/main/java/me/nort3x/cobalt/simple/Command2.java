package me.nort3x.cobalt.simple;

import me.nort3x.cobalt.internals.processors.Command;
import me.nort3x.cobalt.internals.processors.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;

@CommandPool // for all bots
public class Command2 {

    @Autowired Commands commands; // randomly just ignore it , its a total side effect

    @Command(forBot = ABot.NAME)
    void doSomething(MessageReceivedEvent event){
        commands.onSomethingCool2(event);
    }


}
