package me.nort3x.b4j.example.multi_bot;

import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandPool
public class MixedCommandPool {

    @Command(forBot = Names.BOT_1_NAME)
    void command4b1(MessageReceivedEvent e){
        // left empty
    }

    @Command(forBot = Names.BOT_2_NAME)
    void command4b2(MessageReceivedEvent e){
        // left empty
    }
}
