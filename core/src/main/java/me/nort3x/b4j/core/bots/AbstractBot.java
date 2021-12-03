package me.nort3x.b4j.core.bots;


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


/**
 * used as basis for configuring a Discord Bot instance
 * a wrapper around {@link JDABuilder} you can use {@link IBot} for absolute control
 */
@Component
public abstract class AbstractBot implements IBot {


    Logger logger = LoggerFactory.getLogger(getClass());

    private JDA jda;

    /**
     * as name suggest
     * @return connection token for JDA
     */
    abstract protected String provideToken();

    /**
     * will be called after default configuration is done on builder
     * before building JDA instance
     * @param jdaBuilder
     */
    abstract protected void configure(JDABuilder jdaBuilder);

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
            logger.error("Bot: " + provideName() + " with token: " + provideToken() + " Build Exception: ", e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public JDA provideJDA() {
        return jda;
    }

}
