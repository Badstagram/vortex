package me.badstagram.vortex.managers;

import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;

import javax.annotation.Nonnull;

public class UserSettingsManager {

    private final String userId;

    public UserSettingsManager(String userId) {
        this.userId = userId;
    }

    private void setPronouns(@Nonnull String pronouns) {
        try {
            DatabaseUtils.execute("UPDATE users SET pronouns = ? WHERE id = ?", pronouns, this.userId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    private String getPronouns() {
        try {
            var result = DatabaseUtils.executeQuery("SELECT pronouns FROM users WHERE id = ?", this.userId);

            if (!result.containsKey("pronouns")) return "Not Specified";

            return (String) result.get("pronouns");
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
        return "Not Specified";
    }
}
