package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;

import java.io.File;
import java.io.InputStream;

public class Response {
    Object retValue;

    public Response(Object retValue) {
        this.retValue = retValue;
    }

    public boolean isSupported() {
        return retValue instanceof Message ||
                retValue instanceof EmojiResponse ||
                retValue instanceof MessageEmbed ||
                retValue instanceof FileResponse;
    }

    public void replayResponse(Message message) {
        if (retValue instanceof Message) {
            message.reply((Message) retValue).queue();
            return;
        } else if (retValue instanceof EmojiResponse) {
            EmojiResponse emojiResponse = (EmojiResponse) retValue;
            if (emojiResponse.isEmote())
                message.addReaction((Emote) emojiResponse.getEmoteOrEmoji()).queue();
            else
                message.addReaction((String) emojiResponse.getEmoteOrEmoji()).queue();
            return;
        } else if (retValue instanceof MessageEmbed) {
            message.replyEmbeds((MessageEmbed) retValue).queue();
            return;
        } else if (retValue instanceof FileResponse) {
            FileResponse response = (FileResponse) retValue;
            message.reply(response.getInputStream(), response.getName(), response.getAttachmentOptions()).queue();
            return;
        }
        throw new IllegalStateException("response type not supported");
    }

    public Object getRetValue() {
        return retValue;
    }

    public void sendResponse(MessageChannel channel, Event event) {
        if (retValue instanceof Message) {
            channel.sendMessage((Message) retValue).queue();
            return;
        } else if (retValue instanceof Emote) {
            RetrieveMessageFromEvent retrieveMessageFromEvent = new RetrieveMessageFromEvent(event);
            if (!retrieveMessageFromEvent.canRetrieveMessage())
                throw new IllegalStateException("could not finish code path, reacting to non message in channel is not supported ");
            channel.addReactionById(retrieveMessageFromEvent.getMessage().getId(), (Emote) retValue).queue();
        } else if (retValue instanceof MessageEmbed) {
            channel.sendMessageEmbeds((MessageEmbed) retValue).queue();
            return;
        }else if( retValue instanceof FileResponse){
            FileResponse fr = (FileResponse) retValue;
            channel.sendFile(fr.getInputStream(),fr.getName(),fr.getAttachmentOptions()).queue();
            return;
        }
        throw new IllegalStateException("response type not supported");
    }
}
