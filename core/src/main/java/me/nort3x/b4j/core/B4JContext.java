package me.nort3x.b4j.core;


import me.nort3x.b4j.core.bots.IBot;
import me.nort3x.b4j.core.configurations.HelpBannerGenerator;
import me.nort3x.b4j.core.processors.CommandPoolProcessor;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;

import java.util.Set;

@Configuration
@ComponentScan
@Lazy
public class B4JContext {
    @Autowired
    CommandPoolProcessor cpp;



    /**
     * @param jda
     * @return {@link IBot} which provided this JDA
     */
    public IBot getBotFromJDA(@NonNull JDA jda){
        return cpp.getBotJDARelation().getOrDefault(jda,null);
    }

    public Set<HelpBannerGenerator.DescribableMethod> getDescribableCommandsForBot(IBot bot){
        return cpp.getBotDescribeCommands(bot);
    }
}
