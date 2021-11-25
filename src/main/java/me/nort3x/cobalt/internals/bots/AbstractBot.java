package me.nort3x.cobalt.internals.bots;


import me.nort3x.cobalt.internals.events.EventManager;
import me.nort3x.cobalt.internals.processors.CommandPoolProcessor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public abstract class AbstractBot implements IBot {


    @Autowired
    ApplicationContext context;

    @PostConstruct
    void construct(){
        CommandPoolProcessor.observe(getName(),new EventManager(provideJDA(),context));
    }




    Logger logger = LoggerFactory.getLogger(getClass());

    private JDA jda;
    abstract protected String provideToken();

    public AbstractBot(){
        buildIfNotExist();
    }

    private void buildIfNotExist() {
        JDABuilder builder = JDABuilder.createDefault(provideToken());
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Disable presence updates and typing events
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);

        // further configure
        try {
            configure(builder);
            jda = builder.build();
        } catch (Exception e) {
            logger.error("Bot: " + getName() + " with token: " + provideToken() + " Build Exception: ", e);
        }
    }

    protected abstract void configure(JDABuilder jdaBuilder);

    @Override
    public JDA provideJDA() {
        return jda;
    }

}
