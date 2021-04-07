package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.enums.GBDefraIndex;
import me.badstagram.vortex.entities.enums.USEPAIndex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.HttpUrl;
import okhttp3.Request;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Instant;

public class Weather extends Command {

    public Weather() {
        this.name = "weather";
        this.help = "Get the weather for a location";
        this.usage = "weather <location>";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("Info");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();
        if (args.isEmpty())
            throw new BadArgumentException("Location");

        try {
            var locationString = String.join(" ", args);


            var weather = this.getWeather(locationString);

            if (weather == null) {
                ctx.getChannel()
                        .sendMessage("Something went wrong while getting the weather for that location. Try again later.")
                        .queue();
                return;
            }
            var location = weather.getObject("location");
            var currentWeather = weather.getObject("current");
            var condition = currentWeather.getObject("condition");
            var airQuality = currentWeather.getObject("air_quality");

            // location
            var name = location.getString("name");
            var region = location.getString("region");
            var country = location.getString("country");
            var localTime = Instant.ofEpochSecond(location.getLong("localtime_epoch")).toEpochMilli();

            // current weather
            var tempC = (Double) currentWeather.get("temp_c");
            var tempF = (Double) currentWeather.get("temp_f");
            var windMph = (Double) currentWeather.get("wind_mph");
            var windKph = (Double) currentWeather.get("wind_kph");
            var gustMph = (Double) currentWeather.get("gust_mph");
            var gustKph = (Double) currentWeather.get("gust_kph");
            var uvIndex = (Double) currentWeather.get("uv");
            var day = currentWeather.getInt("is_day") == 1;
            var windDegree = currentWeather.getInt("wind_degree");
            var windDir = currentWeather.getString("wind_dir");

            // current condition
            var text = condition.getString("text");
            var icon = condition.getString("icon")
                    .replaceFirst("//", "https://");

            // air quality
            var usEPA = USEPAIndex.fromIndex(airQuality.getInt("us-epa-index"));
            var gbDefra = GBDefraIndex.fromIndex(airQuality.getInt("gb-defra-index"));


            var embed = new EmbedBuilder()
                    .setColor(Colors.SUCCESS.getAsColor())
                    .setTitle(String.format("%s, %s, %s", name, region, country))
                    .addField("Condition", text, true)
                    .addBlankField(true)
                    .addBlankField(true)
                    .addField("Temperature (°C)", String.valueOf(tempC), true)
                    .addField("Temperature (°F)", String.valueOf(tempF), true)
                    .addBlankField(true)
                    .addField("Wind (MPH)", String.valueOf(windMph), true)
                    .addField("Wind (KPH)", String.valueOf(windKph), true)
                    .addField("Wind Degree", String.valueOf(windDegree), true)
                    .addField("Wind Direction", windDir + "°", true)
                    .addBlankField(true)
                    .addBlankField(true)
                    .addField("Gust (MPH)", String.valueOf(gustMph), true)
                    .addField("Gust (KPH)", String.valueOf(gustKph), true)
                    .addBlankField(true)
                    .addField("UV Index", String.valueOf(uvIndex), true)
                    .addBlankField(true)
                    .addBlankField(true)
                    .addField("Air Quality (US EPA)", usEPA.getBand(), true)
                    .addField("Air Quality (GB Defra)", gbDefra.getBand(), true)
                    .addBlankField(true)
                    .setThumbnail(icon)
                    .setFooter("Powered by https://www.weatherapi.com/ | Local time %s".formatted(FormatUtil.formatDate(localTime)))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    @Nullable
    protected final DataObject getWeather(@Nonnull String location) throws IOException {
        var client = Vortex.getHttpClient();

        var url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.weatherapi.com")
                .addPathSegments("v1/current.json")
                .addQueryParameter("key", Config.get("weather_token"))
                .addQueryParameter("q", location)
                .addQueryParameter("aqi", "yes")
                .build();


        var req = new Request.Builder()
                .url(url)
                .build();

        var res = client.newCall(req)
                .execute();

        var body = res.body();

        if (!res.isSuccessful() || body == null)
            return null;


        return DataObject.fromJson(body.string());
    }
}
