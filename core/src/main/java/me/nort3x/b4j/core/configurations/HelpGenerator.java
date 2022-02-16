package me.nort3x.b4j.core.configurations;

import me.nort3x.b4j.core.B4JContext;
import me.nort3x.b4j.core.bots.IBot;
import net.dv8tion.jda.api.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class HelpGenerator {

    @Autowired
    @Lazy
    B4JContext b4JContext;

    @Autowired
    HelpBannerGenerator helpBannerGenerator;

    public Object getHelpBanner(Event event){
        IBot bot = b4JContext.getBotFromJDA(event.getJDA());
        if(helpBannerGenerator.supportHelpForBotName(bot))
            return helpBannerGenerator.generateHelpForBot(bot,b4JContext.getDescribableCommandsForBot(bot));
        return null;
    }


}
