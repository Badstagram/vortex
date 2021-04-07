package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URL;

public class OCR extends Command {
    public OCR() {
        this.name = "ocr";
        this.help = "Find text inside an image";
        this.usage = "ocr";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.userPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var attachments = ctx.getMessage()
                .getAttachments();

        if (attachments.isEmpty()) {
            ctx.getChannel()
                    .sendMessage(":x: You need to attach the image.")
                    .queue();
            return;
        }

        var attachment = attachments.get(0);

        if (!attachment.isImage()) {
            throw new BadArgumentException("image", false);
        }

        try {
            var ocrData = this.getOCRData(attachment);

            if (ocrData == null) {
                ctx.getChannel()
                        .sendMessage(":x: There was a problem getting the OCR data.")
                        .queue();
                return;
            }

            var text = ocrData.getArray("ParsedResults")
                    .getObject(0)
                    .getString("ParsedText");

            if (text.isEmpty()) {
                ctx.getChannel()
                        .sendMessage("There was no text to extract.")
                        .queue();
                return;
            }


            var embed = new EmbedBuilder()
                    .setImage(attachment.getUrl())
                    .setDescription(MarkdownUtil.codeblock(null, text))
                    .build();


            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    public DataObject getOCRData(Message.Attachment attachment) throws IOException {

        var client = Vortex.getHttpClient();

        var url = attachment.getUrl();
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("url", url)
                .build();


        var req = new Request.Builder()
                .url(new URL("https://api.ocr.space/parse/image"))
                .post(body)
                .addHeader("apikey", Config.get("ocr_space_api_key"))
                .build();


        var res = client.newCall(req)
                .execute();

        if (!res.isSuccessful())
            return null;

        var resBody = res.body();

        if (resBody == null)
            return null;

        return DataObject.fromJson(resBody.string());
    }
}
