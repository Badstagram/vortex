package me.badstagram.vortex.events;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class CommandEvent extends Event {
    private final Command cmd;
    private final CommandContext ctx;
    public CommandEvent(@NotNull JDA api, @Nonnull Command cmd,
            CommandContext ctx) {
        super(api);

        this.cmd = cmd;
        this.ctx = ctx;
    }

    public Command getCmd() {
        return cmd;
    }

    public CommandContext getCtx() {
        return ctx;
    }
}
