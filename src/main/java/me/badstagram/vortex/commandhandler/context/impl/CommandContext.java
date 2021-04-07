package me.badstagram.vortex.commandhandler.context.impl;

import me.badstagram.vortex.commandhandler.CommandClient;
import me.badstagram.vortex.commandhandler.context.ICommandContext;
import me.badstagram.vortex.core.Constants;
import me.badstagram.vortex.entities.VortexMember;
import me.badstagram.vortex.entities.impl.VortexMemberImpl;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private String args;
    private final CommandClient client;


    public CommandContext(GuildMessageReceivedEvent event, String args, CommandClient client) {
        this.event = event;
        this.args = args == null ? "" : args;
        this.client = client;
    }

    @Override
    public List<String> getArgs() {
        final String[] split = this.event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");

        return Arrays.asList(split).subList(1, split.length);
    }

    @Override
    public Guild getGuild() {
        return this.event.getGuild();
    }

    @Override
    public VortexMember getMember() {
        return new VortexMemberImpl(this.event.getMember());
    }

    @Override
    public User getAuthor() {
        return this.event.getAuthor();
    }

    @Override
    public JDA getJDA() {
        return this.event.getJDA();
    }

    @Override
    public Message getMessage() {
        return this.event.getMessage();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    @Override
    public TextChannel getChannel() {
        return this.event.getChannel();
    }

    @Override
    public Member getSelfMember() {
        return this.event.getGuild().getSelfMember();
    }

    @Override
    public SelfUser getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    @Override
    public void reply(String msg) {

        this.getMessage()
                .reply(msg)
                .mentionRepliedUser(false)
                .queue();
    }

    @Override
    public void replyOrDefault(MessageEmbed embed, String msg) {
        var self = this.getSelfMember();

        if (!self.hasPermission(this.getChannel(), Permission.MESSAGE_EMBED_LINKS)) {
            this.getChannel().sendMessage(msg).queue();
            return;
        }
        this.getChannel().sendMessage(embed).queue();

    }

    @Override
    public void replyPinging(String msg) {
        this.getMessage()
                .reply(msg)
                .mentionRepliedUser(true)
                .queue();
    }

    @Override
    public CommandClient getClient() {
        return this.client;
    }

    @Override
    public CommandContext setArgs(String args) {
        this.args = args;
        return this;
    }

    @Override
    public ArgumentParser createArgumentParser() {
        return new ArgumentParser(this);
    }
}
