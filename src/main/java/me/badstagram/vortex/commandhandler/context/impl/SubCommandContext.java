package me.badstagram.vortex.commandhandler.context.impl;

import me.badstagram.vortex.commandhandler.CommandClient;
import me.badstagram.vortex.commandhandler.context.ISubCommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class SubCommandContext implements ISubCommandContext {

    private final CommandContext oldCtx;
    private final int layersDeep;


    public SubCommandContext(CommandContext oldCtx) {
        this.layersDeep = 1;
        this.oldCtx = oldCtx;
    }

    public SubCommandContext(CommandContext oldCtx, int layersDeep) {
        this.oldCtx = oldCtx;
        this.layersDeep = layersDeep;
    }

    @Override
    public List<String> getArgs() {
        return this.oldCtx.getArgs().subList(this.layersDeep, this.oldCtx.getArgs().size());
    }

    @Override
    public Guild getGuild() {
        return this.oldCtx.getGuild();
    }

    @Override
    public Member getMember() {
        return this.oldCtx.getMember();
    }

    @Override
    public User getAuthor() {
        return this.oldCtx.getAuthor();
    }

    @Override
    public JDA getJDA() {
        return this.oldCtx.getJDA();
    }

    @Override
    public Message getMessage() {
        return this.oldCtx.getMessage();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.oldCtx.getEvent();
    }

    @Override
    public TextChannel getChannel() {
        return this.oldCtx.getChannel();
    }

    @Override
    public Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }

    @Override
    public SelfUser getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    @Override
    public void reply(String msg) {
        this.oldCtx.reply(msg);
    }

    @Override
    public void replyOrDefault(MessageEmbed embed, String msg) {
        this.oldCtx.replyOrDefault(embed, msg);
    }

    @Override
    public void replyPinging(String msg) {
        this.oldCtx.replyPinging(msg);
    }

    @Override
    public CommandClient getClient() {
        return this.oldCtx.getClient();
    }

    @Override
    public CommandContext getOldCtx() {
        return this.oldCtx;
    }
}
