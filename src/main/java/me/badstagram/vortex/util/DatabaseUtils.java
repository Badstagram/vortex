package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Config;
import org.intellij.lang.annotations.Language;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUtils {
    private static Connection con = null;


    public static Connection getConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DriverManager.getConnection(Config.get("db_url"), Config.get("db_user"), Config.get("db_password"));
        }
        return con;
    }

    /**
     * @param sql
     *         The SQL Statement to execute
     * @param params
     *         The parameters to insert into the Statement
     *
     * @return A {@link HashMap} with the key set to the column name
     *
     * @throws Exception
     *         If a {@link Exception} happens.
     * @throws IllegalStateException
     *         If the query returned no results
     */
    public static Map<String, Object> executeQuery(@Nonnull String sql, Object... params) throws Exception {
        ResultSet rs = null;
        try (var con = getConnection(); var ps = con.prepareStatement(sql)) {
            var i = 1;

            for (var param : params) {
                ps.setObject(i, param);
                i++;
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                var metadata = rs.getMetaData();
                var count = metadata.getColumnCount();

                for (int j = 1; j <= count; j++) {
                    var key = metadata.getColumnName(j);
                    var value = rs.getObject(j);

                    map.put(key, value);
                }
                return map;

            } else {
                throw new IllegalStateException("ResultSet returned no results");
            }

        } finally {

            if (rs != null) {
                rs.close();
            }
        }
    }

    public static void execute(@Nonnull String sql, Object... params) throws Exception {

        try (var con = getConnection(); var ps = con.prepareStatement(sql)) {
            var i = 1;
            for (var param : params) {
                ps.setObject(i, param);
                i++;
            }

            ps.execute();

        }
    }
}

