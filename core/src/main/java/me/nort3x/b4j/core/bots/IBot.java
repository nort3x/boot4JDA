package me.nort3x.b4j.core.bots;

import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;

@Component
public interface IBot {
    JDA provideJDA();
    String provideName();
}
