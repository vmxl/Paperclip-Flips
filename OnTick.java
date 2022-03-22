// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.paperclip.paperclipflipper.utils.Utils;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OnTick
{
    private static int ticks;
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (OnTick.ticks % 20 == 0) {
            Utils.updateSkyblockScoreboard();
        }
        ++OnTick.ticks;
    }
    
    static {
        OnTick.ticks = 0;
    }
}
