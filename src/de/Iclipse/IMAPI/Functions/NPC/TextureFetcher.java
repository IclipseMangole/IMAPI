package de.Iclipse.IMAPI.Functions.NPC;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TextureFetcher {
    private static final String TEXTURE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";

    private static Map<UUID, String[]> skinCache = new HashMap<>();


    public static String getTexture(UUID uuid) {
        String id = uuid.toString().replace("-", "");
        System.out.println(id);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(TEXTURE_URL, id)).openConnection();
            connection.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
            return obj.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSignature(UUID uuid) {
        String id = uuid.toString().replace("-", "");
        try {
            System.out.println(String.format(TEXTURE_URL, id));
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(TEXTURE_URL, id)).openConnection();
            connection.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();
            return obj.get("properties").getAsJsonArray().get(0).getAsJsonObject().get("signature").getAsString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getSkin(UUID uuid) {
        if (skinCache.containsKey(uuid)) {
            return skinCache.get(uuid);
        }
        String[] s = new String[]{getTexture(uuid), getSignature(uuid)};
        skinCache.put(uuid, s);
        return s;
    }
}
