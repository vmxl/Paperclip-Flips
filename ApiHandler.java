// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.utils;

import com.google.gson.JsonObject;
import java.util.Map;
import java.io.IOException;
import com.google.gson.JsonArray;
import java.util.Iterator;
import java.util.List;
import net.minecraft.scoreboard.Scoreboard;
import java.time.Instant;
import java.util.Objects;
import me.paperclip.paperclipflipper.Authenticator;
import me.paperclip.paperclipflipper.Config;
import com.google.gson.JsonElement;
import me.paperclip.paperclipflipper.Main;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Collection;
import net.minecraft.scoreboard.Score;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;

public class ApiHandler
{
    public static void updatePurse() throws IOException {
        if (Utils.isOnSkyblock()) {
            final Scoreboard scoreboard = Minecraft.func_71410_x().field_71441_e.func_96441_U();
            final List<Score> scores = new LinkedList<Score>(scoreboard.func_96534_i(scoreboard.func_96539_a(1)));
            for (final Score score : scores) {
                final ScorePlayerTeam scorePlayerTeam = scoreboard.func_96509_i(score.func_96653_e());
                final String line = Utils.removeColorCodes(ScorePlayerTeam.func_96667_a((Team)scorePlayerTeam, score.func_96653_e()));
                if (line.contains("Purse: ") || line.contains("Piggy: ")) {
                    Main.balance = Double.parseDouble(line.replaceAll("\\(\\+[\\d]+\\)", "").replaceAll("[^\\d.]", ""));
                    return;
                }
            }
        }
        final JsonArray profilesArray = Objects.requireNonNull(Utils.getJson("https://api.hypixel.net/skyblock/profiles?key=" + Config.apiKey + "&uuid=" + Authenticator.myUUID)).getAsJsonObject().getAsJsonArray("profiles");
        int profileIndex = 0;
        Instant lastProfileSave = Instant.EPOCH;
        for (int i = 0; i < profilesArray.size(); ++i) {
            Instant lastSaveLoop;
            try {
                lastSaveLoop = Instant.ofEpochMilli(profilesArray.get(i).getAsJsonObject().get("members").getAsJsonObject().get(Authenticator.myUUID).getAsJsonObject().get("last_save").getAsLong());
            }
            catch (Exception e) {
                continue;
            }
            if (lastSaveLoop.isAfter(lastProfileSave)) {
                profileIndex = i;
                lastProfileSave = lastSaveLoop;
            }
        }
        Main.balance = profilesArray.get(profileIndex).getAsJsonObject().get("members").getAsJsonObject().get(Authenticator.myUUID).getAsJsonObject().get("coin_purse").getAsDouble();
    }
    
    public static void updateBazaar() throws IOException {
        final JsonObject products = Objects.requireNonNull(Utils.getJson("https://api.hypixel.net/skyblock/bazaar")).getAsJsonObject().getAsJsonObject("products");
        for (final Map.Entry<String, JsonElement> itemEntry : products.entrySet()) {
            if (itemEntry.getValue().getAsJsonObject().getAsJsonArray("sell_summary").size() > 0) {
                Main.bazaarItem.put(itemEntry.getKey(), itemEntry.getValue().getAsJsonObject().getAsJsonArray("sell_summary").get(0).getAsJsonObject().get("pricePerUnit").getAsInt());
            }
        }
    }
    
    public static Void updateNPC() throws IOException {
        final JsonArray items = Objects.requireNonNull(Utils.getJson("https://api.hypixel.net/resources/skyblock/items")).getAsJsonObject().getAsJsonArray("items");
        for (final JsonElement i : items) {
            final JsonObject item = i.getAsJsonObject();
            if (item.has("npc_sell_price")) {
                Main.npcItem.put(item.get("id").getAsString(), item.get("npc_sell_price").getAsInt());
            }
        }
        return null;
    }
}
