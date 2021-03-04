package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MiscUtil {
    public static boolean listContainsElement(List<?> list, Object element) {
        return list.contains(element);
    }

    public static String postToHasteBin(String text) throws IOException {
        
        static final BASE_URL = "https://hastebin.de/"
        
        var client = Vortex.getHttpClient();
        var url = new URL(BASE_URL + "documents");

        var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("text/plain"), text))
                .build();

        var response = client.newCall(request).execute();

        if (response.body() == null || !response.isSuccessful()) return "null";

        var data = DataObject.fromJson(response.body().string());

        return BASE_URL + data.getString("key")
    }
}
