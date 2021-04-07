package me.badstagram.vortex.commandhandler;


import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.Checks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandClient extends ListenerAdapter {
    protected final Map<String, OffsetDateTime> cooldowns = new HashMap<>();
    private final String ownerId;
    private final String[] coOwnerIds;
    private final String prefix;
    private final HashMap<String, Command> commands;
    private final HashMap<String, Command> aliases;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, Integer> commandUsages = new HashMap<>();


    public CommandClient(String ownerId, String[] coOwnerIds, String prefix, HashMap<String, Command> commands, HashMap<String, Command> aliases) {

        this.ownerId = ownerId;
        this.coOwnerIds = coOwnerIds;
        this.prefix = prefix;
        this.commands = commands;
        this.aliases = aliases;

        Checks.check(ownerId != null,
                "Owner ID was set null or not set! Please provide an User ID to register as the owner!");
        Checks.isSnowflake(this.ownerId);


    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }

    @Nullable
    public Command getCommand(String name) {
        return this.getCommands().getOrDefault(name, null);
    }

    // handle command
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        // ignore bots
        if (event.getAuthor().isBot()) {
            return;
        }

        String rawContent = event.getMessage().getContentRaw();

        String[] parts = null;
        String prefix = this.prefix;

        if (rawContent.toLowerCase().startsWith(prefix.toLowerCase())) {
            parts = splitOnPrefix(rawContent, prefix.length());
        }

        // if command doesnt start with a valid prefix, we can't use it so return
        if (parts == null) {
            return;
        }

        String name = parts[0];
        String args = parts[1] == null ? "" : parts[1];
        Command command = this.commands.getOrDefault(name, null); // this will be null if it's not a command

        // message wasn't a command, so check if an alias exists
        if (command == null) {
            command = this.aliases.getOrDefault(name, null);
        }

        // no command or alias exists, so return
        if (command == null) {
            return;
        }

        this.logger.debug("args[1] = {}", parts[1]);
        CommandContext ctx = new CommandContext(event, args, this);


        this.commandUsages.merge(name, 1, Integer::sum);
        command.run(ctx);


    }

    private String[] splitOnPrefix(String content, int len) {
        return Arrays.copyOf(content.substring(len).trim().split("\\s+", 2), 2);
    }

    public Map<String, Integer> getCommandUsages() {
        return commandUsages;
    }

    public void removeCommand(Command cmd) {
        this.commands.remove(cmd.getName());
    }

    public void removeCommand(String name) {
        this.commands.remove(name);
    }



}