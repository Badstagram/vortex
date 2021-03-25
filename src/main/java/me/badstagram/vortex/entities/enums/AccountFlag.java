package me.badstagram.vortex.entities.enums;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public enum AccountFlag {
    NEW_ACCOUNT("New Account (< 7 Days old)", "\\uD83D\\uDC76"),
    KSOFT_BANNED("Banned on KSoft.Si (use `-checkban` for more info)", "\\uD83D\\uDEAB");

    private final String name, emoji;

    AccountFlag(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public static List<AccountFlag> getFlagsForUser(User user) {
        List<AccountFlag> flagList = new ArrayList<>();

        var timeIn7Days = OffsetDateTime.now().plusDays(7L).toEpochSecond();
        var timeAccountCreated = user.getTimeCreated().toEpochSecond();

        if (timeAccountCreated < timeIn7Days) flagList.add(NEW_ACCOUNT);

        var ban = Vortex.getKSoftAPI()
                .getBan()
                .setUserId(user.getId())
                .execute();

        if (ban.exists()) flagList.add(KSOFT_BANNED);


        return flagList;
    }
}
