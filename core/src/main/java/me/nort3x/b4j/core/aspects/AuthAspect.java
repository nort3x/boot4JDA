package me.nort3x.b4j.core.aspects;

import me.nort3x.b4j.core.aspects.adaptors.UserAnalyzer;
import me.nort3x.b4j.core.aspects.adaptors.GuildAnalyzer;
import me.nort3x.b4j.core.aspects.adaptors.MessageChannelAnalyzer;
import me.nort3x.b4j.core.aspects.annotation.Authorization;
import me.nort3x.b4j.core.aspects.annotation.AuthorizationDetailType;
import me.nort3x.b4j.core.aspects.annotation.Authorize;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Aspect
@Order(3)
public class AuthAspect {

    Logger log = LoggerFactory.getLogger(AuthAspect.class);

    @Around("@annotation(me.nort3x.b4j.core.aspects.annotation.Authorization) && args(event,..)")
    public Object auth(ProceedingJoinPoint pjp, Event event) throws Throwable {
        var authorization = ((MethodSignature) pjp.getSignature())
                .getMethod()
                .getAnnotation(Authorization.class);

        if (canAuth(event, authorization.value()))
            return pjp.proceed();
        else if (authorization.reflectError()) {
            return new MessageBuilder().append("you are not authorized to invoke this").build();
        }
        return null;
    }

    private boolean canAuth(Event event, Authorize[] authList) {
        for (Authorize authorize : authList) {
            if (isAuthenticated(event, authorize)) return true;
        }
        return false;
    }

    private boolean isAuthenticated(Event event, Authorize authorize) {
        UserAnalyzer authorAnalysisEvent = new UserAnalyzer(event);
        GuildAnalyzer guildAnalyzer = new GuildAnalyzer(event);
        MessageChannelAnalyzer messageChannelAnalyzer = new MessageChannelAnalyzer(event);

        switch (authorize.authorizationPrinciple()) {
            case USER:
                if (authorAnalysisEvent.isAuthorAware())
                    return compareUserID(
                            authorAnalysisEvent.getAuthor(),
                            authorize.authorizationDetailType(),
                            authorize.value()
                    );
                break;
            case ROLE:
                if (authorAnalysisEvent.isAuthorAware())
                    return compareMemberRole(
                            authorize.value(),
                            authorAnalysisEvent.getMember(),
                            authorize.authorizationDetailType()
                    );
                break;
            case GUILD:
                if (guildAnalyzer.isSupported())
                    return compareGuild(
                            authorize.value(),
                            guildAnalyzer.getGuild(),
                            authorize.authorizationDetailType()
                    );
                break;

            case CHANNEL:
                if (messageChannelAnalyzer.isSupported())
                    return compareChannel(
                            authorize.value(),
                            messageChannelAnalyzer.getChannel(),
                            authorize.authorizationDetailType()
                    );
                break;

            default: return false;
        }

        return false;


    }

    private boolean compareChannel(String nameOrId, MessageChannel channel, AuthorizationDetailType authorizationDetailType) {
        return compareIDNameEntity(nameOrId,authorizationDetailType,channel.getName(),channel.getIdLong());
    }

    private boolean compareGuild(String nameOrId, Guild guild, AuthorizationDetailType authorizationDetailType) {
        return compareIDNameEntity(nameOrId,authorizationDetailType,guild.getName(),guild.getIdLong());
    }

    private boolean compareMemberRole(String nameOrId, Optional<Member> maybeMember, AuthorizationDetailType authorizationDetailType) {
        if (maybeMember.isEmpty()) return false;
        Member member = maybeMember.get();

        return member.getRoles().stream().anyMatch(role -> compareIDNameEntity(nameOrId, authorizationDetailType, role.getName(), role.getIdLong()));


    }

    private boolean compareUserID(User user, AuthorizationDetailType type, String providedDetail) {
        return compareIDNameEntity(providedDetail, type, user.getName(), user.getIdLong());
    }

    private boolean compareIDNameEntity(String detail, AuthorizationDetailType detailType, String name, long id) {
        switch (detailType) {
            case BY_NAME:
                return name.equals(detail);
            case BY_ID:
                return id == Long.parseLong(detail);
            default:
                return false;
        }
    }

}
