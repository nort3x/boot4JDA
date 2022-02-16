package me.nort3x.b4j.core.aspects.adaptors;

import me.nort3x.b4j.core.aspects.annotation.Trigger;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class TriggerEventAnalyzer {
    Event event;

    public TriggerEventAnalyzer(Event event) {
        this.event = event;
    }

    public boolean isSupported() {
        return event instanceof SlashCommandEvent ||
                event instanceof MessageReceivedEvent ||
                event instanceof MessageReactionAddEvent;
    }

    public boolean canBeTriggeredBy(Trigger trigger) {
        if (event instanceof SlashCommandEvent) {
            return ((SlashCommandEvent) event).getCommandPath().startsWith(trigger.value());
        } else if (event instanceof MessageReceivedEvent) {
            return ((MessageReceivedEvent) event).getMessage().getContentRaw().startsWith(trigger.shebang() + trigger.value());
        } else if (event instanceof MessageReactionAddEvent) {
            return ((MessageReactionAddEvent) event).retrieveMessage().complete().getContentRaw().startsWith(trigger.shebang() + trigger.value());
        }
        throw new IllegalStateException("unsupported event type");
    }

    public String[] getListOfParams(Trigger trigger){
        if (event instanceof SlashCommandEvent) {
            return ((SlashCommandEvent) event).getCommandPath()
                    .replace(trigger.value(),"")
                    .split(trigger.delimiter());
        } else if (event instanceof MessageReceivedEvent) {
            return ((MessageReceivedEvent) event).getMessage().getContentRaw()
                    .replace(trigger.shebang() + trigger.value(),"")
                    .split(trigger.delimiter());
        } else if (event instanceof MessageReactionAddEvent) {
            return ((MessageReactionAddEvent) event).retrieveMessage().complete()
                    .getContentRaw()
                    .replace(trigger.shebang() + trigger.value(),"")
                    .split(trigger.delimiter());
        }
        throw new IllegalStateException("unsupported event type");
    }
}
