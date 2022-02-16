package me.nort3x.b4j.example.aop;

import me.nort3x.b4j.core.annotations.Command;
import me.nort3x.b4j.core.annotations.CommandPool;
import me.nort3x.b4j.core.aspects.adaptors.EmojiResponse;
import me.nort3x.b4j.core.aspects.adaptors.FileResponse;
import me.nort3x.b4j.core.aspects.adaptors.MessageChannelAnalyzer;
import me.nort3x.b4j.core.aspects.adaptors.UserAnalyzer;
import me.nort3x.b4j.core.aspects.annotation.*;
import me.nort3x.b4j.core.configurations.HelpBannerGenerator;
import me.nort3x.b4j.core.configurations.HelpGenerator;
import me.nort3x.b4j.core.configurations.PrettyHelpGenerator;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;


/**
 * this pool demonstrates basic aop usage
 */
@CommandPool
public class AopCommandPool {



    /**
     * example 1: Aop.IO <br/>
     * by returning:
     * <ul>
     * <li>Message</li>
     * <li>MessageEmbed</li>
     * <li>FileResponse</li>
     * <li>EmojiResponse</li>
     * </ul>
     * aop tries to :
     *<ul>
     * <li>1. replay that to the user if possible see {@link UserAnalyzer#isAuthorAware()}</li>
     * <li>2. send it in the text channel see {@link MessageChannelAnalyzer#isSupported()}</li>
     *</ul>
     * it pretty much depends on what kind of event you are listening to
     * returning null (aka void function) turns this feature off
     */
    @Command
    @NotBot
    EmojiResponse reactToMessages(MessageReceivedEvent messageReceivedEvent){
        return  new EmojiResponse("\uD83D\uDE02",false);
    }


    /**
     *  two useful negates! pull request more useful negates!
     * @see me.nort3x.b4j.core.aspects.NegateAspect
     */
    @Command
    @NotBot
    @NotSelf
    Message sayHi(MessageReceivedEvent messageReceivedEvent){
        return new MessageBuilder()
                .append("Hi! ")
                .append(messageReceivedEvent.getMessage().getContentRaw())
                .build();
    }

    // how to trigger
    @Trigger(value = "show")
    @Command
    @NotSelf
    public Message downloadForMe(MessageReceivedEvent rawEvent, String url){
        return new MessageBuilder().append("I got the: ").append(url).build();
    }



    @Command
    @Trigger(value = "send-file",shebang = "~",reflectExceptionMessages = true,delimiter = "/")
    // ~send-file/something will trigger this command
    FileResponse fileResponse(MessageReceivedEvent me,String fileContent){
        return new FileResponse("haha it's file".getBytes(StandardCharsets.UTF_8),"message.sick", AttachmentOption.SPOILER);
    }


    @Command
    // remember Authorization is logical Or between the provided Authorizes, if you need anything more sophisticated
    // it's better to develop it with manual procedures
    @Authorization(value = {
            @Authorize("w0lfic"), // my discord username
            @Authorize(value = "w0lficTestServerWhichDoesn'tExist",
                    authorizationDetailType = AuthorizationDetailType.BY_NAME,
                    authorizationPrinciple = AuthorizationPrinciple.GUILD
            )
    },reflectError = true)
    @NotSelf
    void criticalCommand(MessageReceivedEvent messageReceivedEvent){
        // body ignored
    }




    // need help banner?

    // supply your generator
    @Bean
    public HelpBannerGenerator helpBannerGeneratorBean(){
        return new PrettyHelpGenerator();
//        return new DebugHelpGenerator();
    }

    // this service will consume it so use it
    @Autowired
    HelpGenerator generator;



    @Command
    @NotSelf
    @Trigger("help")
    @Help(description = "will show help",parameters = {})

    // todo: change it to your username!
    @Authorization(value = {
            @Authorize(value = "w0lfic",authorizationPrinciple = AuthorizationPrinciple.USER,authorizationDetailType = AuthorizationDetailType.BY_NAME)
    },reflectError = true)

    public MessageEmbed help(MessageReceivedEvent messageReceivedEvent){
        // or any other return type depending on the generator
        return (MessageEmbed) generator.getHelpBanner(messageReceivedEvent);
    }


}
