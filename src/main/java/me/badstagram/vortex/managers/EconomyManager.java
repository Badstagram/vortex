package me.badstagram.vortex.managers;

import me.badstagram.vortex.util.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EconomyManager {
    private final String userId, guildId;

    public EconomyManager(String userId, String guildId) {
        this.userId = userId;
        this.guildId = guildId;
    }


    /**
     * Adds money to a users balance
     *
     * @param amount
     *         The amount to add
     *
     * @return The new balance or {@code -1} if some database error occurs
     *
     * @throws SQLException
     *         If a database error occurs
     */
    public int addMoney(int amount) throws SQLException {
        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "UPDATE economy SET money = money + ? WHERE user_id = ? AND guild_id = ? RETURNING money")) {


            ps.setInt(1, amount);
            ps.setString(2, this.userId);
            ps.setString(3, this.guildId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("money");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return -1;
    }

    /**
     * Remove money from a users balance
     *
     * @param amount
     *         The amount to remove
     *
     * @return The new balance or {@code -1} if some database error occurs
     *
     * @throws SQLException
     *         If a database error occurs
     */
    public int removeMoney(int amount) throws SQLException {
        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "UPDATE economy SET money = money - ? WHERE user_id = ? AND guild_id = ? RETURNING money")) {

            ps.setInt(1, amount);
            ps.setString(2, this.userId);
            ps.setString(3, this.guildId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("money");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return -1;
    }

    /**
     * Gets a users balance
     *
     * @return The new balance or {@code -1} if some database error occurs
     *
     * @throws SQLException
     *         If a database error occurs
     */
    public int getMoney() throws SQLException {
        ResultSet rs = null;
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "SELECT * FROM economy WHERE user_id = ? AND guild_id = ?")) {


            ps.setString(1, this.userId);
            ps.setString(2, this.guildId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("money");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return -1;
    }

    /**
     * Sets a users balance
     *
     * @param amount
     *         The amount to set the balance to
     *
     * @throws SQLException
     *         If a database error occurs
     */
    public void setMoney(int amount) throws SQLException {
        try (var con = DatabaseUtils.getConnection(); var ps = con.prepareStatement(
                "UPDATE economy SET money = ? WHERE user_id = ? AND guild_id = ?")) {

            ps.setInt(1, amount);
            ps.setString(2, this.userId);
            ps.setString(3, this.guildId);

            ps.execute();

        }
    }


}
