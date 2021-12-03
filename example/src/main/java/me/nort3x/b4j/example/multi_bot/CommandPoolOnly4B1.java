package me.nort3x.b4j.example.multi_bot;

import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandPool(forBot = Names.BOT_1_NAME)
public class CommandPoolOnly4B1 {

    @Command
    void onlyB1Command(MessageReceivedEvent me){
        if(!me.getAuthor().equals(me.getJDA().getSelfUser()))
            me.getMessage().reply("hello from b1").queue();
    }
}
