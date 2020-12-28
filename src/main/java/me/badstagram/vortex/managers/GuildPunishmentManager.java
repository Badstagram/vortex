package me.badstagram.vortex.managers;

import me.badstagram.vortex.entities.GuildPunishment;
import me.badstagram.vortex.entities.GuildPunishmentType;
import me.badstagram.vortex.entities.builders.GuildPunishmentBuilder;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuildPunishmentManager {
    private final String userId, guildId;

    public GuildPunishmentManager(String userId, String guildId) {
        this.userId = userId;
        this.guildId = guildId;
    }

    /**
     * Create a Guild Punishment
     *
     * @param reason
     *         The reason of the punishment
     * @param type
     *         The type of punishment
     * @param modId
     *         The ID of the moderator that issued the punishment
     * @param perm
     *         If the punishment is permanent
     * @param length
     *         The length of the punishment or {@code null} if the punishment is permanent
     * @param unit
     *         The unit of the length of punishment or {@code null} if the punishment is permanent
     *
     * @return The Case ID
     *
     * @throws Exception
     *         If an {@link Exception} occurs
     */
    public int createCase(String reason, GuildPunishmentType type, String modId, boolean perm, @Nullable String length,
            @Nullable
                    TimeUnit unit) {

        ResultSet rs = null;
        try {
            try {
                try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                        "INSERT INTO punishments (user_id, guild_id, mod_id, reason, type, expire_at) VALUES (?,?,?,?,?,?) RETURNING case_id")) {

                    ps.setString(1, this.userId);
                    ps.setString(2, this.guildId);
                    ps.setString(3, modId);
                    ps.setString(4, reason);
                    ps.setString(5, type.getName());

                    if (!perm) {
                        ps.setNull(6, Types.VARCHAR);
                    } else {
                        var expire = OffsetDateTime.now()
                                .plus(Long.parseLong(length), unit.toChronoUnit())
                                .toInstant()
                                .toEpochMilli();

                        ps.setString(6, Long.toString(expire));
                    }

                    rs = ps.executeQuery();

                    if (rs.next()) {
                        return rs.getInt("case_id");
                    }


                }
            } catch (SQLException e) {
                ErrorHandler.handle(e);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    ErrorHandler.handle(e);
                }
            }
        }
        return -1;
    }

    /**
     * Get a punishment by ID.
     *
     * @param caseId
     *         The case ID
     *
     * @return A {@link GuildPunishment} representing the given ID or {@code null} if no case found with the given ID
     *
     * @throws Exception
     *         If an {@link Exception} occurs
     */
    @Nullable
    public GuildPunishment getCase(int caseId) throws Exception {
        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "SELECT * FROM punishments WHERE guild_id = ? AND case_id = ?")) {

            ps.setString(1, this.guildId);
            ps.setInt(2, caseId);


            rs = ps.executeQuery();

            if (rs.next()) {
                return new GuildPunishmentBuilder()
                        .setGuildId(this.guildId)
                        .setUserId(rs.getString("user_id"))
                        .setReason(rs.getString("reason"))
                        .setModId(rs.getString("mod_id"))
                        .setType(GuildPunishmentType.fromName(rs.getString("type")))
                        .build();
            }
            return null;


        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Get a users punishment.
     *
     * @return A {@link List<GuildPunishment>} representing the given users history
     *
     * @throws Exception
     *         If an {@link Exception} occurs
     */
    public List<GuildPunishment> getHistory() throws Exception {
        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "SELECT * FROM punishments WHERE guild_id = ? AND user_id = ?")) {

            ps.setString(1, this.guildId);
            ps.setString(2, this.userId);


            rs = ps.executeQuery();

            List<GuildPunishment> history = new ArrayList<>();
            while (rs.next()) {
                var punishment = new GuildPunishmentBuilder()
                        .setGuildId(this.guildId)
                        .setCaseId(rs.getInt("case_id"))
                        .setUserId(rs.getString("user_id"))
                        .setReason(rs.getString("reason"))
                        .setModId(rs.getString("mod_id"))
                        .setType(GuildPunishmentType.fromName(rs.getString("type")))
                        .build();

                history.add(punishment);
            }
            return history;


        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

}
