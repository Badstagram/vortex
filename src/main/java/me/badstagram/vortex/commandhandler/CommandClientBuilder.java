package me.badstagram.vortex.commandhandler;


import me.badstagram.vortex.core.Constants;

import java.util.*;

public class CommandClientBuilder {
    private String ownerId = "";
    private String[] coOwnerIds = new String[0];
    private String prefix = Constants.PREFIX;
    private final HashMap<String, Command> commands = new HashMap<>();
    private final HashMap<String, Command> aliases = new HashMap<>();

    public CommandClientBuilder setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public CommandClientBuilder setCoOwnerIds(String[] coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
        return this;
    }

    public CommandClientBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandClientBuilder addCommand(Command cmd) {
        String name = cmd.name;

        if (this.commands.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Command %s is already registered", name));
        }
        this.commands.put(name, cmd);

        for (var alias : cmd.aliases) {
            if (aliases.containsKey(alias)) {
                throw new IllegalArgumentException("Command with alias %s is already added!".formatted(alias));
            }
            aliases.put(alias, cmd);
        }

        return this;
    }

    public CommandClientBuilder addCommands(Command... cmds) {

        for (Command cmd : cmds) {
            this.addCommand(cmd);

            if (cmd.aliases.length == 0){
                continue;
            }
            for (var alias : cmd.aliases) {
                if (aliases.containsKey(alias)) {
                    throw new IllegalArgumentException("Command with alias %s is already added!".formatted(alias));
                }
                aliases.put(alias, cmd);
            }
        }
        return this;
    }

    public CommandClient build() {
        return new CommandClient(ownerId, coOwnerIds, prefix, commands, aliases);
    }
}