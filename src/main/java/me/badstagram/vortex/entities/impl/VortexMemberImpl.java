package me.badstagram.vortex.entities.impl;

import me.badstagram.vortex.entities.VortexMember;
import me.badstagram.vortex.managers.EconomyManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public class VortexMemberImpl implements VortexMember {
    private final Member member;

    public VortexMemberImpl(Member member) {
        this.member = member;
    }

    @Override
    public EconomyManager getEconomyManager() {
        return new EconomyManager(member.getId(), this.getGuild().getId());
    }

    @NotNull
    @Override
    public User getUser() {
        return member.getUser();
    }

    @NotNull
    @Override
    public Guild getGuild() {
        return member.getGuild();
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissions() {
        return member.getPermissions();
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissions(@NotNull GuildChannel guildChannel) {
        return member.getPermissions(guildChannel);
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit() {
        return member.getPermissionsExplicit();
    }

    @NotNull
    @Override
    public EnumSet<Permission> getPermissionsExplicit(@NotNull GuildChannel guildChannel) {
        return member.getPermissionsExplicit(guildChannel);
    }

    @Override
    public boolean hasPermission(@NotNull Permission... permissions) {
        return member.hasPermission(permissions);
    }

    @Override
    public boolean hasPermission(@NotNull Collection<Permission> collection) {
        return member.hasPermission(collection);
    }

    @Override
    public boolean hasPermission(@NotNull GuildChannel guildChannel, @NotNull Permission... permissions) {
        return member.hasPermission(guildChannel, permissions);
    }

    @Override
    public boolean hasPermission(@NotNull GuildChannel guildChannel, @NotNull Collection<Permission> collection) {
        return member.hasPermission(guildChannel, collection);
    }

    @NotNull
    @Override
    public JDA getJDA() {
        return member.getJDA();
    }

    @NotNull
    @Override
    public OffsetDateTime getTimeJoined() {
        return member.getTimeJoined();
    }

    @Override
    public boolean hasTimeJoined() {
        return member.hasTimeJoined();
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeBoosted() {
        return member.getTimeBoosted();
    }

    @Nullable
    @Override
    public GuildVoiceState getVoiceState() {
        return member.getVoiceState();
    }

    @NotNull
    @Override
    public List<Activity> getActivities() {
        return member.getActivities();
    }

    @NotNull
    @Override
    public OnlineStatus getOnlineStatus() {
        return member.getOnlineStatus();
    }

    @NotNull
    @Override
    public OnlineStatus getOnlineStatus(@NotNull ClientType clientType) {
        return member.getOnlineStatus(clientType);
    }

    @NotNull
    @Override
    public EnumSet<ClientType> getActiveClients() {
        return member.getActiveClients();
    }

    @Nullable
    @Override
    public String getNickname() {
        return member.getNickname();
    }

    @NotNull
    @Override
    public String getEffectiveName() {
        return member.getEffectiveName();
    }

    @NotNull
    @Override
    public List<Role> getRoles() {
        return member.getRoles();
    }

    @Nullable
    @Override
    public Color getColor() {
        return member.getColor();
    }

    @Override
    public int getColorRaw() {
        return member.getColorRaw();
    }

    @Override
    public boolean canInteract(@NotNull Member member) {
        return member.canInteract(member);
    }

    @Override
    public boolean canInteract(@NotNull Role role) {
        return member.canInteract(role);
    }

    @Override
    public boolean canInteract(@NotNull Emote emote) {
        return member.canInteract(emote);
    }

    @Override
    public boolean isOwner() {
        return member.isOwner();
    }

    @Nullable
    @Override
    public TextChannel getDefaultChannel() {
        return member.getDefaultChannel();
    }

    @Override
    public boolean isFake() {
        return member.isFake();
    }

    @NotNull
    @Override
    public String getAsMention() {
        return member.getAsMention();
    }

    @Override
    public long getIdLong() {
        return member.getIdLong();
    }
}
