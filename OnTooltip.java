// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.events;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Map;
import gg.essential.universal.UKeyboard;
import me.paperclip.paperclipflipper.objects.BestSellingMethod;
import net.minecraft.util.EnumChatFormatting;
import me.paperclip.paperclipflipper.Config;
import me.paperclip.paperclipflipper.utils.Utils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class OnTooltip
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(final ItemTooltipEvent event) {
        if (!Utils.isOnSkyblock()) {
            return;
        }
        final String id = Utils.getIDFromItemStack(event.itemStack);
        if (Config.debug && id != null) {
            event.toolTip.add(EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD.toString() + "Item ID: " + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD + id);
        }
        if (Config.bestSellingMethod) {
            final Map.Entry<BestSellingMethod, Long> result = Utils.getBestSellingMethod(id);
            if (result.getKey() == BestSellingMethod.NONE) {
                return;
            }
            final boolean shifted = UKeyboard.isShiftKeyDown();
            if (event.itemStack.field_77994_a > 1 && !shifted && !event.toolTip.contains(EnumChatFormatting.DARK_GRAY + "[SHIFT show x" + event.itemStack.field_77994_a + "]")) {
                event.toolTip.add(EnumChatFormatting.DARK_GRAY + "[SHIFT show x" + event.itemStack.field_77994_a + "]");
            }
            event.toolTip.add(EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD.toString() + "Best Selling Method: " + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD + result.getKey().toString() + " ($" + Utils.formatValue(shifted ? (result.getValue() * event.itemStack.field_77994_a) : ((long)result.getValue())) + ")");
        }
    }
}
