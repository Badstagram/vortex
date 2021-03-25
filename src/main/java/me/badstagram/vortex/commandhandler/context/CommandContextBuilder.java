package me.badstagram.vortex.commandhandler.context;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandContextBuilder {
    private List<String> args;
    private Member member;
    private Message message;

    public CommandContextBuilder setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    public CommandContextBuilder setMember(Member member) {
        this.member = member;
        return this;
    }

    public CommandContextBuilder setMessage(Message message) {
        this.message = message;
        return this;
    }


}
