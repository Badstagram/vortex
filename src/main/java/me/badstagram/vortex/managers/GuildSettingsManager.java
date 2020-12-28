package me.badstagram.vortex.managers;

import groovy.io.EncodingAwareBufferedWriter;
import me.badstagram.vortex.automod.AutoModPunishmentType;
import me.badstagram.vortex.entities.GuildPunishmentType;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.Helpers;

import javax.annotation.Nonnull;

public class GuildSettingsManager {
    private final String guildId;

    public GuildSettingsManager(String guildId) {
        this.guildId = guildId;
    }

    /**
     * Sets the action taken when a user posts an invite link.
     *
     * @param type
     *         The Punishment Type.
     *
     * @throws Exception
     *         If an Exception happens while updating the Database
     * @author Badstagram
     */
    public void setAdvertiseAction(@Nonnull AutoModPunishmentType type) throws Exception {

        if (type == AutoModPunishmentType.UNKNOWN) throw new IllegalArgumentException("type can't be UNKNOWN");

        DatabaseUtils.execute("UPDATE guild_config SET anti_invite_action = ? WHERE guild_id = ?", this.guildId, type.getName());
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



    public AutoModPunishmentType getAdvertiseAction() {
        return AutoModPunishmentType.IGNORE;
    }

    public void setAntiAdvertiseEnabled(boolean enable) {
        try {
            DatabaseUtils.execute("UPDATE guild_config SET anti_invite_enabled = ? WHERE guild_id = ?", enable, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public void setModLogChannel(String channelId) {

        try {
            Checks.isSnowflake(channelId, "Channel ID");

            DatabaseUtils.execute("UPDATE guild_config SET mod_log = ? WHERE guild_id = ?", channelId, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
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
}
