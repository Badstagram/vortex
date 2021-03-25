package me.badstagram.vortex.managers;

import me.badstagram.vortex.commands.globalbans.GlobalBanStatus;
import me.badstagram.vortex.entities.GlobalBan;
import me.badstagram.vortex.entities.builders.GlobalBanBuilder;
import me.badstagram.vortex.util.DatabaseUtils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

public class GlobalBanManager {

    /**
     * Creates a ban report.
     *
     * @param userId
     *         The id of the user being reported
     * @param modId
     *         The id of the moderator
     * @param reason
     *         The reason of the Global Ban
     * @param proof
     *         The proof of the Global Ban
     *
     * @return The Ban ID
     *
     * @throws SQLException
     *         If a Database error occurred
     */
    @Nonnull
    @CheckReturnValue

    public static String createBanReport(@Nonnull String userId, @Nonnull String modId, @Nonnull String reason,
            @Nonnull String proof) throws SQLException {

        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "INSERT INTO global_bans (ban_id, user_id, reason, proof, status, time_stamp, moderator_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING ban_id")) {

            ps.setString(1, UUID.randomUUID().toString());
            ps.setString(2, userId);
            ps.setString(3, reason);
            ps.setString(4, proof);
            ps.setString(5, GlobalBanStatus.PENDING.getName());
            ps.setString(6, Long.toString(Instant.now().toEpochMilli()));
            ps.setString(7, modId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("ban_id");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return "";
    }

    /**
     * Approve a ban report.
     *
     * @param banId
     *         The id of the ban report
     * @param approvedBy
     *         The id of the moderator
     *
     * @return A {@link GlobalBan} object representing the ban
     *
     * @throws SQLException
     *         If a Database error occurred
     */
    @Nullable
    @CheckReturnValue
    public static GlobalBan approveBanReport(@Nonnull UUID banId, @Nonnull String approvedBy) throws SQLException {

        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "UPDATE global_bans SET status = ?, approved_by = ? WHERE ban_id = ? RETURNING *")) {

            ps.setString(1, GlobalBanStatus.APPROVED.getName());
            ps.setString(2, approvedBy);
            ps.setString(3, banId.toString());


            rs = ps.executeQuery();
            if (rs.next()) {
                return new GlobalBanBuilder()
                        .setBanId(rs.getString("ban_id"))
                        .setUserId(rs.getString("user_id"))
                        .setReason(rs.getString("reason"))
                        .setProof(rs.getString("proof"))
                        .setApprovedBy(rs.getString("approved_by"))
                        .setModeratorId(rs.getString("moderator_id"))
                        .build();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }

    /**
     * Deny a ban report.
     *
     * @param banId
     *         The id of the ban report
     *
     * @return The id of the moderator that reported the ban.
     *
     * @throws SQLException
     *         If a Database error occurred
     */

    @Nullable
    @CheckReturnValue
    public static String denyBanReport(@Nonnull UUID banId) throws SQLException {

        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "UPDATE global_bans SET status = ? WHERE ban_id = ? RETURNING user_id")) {

            ps.setString(1, GlobalBanStatus.DENIED.getName());
            ps.setString(2, banId.toString());

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("user_id");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }


}
