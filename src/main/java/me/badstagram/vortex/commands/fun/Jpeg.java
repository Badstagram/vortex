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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class Jpeg extends Command {
    public Jpeg() {
        this.name = "jpeg";
        this.help = "Needs more jpeg";

        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {

        try {
            var attachments = ctx.getMessage()
                    .getAttachments();
            var url = "";

            if (attachments.isEmpty()) {
                var users = ctx.createArgumentParser()
                        .parseUser();
                if (users.isEmpty())
                    throw new BadArgumentException("user");

                url = users.get(0)
                        .getEffectiveAvatarUrl();
            } else {

                ctx.getChannel().sendTyping().queue();
                var attachment = attachments.get(0);

                if (!attachment.isImage()) {
                    throw new BadArgumentException("attachment", false);
                }

                url = attachment.getUrl();
            }

            var bytes = this.getImage(url);

            if (bytes.length > Message.MAX_FILE_SIZE) {
                ctx.getChannel()
                        .sendMessage("Resulting file was too large.")
                        .queue();
                return;
            }

            ctx.getChannel().sendFile(bytes, "jpeg.%s".formatted(url.substring(url.length() - 3))).queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected byte[] getImage(String url) throws IOException {
        var requestBody = DataObject.empty()
                .put("images", DataArray.empty()
                        .add(url))
                .put("quality", 1)
                .toString();

        var request = new Request.Builder()
                .url("https://api.pxlapi.dev/jpeg")
                .addHeader("Authorization", "Application " + Config.get("pxlapi"))
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                .build();

        var response = Vortex.getHttpClient()
                .newCall(request)
                .execute();

        var body = response.body();

        if (!response.isSuccessful() || body == null)
            return new byte[0];

        return body.bytes();
    }
}
