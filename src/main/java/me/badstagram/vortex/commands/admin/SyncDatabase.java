package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class SyncDatabase extends Command {
    public SyncDatabase() {
        this.name = "syncdb";
        this.help = "Sync all the databases used.";
        this.owner = true;
        this.category = new Category("Admin");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var mongo = Vortex.getMongo();

        var database = mongo.getDatabase("Vortex");

        var collection = database.getCollection("guild_config");
        var documents = new ArrayList<Document>();

        ctx.reply("[!] Syncing Database...");

        for (var g : ctx.getJDA().getGuilds()) {

/*            if (collection.find(eq(g.getId())).first() != null) {
                continue;
            }*/


            var document = new Document("_id", g.getId())
                    .append("guild_id", g.getId())
                    .append("welcome_message", "Welcome {member.mention} to {guild.name")
                    .append("leave_message", "{member.mention} has left {guild.name}")
                    .append("anti_invite_enabled", "true")
                    .append("max_joins_per_hour_threshold", "5")
                    .append("mass_mention_threshold", "5")
                    .append("mass_mention_action", "Kick")
                    .append("is_premium", "false")
                    .append("punish_log_channel", null)
                    .append("moderation_log_channel", null)
                    .append("muted_role", null)
                    .append("member_log", null);

            documents.add(document);
        }

        collection.insertMany(documents);
    }
}
