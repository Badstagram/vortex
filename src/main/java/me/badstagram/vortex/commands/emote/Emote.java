package me.badstagram.vortex.commands.emote;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Emote extends Command {
    public Emote() {
        this.name = "emote";
        this.help = "Commands to manage guild emotes";
        this.usage = "emote <create|remove|steal> <emote>";
        this.subCommands = new Command[]{new Steal(), new Create(), new Remove()};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        throw new BadArgumentException("sub command");
    }
}
