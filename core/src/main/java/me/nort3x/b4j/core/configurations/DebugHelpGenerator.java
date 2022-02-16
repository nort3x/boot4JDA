package me.nort3x.b4j.core.configurations;

import me.nort3x.b4j.core.aspects.annotation.Authorize;
import me.nort3x.b4j.core.bots.IBot;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Set;

public class DebugHelpGenerator implements HelpBannerGenerator {
    @Override
    public boolean supportHelpForBotName(IBot bot) {
        return true;
    }

    public Color getColor(){
        return Color.BLUE;
    }
    public String getFooter(IBot bot){
        return "powered by JDA:b4j";
    }

    public String getTitle(IBot bot){
        return bot.provideName() + " help banner";
    }

    public void configMessageEmbed(EmbedBuilder embedBuilder,Set<DescribableMethod> methods,IBot bot){
        embedBuilder.setTitle(getTitle(bot));
        embedBuilder.setColor(getColor());
        embedBuilder.setFooter(getFooter(bot));
    }

    public void generateHelpBody(EmbedBuilder embedBuilder, Set<DescribableMethod> methods, IBot bot){
        embedBuilder.appendDescription("```");
        embedBuilder.appendDescription("\n---------------------\n");

        for (DescribableMethod method : methods) {
            embedBuilder.appendDescription("method name: "+method.getMethod().getName());


            method.trigger.ifPresent(x -> {
                embedBuilder.appendDescription("\ntrigger:\n  ");
                embedBuilder.appendDescription("shebang:   " + x.shebang() + "\n  ");
                embedBuilder.appendDescription("trigger:   " + x.value() + "\n  ");
                embedBuilder.appendDescription("delimiter: " + (x.delimiter().isBlank() ? "SPACE" : x.delimiter()) + "\n");
            });
            embedBuilder.appendDescription("parameters:\n");

            for (String parameter : method.getHelpAnnotation().parameters()) {
                embedBuilder.appendDescription("  ").appendDescription(parameter).appendDescription("\n");
            }
            embedBuilder.appendDescription("description:\n  ")
                    .appendDescription(method.getHelpAnnotation().description());


            method.authorization.ifPresent(x -> {
                embedBuilder.appendDescription("\nAuthorization:\n  ");
                for (Authorize authorize : x.value()) {
                    embedBuilder.appendDescription("authorized: "+ authorizeToString(authorize) +"\n  ");
                }
            });

            embedBuilder.appendDescription("\n---------------------\n");
        }
        embedBuilder.appendDescription("```");
    }

    @Override
    public final Object generateHelpForBot(IBot bot, Set<DescribableMethod> methods) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        configMessageEmbed(embedBuilder,methods,bot);
        generateHelpBody(embedBuilder,methods,bot);
        return embedBuilder.build();
    }

    protected String authorizeToString(Authorize authorize){
        StringBuilder sb = new StringBuilder();
        return sb.append("\n    scope: ")
                .append(authorize.authorizationDetailType())
                .append("\n    on:    ")
                .append(authorize.authorizationPrinciple())
                .append("\n    value: ")
                .append(authorize.value()).toString();
    }
}
