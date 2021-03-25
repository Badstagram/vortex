package me.badstagram.vortex.exceptions;

import me.badstagram.vortex.entities.enums.GuildPunishmentType;
import net.dv8tion.jda.api.entities.Member;

public class CantPunishException extends Exception {
    private final Member member;
    private final GuildPunishmentType type;


    /**
     * Thrown when a Member can't be punished.
     *
     * @param member
     *         The Member that can't be punished
     * @param type
     *         The type of punishment
     */
    public CantPunishException(Member member, GuildPunishmentType type) {
        super();
        this.member = member;
        this.type = type;
    }


    public Member getMember() {
        return this.member;
    }

    public GuildPunishmentType getType() {
        return this.type;
    }
}
