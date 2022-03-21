package me.paperclip.paperclipflipper;

import java.util.Collection;
import java.util.Arrays;
import gg.essential.universal.UDesktop;
import java.net.URI;
import gg.essential.vigilance.data.SortingBehavior;
import gg.essential.vigilance.data.PropertyCollector;
import gg.essential.vigilance.data.JVMAnnotationPropertyCollector;
import gg.essential.vigilance.data.PropertyType;
import gg.essential.vigilance.data.Property;
import java.util.ArrayList;
import java.io.File;
import gg.essential.vigilance.Vigilant;

public class Config extends Vigilant
{
    public static final File CONFIG_FILE;
    public static ArrayList<String> categoryFilter;
    @Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Basic", name = "Enabled", description = "Whether the mod should check for and send flips")
    public static boolean enabled;
    @Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Basic", name = "Enabled only in skyblock", description = "Whether the mod should disable sending flips when not in SkyBlock")
    public static boolean onlySkyblock;
    @Property(type = PropertyType.NUMBER, category = "Flipping", subcategory = "Basic", name = "Minimum Profit", description = "The minimum amount of profit that is required for the mod to send you the flip", max = Integer.MAX_VALUE, increment = 10000)
    public static int minProfit;
    @Property(type = PropertyType.NUMBER, category = "Flipping", subcategory = "Basic", name = "Minimum Demand", description = "The minimum sales per day that is required for the mod to send you the flip", max = Integer.MAX_VALUE, increment = 5)
    public static int minDemand;
    @Property(type = PropertyType.PERCENT_SLIDER, category = "Flipping", subcategory = "Basic", name = "Minimum Profit Percentage", description = "The minimum percentage of profit that is required for the mod to send you the flip")
    public static float minProfitPercentage;
    @Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Basic", name = "Alert Sounds", description = "Whether a sound should be played upon flip sent")
    public static boolean alertSounds;
    @Property(type = PropertyType.SWITCH, category = "Flipping", subcategory = "Advanced", name = "Manipulation Check", description = "Whether the mod should check if the item was manipulated before sending the flip, DISABLE THIS AT YOUR OWN RISK AS YOU CAN LOSE YOUR MONEY TO MARKET MANIPULATORS")
    public static boolean manipulationCheck;
    @Property(type = PropertyType.TEXT, category = "Confidential", name = "API Key", protectedText = true, description = "Run /api new to set it automatically, or paste one if you do not want to renew it")
    public static String apiKey;
    @Property(type = PropertyType.SWITCH, category = "Confidential", name = "Debug", description = "Whether to show debug information, such as latency")
    public static boolean debug;
    @Property(type = PropertyType.SWITCH, category = "Money Saving", name = "Best Selling Method", description = "Shows the best way to sell an item on its lore, the one that sells for the most will be shown")
    public static boolean bestSellingMethod;
    @Property(type = PropertyType.SWITCH, category = "Money Saving", name = "Best Selling Method Item Overlay", description = "Shows a green overlay on the item if the current open menu is the best selling method for the item")
    public static boolean bestSellingOverlay;
    @Property(type = PropertyType.SWITCH, category = "QOL", name = "Hide spam messages", description = "Hide messages that contain predefined keywords of scam advertisements and etc")
    public static boolean hideSpam;
    
    public Config() {
        super(Config.CONFIG_FILE, "Fortnite Configuration", (PropertyCollector)new JVMAnnotationPropertyCollector(), (SortingBehavior)new CustomSorting());
        this.initialize();
    }
    
    @Property(type = PropertyType.BUTTON, category = "Links", name = "GitHub", description = "Help with the development!")
    public static void github() {
        UDesktop.browse(URI.create("https://github.com/vmxl/Paperclip-Flips"));
    }
    
    @Property(type = PropertyType.BUTTON, category = "Links", name = "Discord", description = "Join our Discord server!")
    public static void discord() {
        UDesktop.browse(URI.create("https://discord.gg/22XQdYPzV9"));
    }
    
    static {
        CONFIG_FILE = new File("config/paperclipflipper.toml");
        Config.categoryFilter = new ArrayList<String>(Arrays.asList("TRAVEL_SCROLL", "COSMETIC", "DUNGEON_PASS", "ARROW_POISON", "PET_ITEM"));
        Config.enabled = false;
        Config.onlySkyblock = true;
        Config.minProfit = 50000;
        Config.minDemand = 10;
        Config.minProfitPercentage = 0.0f;
        Config.alertSounds = true;
        Config.manipulationCheck = true;
        Config.apiKey = "";
        Config.debug = false;
        Config.bestSellingMethod = true;
        Config.bestSellingOverlay = true;
        Config.hideSpam = true;
    }
}//
