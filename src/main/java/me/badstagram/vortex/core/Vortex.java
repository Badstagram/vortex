package me.badstagram.vortex.core;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.sentry.Sentry;
import me.badstagram.vortex.cache.MessageCache;
import me.badstagram.vortex.commandhandler.CommandClient;
import me.badstagram.vortex.commandhandler.CommandClientBuilder;
import me.badstagram.vortex.commands.admin.*;
import me.badstagram.vortex.commands.config.ConfigCommand;
import me.badstagram.vortex.commands.economy.*;
import me.badstagram.vortex.commands.filter.Filter;
import me.badstagram.vortex.commands.fun.Avatar;
import me.badstagram.vortex.commands.fun.Define;
import me.badstagram.vortex.commands.fun.EightBall;
import me.badstagram.vortex.commands.fun.ProgressBar;
import me.badstagram.vortex.commands.globalbans.GBan;
import me.badstagram.vortex.commands.info.*;
import me.badstagram.vortex.commands.moderation.*;
import me.badstagram.vortex.commands.status.Status;
import me.badstagram.vortex.listeners.BulkMessageDeleteListener;
import me.badstagram.vortex.listeners.MessageEditListener;
import me.badstagram.vortex.listeners.MessageListener;
import me.badstagram.vortex.listeners.OnReadyListener;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.explodingbush.ksoftapi.KSoftAPI;
import okhttp3.OkHttpClient;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Random;

public class Vortex {
    private static final boolean devMode = true;
    private static final Logger log = LoggerFactory.getLogger(Vortex.class);
    private static final MessageCache cache = new MessageCache();
    private static final EventWaiter waiter = new EventWaiter();
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static JDA jda;
    private static KSoftAPI kSoftAPI;
    private static Random rnd;
    private static MongoClient mongo = null;
    private static CommandClient commandClient = null;


    public static void main(String[] args) {

        Sentry.init(options -> options.setDsn(Config.get("sentry")));

        try {
            jda = login(System.getenv("VORTEX_TOKEN"));
            commandClient = new CommandClientBuilder()
                    .setPrefix(Constants.PREFIX)
                    .setOwnerId("424239181296959507")
                    .addCommand(new Ping())
                    .addCommand(new ErrorTest())
                    .addCommand(new UserInfo())
                    .addCommand(new ProgressBar())
                    .addCommand(new SyncDatabase())
                    .addCommand(new GBan())
                    .addCommand(new Eval())
                    .addCommand(new InviteLookup())
                    .addCommand(new EightBall())
                    .addCommand(new Avatar())
                    .addCommand(new Daily())
                    .addCommand(new BotInfo())
                    .addCommand(new SQL())
                    .addCommand(new Hourly())
                    .addCommand(new Balance())
                    .addCommand(new Work())
                    .addCommand(new Ban())
                    .addCommand(new Kick())
                    .addCommand(new Warn())
                    .addCommand(new Case())
                    .addCommand(new HowLong())
                    .addCommand(new Status())
                    .addCommand(new ConfigCommand())
                    .addCommand(new Define())
                    .addCommand(new History())
                    .addCommand(new TranslateCommand())
                    .addCommand(new Filter())
                    .addCommand(new Pay())
                    .addCommand(new Rep())
                    .build();

            jda.addEventListener(commandClient);

        } catch (Exception e) {
            e.printStackTrace();
            ErrorHandler.handle(e);
            log.error("There was an error and Vortex was unable to start up!", e);
            System.exit(-1);
        }

    }

    protected static JDA login(@Nonnull String token) throws Exception {
        return JDABuilder.createDefault(token)
                .disableCache(CacheFlag.ACTIVITY)
                .enableCache(CacheFlag.VOICE_STATE)
                .enableCache(CacheFlag.CLIENT_STATUS)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_PRESENCES)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setRawEventsEnabled(devMode)
                .setActivity(Activity.playing("Restarting..."))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(waiter,
                        new OnReadyListener(),
                        new MessageListener(),
                        new MessageEditListener(),
                        new BulkMessageDeleteListener())
                .setBulkDeleteSplittingEnabled(false)
                .build()
                .awaitReady();

    }

    public static boolean isDevMode() {
        return getRunMode() == RunMode.DEVELOPMENT;
    }

    public static Random getRandom() {
        if (rnd == null) {
            rnd = new Random();
        }
        return rnd;
    }

    public static KSoftAPI getKSoftAPI() {
        if (kSoftAPI == null) {
            kSoftAPI = new KSoftAPI(System.getenv("KSOFT_TOKEN"));
        }
        return kSoftAPI;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static MongoClient getMongo() {
        if (mongo == null) {
            mongo = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(Config.get("mongo_connection_string")))
                            .build()
            );
        }
        return mongo;
    }

    public static MessageCache getCache() {
        return cache;
    }

    public static EventWaiter getWaiter() {
        return waiter;
    }

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static RunMode getRunMode() {
        if (getJDA() == null) {
            return RunMode.fromBase64(Config.get("token").split("\\.", 1)[0]);
        }
        return RunMode.fromId(getJDA().getSelfUser().getId());

    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass());
    }

    public static CommandClient getCommandClient() {
        return commandClient;
    }
}
