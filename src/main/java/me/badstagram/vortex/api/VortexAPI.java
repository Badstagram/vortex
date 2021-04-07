package me.badstagram.vortex.api;


import io.javalin.Javalin;
import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VortexAPI {
    private final Logger logger;
    private final JDA jda;
    private final List<String> adminTokens;

    public VortexAPI(JDA jda) throws IOException {
        this.logger = Vortex.getLogger();
        this.jda = jda;
        this.adminTokens = DataObject.fromJson(String.join("\n", Files.readAllLines(Paths.get("./tokens.json"))))
                .getArray("admin_tokens")
                .toList()
        .stream()
        .filter(o -> o instanceof String)
        .map(o -> (String) o)
        .collect(Collectors.toList());
    }

    public void start() {
        Javalin app = Javalin.create(config -> config.defaultContentType = "application/json")
                .start(2000);

        app.exception(Exception.class, (e, ctx) -> {
            this.logger.error("[API]", e);

            ctx.status(500)
                    .result(this.getErrorJSON(500, "Internal Server Error", e.getMessage(), ctx.path()));

        });

        app.before("/api/*", ctx -> this.logger.info("[API] {} request on {}", ctx.method(), ctx.path()));

        app.get("/api/status/", ctx -> {
            DataObject data = DataObject.empty()
                    .put("fully_ready", Vortex.isReady())
                    .put("guild_count", this.jda.getGuilds()
                            .size())
                    .put("user_count", this.jda.getUsers()
                            .size());



            ctx.result(data.toString());
        });

        app.post("/api/admin/stop/", ctx -> {
            var auth = ctx.header("Authorization");


            if (auth == null || !this.adminTokens.contains(auth)) {

                ctx.status(401)
                        .result(this.getErrorJSON(401, "forbidden", "Authorization credentials incorrect or not provided", ctx.path()));
                return;
            }

            this.jda.shutdown();

            ctx.result(
                    DataObject.empty()
                    .put("success", true)
                    .toString()
            );

        });
    }

    protected String getErrorJSON(int code, String message, String details, String path) {
        return DataObject.empty()
                .put("error", true)
                .put("code", code)
                .put("message", message)
                .put("details", details)
                .put("path", path)
                .toString();
    }


}
