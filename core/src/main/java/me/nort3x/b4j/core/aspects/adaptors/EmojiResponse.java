package me.nort3x.b4j.core.aspects.adaptors;

public class EmojiResponse {
    Object emoteOrEmoji;
    boolean isEmote;
    public EmojiResponse(Object emoteOrEmoji, boolean isEmote) {
        this.emoteOrEmoji = emoteOrEmoji;
        this.isEmote = isEmote;
    }

    public Object getEmoteOrEmoji() {
        return emoteOrEmoji;
    }

    public boolean isEmote() {
        return isEmote;
    }
}
