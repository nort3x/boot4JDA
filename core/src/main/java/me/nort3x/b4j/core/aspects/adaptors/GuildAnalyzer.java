package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public class GuildAnalyzer {
    Event event;

    public GuildAnalyzer(Event event) {
        this.event = event;
    }
    public boolean isSupported(){
        return event instanceof GenericMessageEvent || event instanceof GenericComponentInteractionCreateEvent;
    }
    public Guild getGuild(){
        if(event instanceof GenericMessageEvent)
            return ((GenericMessageEvent)event).getGuild();
        if(event instanceof GenericInteractionCreateEvent)
            return ((GenericInteractionCreateEvent)event).getGuild();

        throw new IllegalStateException("unsupported event type to extract guild");
    }
}
