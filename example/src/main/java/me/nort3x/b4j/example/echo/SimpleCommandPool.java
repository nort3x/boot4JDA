package me.nort3x.b4j.example.echo;

import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandPool
public class SimpleCommandPool {

    @Command
    void simpleEchoCommand(MessageReceivedEvent me) {
        if(me.getAuthor().getIdLong() != me.getJDA().getSelfUser().getIdLong())
        me.getMessage().reply(me.getMessage().getContentRaw()).queue();
    }
}
