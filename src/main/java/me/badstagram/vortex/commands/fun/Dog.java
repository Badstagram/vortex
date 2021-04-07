package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;
import okhttp3.Request;

import java.io.IOException;
import java.net.URL;

public class Dog extends Command {
    public Dog() {
        this.name = "dog";
        this.aliases = new String[]{"floof"};
        this.help = "DOGOOOOOOOO";
        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("Fun");


    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            ctx.getChannel()
                    .sendFile(this.getFloofImage(), "floof.jpg")
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected byte[] getFloofImage() throws IOException {

        var client = Vortex.getHttpClient();


        var req = new Request.Builder()
                .url(new URL("https://samoyed.pics"))
                .build();

        var res = client.newCall(req)
                .execute();

        var body = res.body();

        if (body == null || !res.isSuccessful())
            return null;

        return body.bytes();
    }
}
