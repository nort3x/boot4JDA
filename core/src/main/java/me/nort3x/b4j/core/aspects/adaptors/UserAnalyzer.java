package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import java.util.Optional;

public class UserAnalyzer {
    Event event;

    public UserAnalyzer(Event event) {
        this.event = event;
    }

    public boolean isAuthorAware() {
        return event instanceof GenericInteractionCreateEvent ||
                event instanceof GenericMessageReactionEvent ||
                event instanceof MessageReceivedEvent ||
                event instanceof MessageUpdateEvent;
    }

    public User getAuthor() throws IllegalStateException {
        if (event instanceof GenericInteractionCreateEvent) {
            return ((GenericInteractionCreateEvent) event).getUser();
        } else if (event instanceof GenericMessageReactionEvent) {
            return ((GenericMessageReactionEvent) event).getUser();
        } else if (event instanceof MessageReceivedEvent) {
            return ((MessageReceivedEvent) event).getAuthor();
        } else if (event instanceof MessageUpdateEvent) {
            return ((MessageUpdateEvent) event).getAuthor();
        }
        throw new IllegalStateException("call this method only for author aware events");
    }


    public Optional<Member> getMember() throws IllegalStateException {
        if (event instanceof GenericInteractionCreateEvent) {
            return Optional.ofNullable(((GenericInteractionCreateEvent) event).getMember());
        } else if (event instanceof GenericMessageReactionEvent) {
            return Optional.ofNullable(((GenericMessageReactionEvent) event).getMember());
        } else if (event instanceof MessageReceivedEvent) {
            return Optional.ofNullable(((MessageReceivedEvent) event).getMember());
        } else if (event instanceof MessageUpdateEvent) {
            return Optional.ofNullable(((MessageUpdateEvent) event).getMember());
        }
        throw new IllegalStateException("call this method only for author aware events");
    }


    public Event getEvent() {
        return event;
    }

}
