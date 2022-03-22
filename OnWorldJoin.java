// 
// Decompiled by Procyon v0.5.36
// 

package me.dizzy.dizzyflipper.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.dizzy.dizzyflipper.commands.subcommands.Toggle;
import java.util.TimerTask;
import me.dizzy.dizzyflipper.Config;
import java.util.Timer;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class OnWorldJoin
{
    boolean hasRan;
    
    public OnWorldJoin() {
        this.hasRan = false;
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        final Timer timer = new Timer();
        if (Config.enabled && !this.hasRan) {
            this.hasRan = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Toggle.updateConfig();
                }
            }, 2000L);
        }
    }
}
