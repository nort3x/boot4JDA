package me.nort3x.b4j.core.configurations;

import me.nort3x.b4j.core.aspects.annotation.Authorize;
import me.nort3x.b4j.core.bots.IBot;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Set;

public class PrettyHelpGenerator extends DebugHelpGenerator {
    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    /**
     * @return url for thumbnail or null to disable it
     */
    public String getThumbnailUrl(){
        return "https://i.imgur.com/TNzxfMB.png";
    }

    @Override
    public void configMessageEmbed(EmbedBuilder embedBuilder, Set<DescribableMethod> methods, IBot bot) {
        super.configMessageEmbed(embedBuilder, methods, bot);
        if(getThumbnailUrl() != null)
        embedBuilder.setThumbnail(getThumbnailUrl());
    }

    @Override
    public void generateHelpBody(EmbedBuilder embedBuilder, Set<DescribableMethod> methods, IBot bot) {

        for (DescribableMethod method : methods) {
            if(method.trigger.isEmpty()) continue;

            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();
            method.trigger.ifPresent(x->{
                key.append(x.shebang()).append(x.value());
            });

            for (String parameter : method.getHelpAnnotation().parameters()) {
                key.append(method.trigger.get().delimiter()).append(parameter);
            }

            value.append(method.getHelpAnnotation().description());

            method.authorization.ifPresent(x -> {
                value.append("```\n");
                value.append("\nAuthorization:\n  ");
                for (Authorize authorize : x.value()) {
                    value.append("authorized: ").append(authorizeToString(authorize)).append("\n  ");
                }
                value.append("```");
            });

            embedBuilder.addField(key.toString(),value.toString(),false);

        }
    }
}
