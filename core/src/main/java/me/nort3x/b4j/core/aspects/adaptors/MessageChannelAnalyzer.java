package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public class MessageChannelAnalyzer {
    Event event;

    public MessageChannelAnalyzer(Event event) {
        this.event = event;
    }

    public boolean isSupported(){
        GenericComponentInteractionCreateEvent m;
        return event instanceof GenericMessageEvent ||
                event instanceof GenericComponentInteractionCreateEvent;
    }

    public MessageChannel getChannel(){
        if(event instanceof GenericMessageEvent){
            return ((GenericMessageEvent)event).getChannel();
        }else if(event instanceof GenericInteractionCreateEvent){
            return ((GenericComponentInteractionCreateEvent)event).getChannel();
        }
        throw new IllegalStateException("calling this method with unsupported event");
    }
}
