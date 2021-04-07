package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.Nonnull;
import java.io.IOException;

public class SafeEval extends Command {
    public SafeEval() {
        this.name = "safeeval";
        this.help = "Safely evaluate code";
        this.usage = "safeeval <code>";
        this.aliases = new String[] { "sev" };
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        ctx.getChannel().sendTyping().queue();

        try {
            var outputData = this.getOutput(ctx);
            var stdout = outputData.getString("stdout");
            var stderr = outputData.getString("stderr");
            var response = "";

            if (stdout.isEmpty() && !stderr.isEmpty()) {
                response = """
                        I only received error output.
                        ```java
                        %s
                        ```
                        """.formatted(stderr);

            } else if (!stdout.isEmpty()) {
                response = """
                        Here is the output.
                        ```java
                        %s
                        ```
                        """.formatted(stdout);
            } else {
                response = "I got no output.";
            }

            ctx.replyPinging(response);
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }

    protected DataObject getOutput(@Nonnull CommandContext ctx) throws IOException {
        var code = MarkdownSanitizer.sanitize(String.join(" ", ctx.getArgs()), MarkdownSanitizer.SanitizationStrategy.REMOVE);

//        if (!code.endsWith(";") && !code.endsWith("}")) code += ";";


        var source = """
                public class Main 
                {
                    public static void main(String[] args) throws Exception
                    {
                        %s
                    }
                }
                """.formatted(code);

        var body = DataObject.empty()
                .put("language", "java")
                .put("source", source);

        var client = Vortex.getHttpClient();

        var request = new Request.Builder()
                .url("https://emkc.org/api/v1/piston/execute")
                .post(RequestBody.create(MediaType.parse("application/json"), body.toString()))
                .build();

        var response = client.newCall(request)
                .execute();

        var resBody = response.body();
        if (!response.isSuccessful() || resBody == null) return null;

        return DataObject.fromJson(resBody.string());

    }
}
