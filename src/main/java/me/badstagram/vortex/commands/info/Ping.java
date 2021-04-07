package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.MarkdownUtil;


public class Ping extends Command {


    public Ping() {
        this.name = "ping";
        this.help = "Get the ping to discord";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.aliases = new String[] { "\uD83C\uDFD3" };
        this.category = new Category("Info");



    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException {
        try {
            var jda = ctx.getJDA();

            var gateway = jda.getGatewayPing();

            jda.getRestPing().submit()
                    .thenAccept(rest -> {
                        var embed = EmbedUtil.createDefault()
                                .setTitle("Pong")
                                .addField("Gateway", MarkdownUtil.monospace(gateway + "ms"), false)
                                .addField("Rest", MarkdownUtil.monospace(rest + "ms"), false)
                                .build();

                        ctx.replyOrDefault(embed, "Gateway: `%s`. Rest: `%s`".formatted(gateway, rest));

                    });
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }
}
