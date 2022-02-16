package me.nort3x.b4j.core.events;


import me.nort3x.b4j.core.aspects.annotation.Trigger;
import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.application.ApplicationCommandCreateEvent;
import net.dv8tion.jda.api.events.application.ApplicationCommandDeleteEvent;
import net.dv8tion.jda.api.events.application.ApplicationCommandUpdateEvent;
import net.dv8tion.jda.api.events.application.GenericApplicationCommandEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.GenericChannelEvent;
import net.dv8tion.jda.api.events.channel.update.*;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.*;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.events.message.react.*;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.*;
import net.dv8tion.jda.api.events.self.*;
import net.dv8tion.jda.api.events.stage.GenericStageInstanceEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceCreateEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceDeleteEvent;
import net.dv8tion.jda.api.events.stage.update.GenericStageInstanceUpdateEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdatePrivacyLevelEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdateTopicEvent;
import net.dv8tion.jda.api.events.thread.GenericThreadEvent;
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent;
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent;
import net.dv8tion.jda.api.events.thread.member.GenericThreadMemberEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberLeaveEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * a wrapper around {@link ListenerAdapter}
 * which let you add listeners as methods one by one
 * given methods will be grouped by their input parameter and will execute on received events in parallel
 */
public class BasicListener extends ListenerAdapter {

    private final Map<Class<?>, Set<InvokableRule>> parameterTypeToMethods = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(BasicListener.class);

    public boolean addRule(Method m, Object o) {
        Parameter[] parameters = m.getParameters();
        if (parameters.length != 1 && !m.isAnnotationPresent(Trigger.class)) {
            logger.warn("non trigger command method should only accept 'one parameter' of event type : " + m.getDeclaringClass().getName() + "." + m.getName() + " is Rejected");
            return false;
        }
        Class<?> type = parameters[0].getType();
        parameterTypeToMethods.computeIfAbsent(parameters[0].getType(), x -> ConcurrentHashMap.newKeySet()).add(new InvokableRule(m, o));
        return true;
    }


    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SelectionMenuEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onStageInstanceDelete(StageInstanceDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(StageInstanceDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onStageInstanceUpdateTopic(StageInstanceUpdateTopicEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(StageInstanceUpdateTopicEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onStageInstanceUpdatePrivacyLevel(StageInstanceUpdatePrivacyLevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(StageInstanceUpdatePrivacyLevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onStageInstanceCreate(StageInstanceCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(StageInstanceCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateBitrate(ChannelUpdateBitrateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateBitrateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateName(ChannelUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateNSFW(ChannelUpdateNSFWEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateNSFWEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateParent(ChannelUpdateParentEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateParentEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdatePosition(ChannelUpdatePositionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdatePositionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateRegion(ChannelUpdateRegionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateRegionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateSlowmode(ChannelUpdateSlowmodeEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateSlowmodeEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateTopic(ChannelUpdateTopicEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateTopicEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateType(ChannelUpdateTypeEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateTypeEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateUserLimit(ChannelUpdateUserLimitEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateUserLimitEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateArchived(ChannelUpdateArchivedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateArchivedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateArchiveTimestampEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateAutoArchiveDuration(ChannelUpdateAutoArchiveDurationEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateAutoArchiveDurationEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateLocked(ChannelUpdateLockedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateLockedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onChannelUpdateInvitable(ChannelUpdateInvitableEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ChannelUpdateInvitableEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onThreadRevealed(ThreadRevealedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ThreadRevealedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onThreadHidden(ThreadHiddenEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ThreadHiddenEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onThreadMemberJoin(ThreadMemberJoinEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ThreadMemberJoinEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onThreadMemberLeave(ThreadMemberLeaveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ThreadMemberLeaveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateNSFWLevel(GuildUpdateNSFWLevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateNSFWLevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberUpdateAvatarEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceVideo(GuildVoiceVideoEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceVideoEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceRequestToSpeak(GuildVoiceRequestToSpeakEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceRequestToSpeakEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdateIcon(RoleUpdateIconEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdateIconEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericComponentInteractionCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericStageInstance(GenericStageInstanceEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericStageInstanceEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericStageInstanceUpdate(GenericStageInstanceUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericStageInstanceUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericChannel(GenericChannelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericChannelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericChannelUpdate(GenericChannelUpdateEvent<?> event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericChannelUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericThread(GenericThreadEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericThreadEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericThreadMember(GenericThreadMemberEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericThreadMemberEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericUpdate(UpdateEvent<?, ?> event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRawGateway(RawGatewayEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RawGatewayEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGatewayPing(GatewayPingEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GatewayPingEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onReady(ReadyEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ReadyEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onResumed(ResumedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ResumedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onReconnected(ReconnectedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ReconnectedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(DisconnectEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ShutdownEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(StatusChangeEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onException(ExceptionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ExceptionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SlashCommandEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ButtonClickEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onApplicationCommandUpdate(ApplicationCommandUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ApplicationCommandUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onApplicationCommandDelete(ApplicationCommandDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ApplicationCommandDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onApplicationCommandCreate(ApplicationCommandCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(ApplicationCommandCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateDiscriminatorEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateAvatar(UserUpdateAvatarEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateAvatarEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateOnlineStatusEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateActivityOrderEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateFlags(UserUpdateFlagsEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateFlagsEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserTyping(UserTypingEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserTypingEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserActivityStartEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserActivityEnd(UserActivityEndEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserActivityEndEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUserUpdateActivities(UserUpdateActivitiesEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UserUpdateActivitiesEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onSelfUpdateAvatar(SelfUpdateAvatarEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SelfUpdateAvatarEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onSelfUpdateMFA(SelfUpdateMFAEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SelfUpdateMFAEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onSelfUpdateName(SelfUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SelfUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onSelfUpdateVerified(SelfUpdateVerifiedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(SelfUpdateVerifiedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageReceivedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageBulkDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageEmbed(MessageEmbedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageEmbedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageReactionAddEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageReactionRemoveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageReactionRemoveAllEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onMessageReactionRemoveEmote(MessageReactionRemoveEmoteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(MessageReactionRemoveEmoteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onPermissionOverrideDelete(PermissionOverrideDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(PermissionOverrideDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onPermissionOverrideUpdate(PermissionOverrideUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(PermissionOverrideUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onPermissionOverrideCreate(PermissionOverrideCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(PermissionOverrideCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }


    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildReadyEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildTimeout(GuildTimeoutEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildTimeoutEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildJoinEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildLeaveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildAvailableEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUnavailableEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UnavailableGuildJoinedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(UnavailableGuildLeaveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildBanEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUnbanEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberRemoveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateAfkChannelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateSystemChannelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateRulesChannelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateCommunityUpdatesChannelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateAfkTimeoutEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateExplicitContentLevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateIcon(GuildUpdateIconEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateIconEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateMFALevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateNotificationLevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateOwnerEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }


    @Override
    public void onGuildUpdateSplash(GuildUpdateSplashEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateSplashEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateVerificationLevelEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateLocale(GuildUpdateLocaleEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateLocaleEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateFeatures(GuildUpdateFeaturesEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateFeaturesEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateVanityCodeEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateBanner(GuildUpdateBannerEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateBannerEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateDescription(GuildUpdateDescriptionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateDescriptionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateBoostTierEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateBoostCountEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateMaxMembersEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildUpdateMaxPresences(GuildUpdateMaxPresencesEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildUpdateMaxPresencesEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildInviteCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildInviteDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberJoinEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberRoleAddEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberRoleRemoveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberUpdateNicknameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberUpdateBoostTimeEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildMemberUpdatePendingEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceJoinEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceMoveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceLeaveEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceMute(GuildVoiceMuteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceMuteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceDeafenEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceGuildMuteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceGuildDeafenEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceSelfMuteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceSelfDeafenEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceSuppress(GuildVoiceSuppressEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceSuppressEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGuildVoiceStream(GuildVoiceStreamEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GuildVoiceStreamEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleCreate(RoleCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleDeleteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdateColor(RoleUpdateColorEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdateColorEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdateHoisted(RoleUpdateHoistedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdateHoistedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdateMentionable(RoleUpdateMentionableEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdateMentionableEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdatePermissionsEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onRoleUpdatePosition(RoleUpdatePositionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(RoleUpdatePositionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onEmoteAdded(EmoteAddedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(EmoteAddedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onEmoteRemoved(EmoteRemovedEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(EmoteRemovedEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onEmoteUpdateName(EmoteUpdateNameEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(EmoteUpdateNameEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onEmoteUpdateRoles(EmoteUpdateRolesEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(EmoteUpdateRolesEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onHttpRequest(HttpRequestEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(HttpRequestEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericApplicationCommand(GenericApplicationCommandEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericApplicationCommandEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericInteractionCreate(GenericInteractionCreateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericInteractionCreateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericMessageEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericMessageReactionEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }


    @Override
    public void onGenericUser(GenericUserEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericUserEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericUserPresence(GenericUserPresenceEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericUserPresenceEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericSelfUpdate(GenericSelfUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericSelfUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }


    @Override
    public void onGenericGuild(GenericGuildEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericGuildUpdate(GenericGuildUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericGuildInvite(GenericGuildInviteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildInviteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericGuildMember(GenericGuildMemberEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildMemberEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildMemberUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericGuildVoiceEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericRole(GenericRoleEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericRoleEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericRoleUpdate(GenericRoleUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericRoleUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericEmote(GenericEmoteEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericEmoteEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericEmoteUpdate(GenericEmoteUpdateEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericEmoteUpdateEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }

    @Override
    public void onGenericPermissionOverride(GenericPermissionOverrideEvent event) {
        Optional.ofNullable(parameterTypeToMethods.getOrDefault(GenericPermissionOverrideEvent.class, null))
                .ifPresent(set ->
                        set.parallelStream().forEach(invokableRule -> invokableRule.invoke(event))
                );
    }
}
