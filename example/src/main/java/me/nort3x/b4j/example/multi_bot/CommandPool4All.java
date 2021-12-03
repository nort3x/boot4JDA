package me.nort3x.b4j.example.multi_bot;

import me.nort3x.b4j.core.B4JContext;
import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@CommandPool
public class CommandPool4All {

    Logger logger;

    @Autowired
    B4JContext context;

    public CommandPool4All() {
        logger = LoggerFactory.getLogger(CommandPool4All.class);
    }

    @Command
    void everyOneLog(MessageReceivedEvent event) {
        logger.info(
                String.format(
                        "Bot: %s received: %s",
                        context.getBotFromJDA(event.getJDA()).provideName(),
                        event.getMessage().toString()
                )
        );

    }
}
