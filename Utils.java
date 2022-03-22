// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.utils;

import com.google.common.collect.Sets;
import me.paperclip.paperclipflipper.Main;
import java.util.AbstractMap;
import me.paperclip.paperclipflipper.objects.BestSellingMethod;
import java.util.Map;
import me.paperclip.paperclipflipper.Reference;
import java.util.concurrent.Callable;
import net.minecraft.nbt.NBTTagCompound;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import java.util.Iterator;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatStyle;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.event.ClickEvent;
import gg.essential.universal.UChat;
import net.minecraft.util.EnumChatFormatting;
import java.text.NumberFormat;
import java.io.IOException;
import java.net.URLConnection;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import java.net.URL;
import com.google.gson.JsonElement;
import java.text.DecimalFormat;
import java.util.Set;

public class Utils
{
    private static final Set<String> SKYBLOCK_IN_ALL_LANGUAGES;
    private static boolean hasSkyblockScoreboard;
    
    private static String formatValue(final long amount, final long div, final char suffix) {
        return new DecimalFormat(".##").format(amount / (double)div) + suffix;
    }
    
    public static JsonElement getJson(final String jsonUrl) throws IOException {
        final URL url = new URL(jsonUrl);
        final URLConnection conn = url.openConnection();
        conn.setRequestProperty("Connection", "close");
        conn.setRequestProperty("User-Agent", "PaperclipFlipper/1.0");
        return new JsonParser().parse((Reader)new InputStreamReader(conn.getInputStream()));
    }
    
    public static String formatValue(final long amount) {
        if (amount >= 1000000000000000L) {
            return formatValue(amount, 1000000000000000L, 'q');
        }
        if (amount >= 1000000000000L) {
            return formatValue(amount, 1000000000000L, 't');
        }
        if (amount >= 1000000000L) {
            return formatValue(amount, 1000000000L, 'b');
        }
        if (amount >= 1000000L) {
            return formatValue(amount, 1000000L, 'm');
        }
        if (amount >= 100000L) {
            return formatValue(amount, 1000L, 'k');
        }
        return NumberFormat.getInstance().format(amount);
    }
    
    public static void sendMessageWithPrefix(final String message) {
        UChat.chat((Object)(EnumChatFormatting.GOLD + "[ paperclip ] " + message.replaceAll("&", "§")));
    }
    
    public static void sendMessageWithPrefix(final String message, final ClickEvent clickEvent) {
        final UTextComponent result = new UTextComponent(EnumChatFormatting.GOLD + "[ paperclip ] " + message.replaceAll("&", "§"));
        result.func_150255_a(new ChatStyle().func_150241_a(clickEvent));
        UChat.chat((Object)result);
    }
    
    public static int getTax(final int price) {
        float taxRate = 0.01f;
        if (price >= 1000000) {
            taxRate = 0.02f;
        }
        return (int)Math.floor(price * taxRate);
    }
    
    public static int getTaxedProfit(final int price, final int profit) {
        return profit - getTax(price);
    }
    
    public static String getProfitText(final int profit) {
        EnumChatFormatting color = EnumChatFormatting.GRAY;
        if (profit >= 100000) {
            color = EnumChatFormatting.GOLD;
        }
        if (profit >= 500000) {
            color = EnumChatFormatting.GREEN;
        }
        if (profit >= 1000000) {
            color = EnumChatFormatting.DARK_GREEN;
        }
        if (profit >= 10000000) {
            color = EnumChatFormatting.AQUA;
        }
        return color + "+$" + formatValue(profit);
    }
    
    public static boolean isOnSkyblock() {
        return hasSkyblockScoreboard();
    }
    
    public static boolean hasSkyblockScoreboard() {
        return Utils.hasSkyblockScoreboard;
    }
    
    public static void updateSkyblockScoreboard() {
        final Minecraft mc = Minecraft.func_71410_x();
        if (mc != null && mc.field_71441_e != null && mc.field_71439_g != null) {
            if (mc.func_71356_B() || mc.field_71439_g.func_142021_k() == null || !mc.field_71439_g.func_142021_k().toLowerCase().contains("hypixel")) {
                Utils.hasSkyblockScoreboard = false;
                return;
            }
            final Scoreboard scoreboard = mc.field_71441_e.func_96441_U();
            final ScoreObjective sidebarObjective = scoreboard.func_96539_a(1);
            if (sidebarObjective != null) {
                final String objectiveName = sidebarObjective.func_96678_d().replaceAll("(?i)\\u00A7.", "");
                for (final String skyblock : Utils.SKYBLOCK_IN_ALL_LANGUAGES) {
                    if (objectiveName.contains(skyblock)) {
                        Utils.hasSkyblockScoreboard = true;
                        return;
                    }
                }
            }
            Utils.hasSkyblockScoreboard = false;
        }
    }
    
    public static String getIDFromItemStack(final ItemStack stack) {
        if (stack == null) {
            return null;
        }
        final NBTTagCompound tag = stack.func_77978_p();
        String id = null;
        if (tag != null && tag.func_150297_b("ExtraAttributes", 10)) {
            final NBTTagCompound ea = tag.func_74775_l("ExtraAttributes");
            if (!ea.func_150297_b("id", 8)) {
                return null;
            }
            id = ea.func_74779_i("id");
            if ("PET".equals(id)) {
                final String petInfo = ea.func_74779_i("petInfo");
                if (petInfo.length() > 0) {
                    final JsonObject petInfoObject = (JsonObject)new GsonBuilder().setPrettyPrinting().create().fromJson(petInfo, (Class)JsonObject.class);
                    id = petInfoObject.get("type").getAsString() + petInfoObject.get("tier").getAsString();
                }
            }
            if ("ENCHANTED_BOOK".equals(id)) {
                final NBTTagCompound enchants = ea.func_74775_l("enchantments");
                final Iterator<String> iterator = enchants.func_150296_c().iterator();
                if (iterator.hasNext()) {
                    final String enchname = iterator.next();
                    id = enchname.toUpperCase() + ";" + enchants.func_74762_e(enchname);
                }
            }
        }
        return id;
    }
    
    public static void runInAThread(final Callable<Void> callable) {
        new Thread(() -> {
            try {
                callable.call();
            }
            catch (Exception e) {
                Reference.logger.error(e.getMessage(), (Throwable)e);
            }
        }).start();
    }
    
    public static String removeColorCodes(final String in) {
        return in.replaceAll("(?i)\\u00A7.", "");
    }
    
    public static Map.Entry<BestSellingMethod, Long> getBestSellingMethod(final String id) {
        if (id == null) {
            return new AbstractMap.SimpleEntry<BestSellingMethod, Long>(BestSellingMethod.NONE, 0L);
        }
        if (id.equals("POTION")) {
            return new AbstractMap.SimpleEntry<BestSellingMethod, Long>(BestSellingMethod.NONE, 0L);
        }
        BestSellingMethod method = BestSellingMethod.NONE;
        long bestPrice = 0L;
        if (Main.lbinItem.containsKey(id) && Main.lbinItem.get(id) - getTax(Main.lbinItem.get(id)) > bestPrice) {
            bestPrice = Main.lbinItem.get(id) - getTax(Main.lbinItem.get(id));
            method = BestSellingMethod.LBIN;
        }
        if (Main.bazaarItem.containsKey(id) && Main.bazaarItem.get(id) > bestPrice) {
            bestPrice = Main.bazaarItem.get(id);
            method = BestSellingMethod.BAZAAR;
        }
        if (Main.npcItem.containsKey(id) && Main.npcItem.get(id) > bestPrice) {
            bestPrice = Main.npcItem.get(id);
            method = BestSellingMethod.NPC;
        }
        return new AbstractMap.SimpleEntry<BestSellingMethod, Long>(method, bestPrice);
    }
    
    public static EnumChatFormatting getColorCodeFromRarity(final String rarity) {
        switch (rarity) {
            case "COMMON": {
                return EnumChatFormatting.WHITE;
            }
            case "UNCOMMON": {
                return EnumChatFormatting.GREEN;
            }
            case "RARE": {
                return EnumChatFormatting.BLUE;
            }
            case "EPIC": {
                return EnumChatFormatting.DARK_PURPLE;
            }
            case "LEGENDARY": {
                return EnumChatFormatting.GOLD;
            }
            case "MYTHIC": {
                return EnumChatFormatting.LIGHT_PURPLE;
            }
            case "DIVINE": {
                return EnumChatFormatting.AQUA;
            }
            case "SPECIAL":
            case "VERY_SPECIAL": {
                return EnumChatFormatting.RED;
            }
            default: {
                return EnumChatFormatting.WHITE;
            }
        }
    }
    
    static {
        SKYBLOCK_IN_ALL_LANGUAGES = Sets.newHashSet((Object[])new String[] { "SKYBLOCK", "\u7a7a\u5c9b\u751f\u5b58", "\u7a7a\u5cf6\u751f\u5b58" });
    }
}
