package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import java.util.Optional;

public class RetrieveMessageFromEvent {
    Event event;

    public RetrieveMessageFromEvent(Event event) {
        this.event = event;
    }

    public boolean canRetrieveMessage() {
        return event instanceof GenericComponentInteractionCreateEvent ||
                event instanceof MessageUpdateEvent ||
                event instanceof MessageReceivedEvent;
    }

    public Message getMessage() throws IllegalStateException{
        if (event instanceof GenericComponentInteractionCreateEvent)
            return ((GenericComponentInteractionCreateEvent) event).getMessage();
        else if (event instanceof MessageUpdateEvent)
            return ((MessageUpdateEvent) event).getMessage();
        else if(event instanceof MessageReceivedEvent)
            return ((MessageReceivedEvent)event).getMessage();

        throw new IllegalStateException("call this method for only MessageRetrievable events");
    }
}
