package me.badstagram.vortex.managers;

import me.badstagram.vortex.automod.AutoModPunishmentType;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.Nonnull;

public class GuildSettingsManager {
    private final String guildId;


    public GuildSettingsManager(String guildId) {
        this.guildId = guildId;
    }

    public GuildSettingsManager(Guild guild) {
        this.guildId = guild.getId();
    }

    public GuildSettingsManager(CommandContext ctx) {
        this.guildId = ctx.getGuild().getId();
    }

    /**
     * Gets if Anti Advertise is enabled
     *
     * @return If Anti Advertise is enabled
     */
    public boolean isAntiAdvertiseEnabled() {
        try {

            var result = DatabaseUtils.executeQuery("SELECT anti_invite_enabled FROM guild_config WHERE guild_id = ?", this.guildId);

            return (Boolean) result.get("anti_invite_enabled");


        } catch (Exception e) {
            ErrorHandler.handle(e);
            return true;
        }
    }

    public void setAntiAdvertiseEnabled(boolean enable) {
        try {
            DatabaseUtils.execute("UPDATE guild_config SET anti_invite_enabled = ? WHERE guild_id = ?", enable, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public AutoModPunishmentType getAdvertiseAction() {
        return AutoModPunishmentType.IGNORE;
    }

    /**
     * Sets the action taken when a user posts an invite link.
     *
     * @param type The Punishment Type.
     * @throws Exception If an Exception happens while updating the Database
     * @author Badstagram
     */
    public void setAdvertiseAction(@Nonnull AutoModPunishmentType type) throws Exception {

        if (type == AutoModPunishmentType.UNKNOWN) throw new IllegalArgumentException("type can't be UNKNOWN");

        DatabaseUtils.execute("UPDATE guild_config SET anti_invite_action = ? WHERE guild_id = ?", this.guildId, type.getName());
    }

    public String getModLogChannel() {

        try {
            return (String) DatabaseUtils.executeQuery("SELECT mod_log FROM guild_config WHERE guild_id = ?", this.guildId)
                    .get("mod_log");

        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
        return "";
    }

    public void setModLogChannel(String channelId) {
        try {
            Checks.isSnowflake(channelId, "Channel ID");

            DatabaseUtils.execute("UPDATE guild_config SET mod_log = ? WHERE guild_id = ?", channelId, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public void setDeCancerEnabled(boolean enabled) {
        try {

            DatabaseUtils.execute("UPDATE guild_config SET decancer_enabled = ? WHERE guild_id = ?", enabled, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public boolean isDeCancerEnabled() {
        try {

            var result = DatabaseUtils.executeQuery("SELECT decancer_enabled FROM guild_config WHERE guild_id = ?", this.guildId);

            return (Boolean) result.get("decancer_enabled");


        } catch (Exception e) {
            ErrorHandler.handle(e);
            return true;
        }
    }

    public String getPunishLogChannel() {

        try {
            return (String) DatabaseUtils.executeQuery("SELECT mod_log FROM guild_config WHERE guild_id = ?", this.guildId)
                    .get("mod_log");

        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
        return "";
    }

    public void setPunishLogChannel(String channelId) {
        try {
            Checks.isSnowflake(channelId, "Channel ID");

            DatabaseUtils.execute("UPDATE guild_config SET mod_log = ? WHERE guild_id = ?", channelId, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }
}
