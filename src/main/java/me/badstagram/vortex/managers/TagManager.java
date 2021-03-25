package me.badstagram.vortex.managers;

import me.badstagram.vortex.entities.Tag;
import me.badstagram.vortex.entities.builders.TagBuilder;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TagManager {
    private final String guildId;

    public TagManager(String guildId) {
        this.guildId = guildId;
    }

    public void createTag(@Nonnull String name, @Nonnull String value, @Nonnull String userId) {
        try {
            DatabaseUtils.execute("INSERT INTO tags (title, value, added_by, guild_id) VALUES (?,?,?,?)", name, value, userId, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    @Nullable
    public Tag getTag(@Nonnull String name) {
        try {
            var result = DatabaseUtils.executeQuery("SELECT * FROM tags WHERE title = ?", name);

            var title = (String) result.get("title");
            var value = (String) result.get("value");
            var addedBy = (String) result.get("added_by");
            var guildId = (String) result.get("guild_id");

            return new TagBuilder()
                    .setName(title)
                    .setValue(value)
                    .setAddedBy(addedBy)
                    .setGuildId(guildId)
                    .build();

        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
        return null;
    }

    public void deleteTag(@Nonnull String name) {
        try {
            DatabaseUtils.execute("DELETE FROM tags WHERE title = ? AND guild_id = ?", name, this.guildId);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }

    }
}
