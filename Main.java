package me.paperclip.paperclipflipper;

import java.util.LinkedList;
import java.util.HashMap;
import me.paperclip.paperclipflipper.commands.subcommands.Token;
import me.paperclip.paperclipflipper.commands.subcommands.Help;
import me.paperclip.paperclipflipper.commands.subcommands.Toggle;
import me.paperclip.paperclipflipper.commands.subcommands.Subcommand;
import me.paperclip.paperclipflipper.websocket.Client;
import java.io.IOException;
import me.paperclip.paperclipflipper.utils.Utils;
import me.paperclip.paperclipflipper.utils.ApiHandler;
import me.paperclip.paperclipflipper.events.OnChatReceived;
import me.paperclip.paperclipflipper.events.OnTooltip;
import me.paperclip.paperclipflipper.events.OnTick;
import me.paperclip.paperclipflipper.events.OnWorldJoin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import java.util.List;
import java.util.Date;
import me.paperclip.paperclipflipper.objects.AverageItem;
import java.util.Map;
import me.paperclip.paperclipflipper.commands.DizzyCommand;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "paperclip", name = "paperclip", version = "v0.9.2.1")
public class Main
{
    public static Config config;
    public static Authenticator authenticator;
    public static boolean checkedForUpdate;
    public static DizzyCommand commandManager;
    public static Map<String, AverageItem> averageItemMap;
    public static Map<String, Date> processedItem;
    public static Map<String, Integer> lbinItem;
    public static Map<String, Integer> bazaarItem;
    public static Map<String, Integer> npcItem;
    public static List<String> chatFilters;
    public static double balance;
    public static boolean justPlayedASound;
    public static File jarFile;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        Main.jarFile = event.getSourceFile();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        final ProgressManager.ProgressBar progressBar = ProgressManager.push("paperclip", 4);
        Main.authenticator = new Authenticator(progressBar);
        try {
            Main.authenticator.authenticate(true);
        }
        catch (Exception e) {
            while (progressBar.getStep() < progressBar.getSteps() - 1) {
                progressBar.step("loading-failed-" + progressBar.getStep());
            }
            e.printStackTrace();
            Reference.logger.error("paperclip has been disabled due to an error while authenticating. Please check the logs for more information.");
            return;
        }
        progressBar.step("Registering events, commands, hooks & tasks");
        Main.config.preload();
        ClientCommandHandler.instance.func_71560_a((ICommand)Main.commandManager);
        MinecraftForge.EVENT_BUS.register((Object)new OnWorldJoin());
        MinecraftForge.EVENT_BUS.register((Object)new OnTick());
        MinecraftForge.EVENT_BUS.register((Object)new OnTooltip());
        MinecraftForge.EVENT_BUS.register((Object)new OnChatReceived());
        Tasks.updateBalance.start();
        Tasks.updateBazaarItem.start();
        Tasks.updateFilters.start();
        Utils.runInAThread(ApiHandler::updateNPC);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Reference.logger.info("Logging out...");
            try {
                Main.authenticator.logout();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            return;
        }));
        progressBar.step("Establishing WebSocket Connection");
        Client.connectWithToken();
        ProgressManager.pop(progressBar);
    }
    
    static {
        Main.config = new Config();
        Main.checkedForUpdate = false;
        Main.commandManager = new PaperclipCommand(new Subcommand[] { new Toggle(), new Help(), new Token() });
        Main.averageItemMap = new HashMap<String, AverageItem>();
        Main.processedItem = new HashMap<String, Date>();
        Main.lbinItem = new HashMap<String, Integer>();
        Main.bazaarItem = new HashMap<String, Integer>();
        Main.npcItem = new HashMap<String, Integer>();
        Main.chatFilters = new LinkedList<String>();
        Main.balance = 0.0;
        Main.justPlayedASound = false;
    }
}
