package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.data.DataObject;

import javax.annotation.Nullable;

import static com.mongodb.client.model.Filters.eq;

public class OAuth2Util {
    @Nullable
    public static String getOAuth2Token(String userId) {
        var mongoClient = Vortex.getMongo();
        var db = mongoClient.getDatabase("vortex");
        var collection = db.getCollection("OAuth2_tokens");

        var found = collection.find(eq("user_id", userId))
                .first();

        if (found == null)
            return null;

        return DataObject.fromJson(found.toJson())
                .getString("token");


    }

    public static String getOAuth2Token(User user) {
        return getOAuth2Token(user.getId());
    }

    public static String getOAuth2Token(Member member) {
        return getOAuth2Token(member.getId());
    }
}
