// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.commands.subcommands;

import me.paperclip.paperclipflipper.Main;
import net.minecraft.command.ICommandSender;
import me.paperclip.paperclipflipper.utils.Utils;
import me.paperclip.paperclipflipper.Config;

public class Toggle implements Subcommand
{
    public static void updateConfig() {
        if (Config.enabled) {
            Utils.sendMessageWithPrefix("&aFlipper enabled.");
        }
        else {
            Utils.sendMessageWithPrefix("&cFlipper disabled.");
        }
    }
    
    @Override
    public String getCommandName() {
        return "toggle";
    }
    
    @Override
    public boolean isHidden() {
        return false;
    }
    
    @Override
    public String getCommandUsage() {
        return "";
    }
    
    @Override
    public String getCommandDescription() {
        return "Toggles the flipper on or off";
    }
    
    @Override
    public boolean processCommand(final ICommandSender sender, final String[] args) {
        Config.enabled = !Config.enabled;
        Main.config.writeData();
        updateConfig();
        return true;
    }
}
