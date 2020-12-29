/*
 MIT License

 Copyright (c) 2020 badstagram

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.

 */

package me.badstagram.vortex.managers;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CooldownManager {
    private final String guildId, userId;
    private final GuildMessageReceivedEvent event;
    private final Command cmd;

    public CooldownManager(String guildId, String userId, GuildMessageReceivedEvent event,
            Command cmd) {
        this.guildId = guildId;
        this.userId = userId;
        this.event = event;

        this.cmd = cmd;
    }

    public void putCooldown() {

        var cooldown = this.cmd.getCooldown();

        var endTime = Instant.now().plusSeconds(cooldown).toEpochMilli();

        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement("INSERT INTO cooldowns (guild_id, user_id, command_name, expire_time) VALUES (?,?,?,?)")) {

            ps.setString(1, this.guildId);
            ps.setString(2, this.userId);
            ps.setString(3, this.cmd.getName());
            ps.setString(4, String.valueOf(endTime));

            ps.execute();
            Logger log = LoggerFactory.getLogger(this.getClass());

            log.debug("Cooldown added for command {}. Expires at {} ({} seconds)", this.cmd.getName(), endTime, cooldown);
        } catch (final Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public boolean isOnCooldown() {

        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement("SELECT expire_time FROM cooldowns WHERE guild_id = ? AND user_id = ? AND command_name = ?")) {

            ps.setString(1, this.guildId);
            ps.setString(2, this.userId);
            ps.setString(3, this.cmd.getName());


            rs = ps.executeQuery();

            if (rs.next()) {
                Logger log = LoggerFactory.getLogger(this.getClass());
                long expireTime = Long.parseLong(rs.getString("expire_time"));
                long now = Instant.now().toEpochMilli();


                return expireTime > now;

            }

        } catch (final Exception e) {
            ErrorHandler.handle(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                ErrorHandler.handle(e);

            }
        }
        return false;
    }

    public String getTimeRemaining() {
        ResultSet rs = null;
        try (Connection con = DatabaseUtils.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT expire_time FROM cooldowns WHERE guild_id = ? AND user_id = ? AND command_name = ?")) {

            ps.setString(1, this.guildId);
            ps.setString(2, this.userId);
            ps.setString(3, this.cmd.getName());


            rs = ps.executeQuery();

            if (rs.next()) {

                Instant now = Instant.now();
                Instant expire = Instant.ofEpochMilli(Long.parseLong(rs.getString("expire_time")));
                return FormatUtil.secondsToTimeCompact(now.until(expire, ChronoUnit.SECONDS));

            }

        } catch (final Exception e) {
            ErrorHandler.handle(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                ErrorHandler.handle(e);

            }
        }

        return "";
    }

}
