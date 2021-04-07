package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SQL extends Command {
    public SQL() {
        this.name = "sql";
        this.help = "Run SQL queries using the bot";
        this.owner = true;
        this.usage = "sql <query>";
        this.category = new Category("Admin");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var result = this.getSqlOutput(
                    MarkdownSanitizer.sanitize(ctx.getEvent().getMessage().getContentRaw().split("\\s+", 2)[1],
                            MarkdownSanitizer.SanitizationStrategy.REMOVE));

            var self = ctx.getSelfMember();

            if (result.length() - "`````".length() > Message.MAX_CONTENT_LENGTH && self.hasPermission(ctx.getChannel(), Permission.MESSAGE_ATTACH_FILES)) {
                ctx.getChannel()
                        .sendMessage("Output too long. Sending as file.")
                        .addFile(result.replaceAll("`", "").getBytes(), "output.txt")
                        .queue();
                return;
            } else if (!self.hasPermission(ctx.getChannel(), Permission.MESSAGE_ATTACH_FILES)) {
                ctx.getChannel()
                        .sendMessage("Output too long. Sending as file.")
                        .addFile(result.replaceAll("`", "").getBytes(), "output.txt")
                        .queue();
                return;
            }

            ctx.getChannel()
                    .sendMessage(result)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }

    protected String getSqlOutput(@Nonnull String sql) throws Exception {
        var proc = new ProcessBuilder()
                .command("docker", "exec", "postgres", "psql", "-U", "postgres", "-c", sql)
                .start();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            StringBuilder lines = new StringBuilder();
            String currentLine;

            while ((currentLine = input.readLine()) != null) {
                lines.append(currentLine).append("\n");
            }

            return MarkdownUtil.codeblock(lines.toString());
        }
    }
}
