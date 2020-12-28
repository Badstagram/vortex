package me.badstagram.vortex.events;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

public class CommandExecutedEvent extends CommandEvent {
    public CommandExecutedEvent(@NotNull JDA api, Command cmd, CommandContext ctx) {
        super(api, cmd, ctx);
    }
}
