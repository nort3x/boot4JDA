package me.nort3x.cobalt.internals.bots;

import net.dv8tion.jda.api.JDA;

public interface IBot {
    JDA provideJDA();
    String getName();
}
