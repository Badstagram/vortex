package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class Thonkify extends Command {
    public Thonkify() {
        this.name = "thonkify";
        this.help = "Thonk";
        this.usage = "thonkify <text>";
        this.aliases = new String[] { "thonk" };
        this.botPermissions = new Permission[] { Permission.MESSAGE_ATTACH_FILES };
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("text");

        var text = String.join(" ", args);

        try {

            ctx.getChannel()
                    .sendTyping()
                    .queue();

            var bytes = this.getImage(text);

            if (bytes == null) {
                ctx.getChannel()
                        .sendMessage("Something went wrong")
                        .queue();
                return;
            }

            ctx.getChannel()
                    .sendFile(bytes, "thonk.png")
                    .queue();


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }

    protected byte[] getImage(String text) throws IOException {

        var reqBody = DataObject.empty()
                .put("text", text)
                .toString();

        var req = new Request.Builder()
                .url("https://api.pxlapi.dev/thonkify")
                .addHeader("Authorization", "Application " + Config.get("pxlapi"))
                .post(RequestBody.create(MediaType.parse("application/json"), reqBody))
                .build();

        var res = Vortex.getHttpClient()
                .newCall(req)
                .execute();

        var resBody = res.body();
        if (resBody == null || !res.isSuccessful())
            return null;

        return resBody.bytes();
    }
}
