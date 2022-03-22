// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.events;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import me.dizzy.dizzyflipper.Reference;
import java.util.Locale;
import me.paperclip.paperclipflipper.Main;
import me.paperclip.paperclipflipper.utils.Utils;
import me.paperclip.paperclipflipper.Config;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class OnChatReceived
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chat(final ClientChatReceivedEvent event) {
        final String message = event.message.func_150260_c();
        if (message.startsWith("Your new API key is ")) {
            final String key = Config.apiKey = message.split("key is ")[1];
            Utils.sendMessageWithPrefix("§aAPI Key set to " + key);
        }
        for (final String filter : Main.chatFilters) {
            if (message.toLowerCase(Locale.ROOT).contains(filter) && message.contains(": ")) {
                event.setCanceled(true);
                Reference.logger.info("The following message was ignored due to a chat filter: " + message);
            }
        }
    }
}
