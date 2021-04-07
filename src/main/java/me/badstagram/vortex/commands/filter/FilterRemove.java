package me.badstagram.vortex.commands.filter;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;

public class FilterRemove extends SubCommand {
    public FilterRemove() {
        this.category = new Category("Filter");

    }

    @Override
    public void execute(SubCommandContext ctx) {

    }


}
