// 
// Decompiled by Procyon v0.5.36
// 

package me.dizzy.dizzyflipper.commands.subcommands;

import me.paperclip.paperclipflipper.utils.Utils;
import me.paperclip.paperclipflipper.Main;
import net.minecraft.event.ClickEvent;
import me.paperclip.paperclipflipper.Config;
import net.minecraft.command.ICommandSender;

public class Token implements Subcommand
{
    @Override
    public String getCommandName() {
        return "token";
    }
    
    @Override
    public boolean isHidden() {
        return true;
    }
    
    @Override
    public String getCommandUsage() {
        return "";
    }
    
    @Override
    public String getCommandDescription() {
        return "Shows the current session token for API debugging purposes, do NOT share this with anyone!";
    }
    
    @Override
    public boolean processCommand(final ICommandSender sender, final String[] args) {
        if (Config.debug) {
            Utils.sendMessageWithPrefix("&7Click to copy the token", new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Main.authenticator.getToken()));
        }
        return true;
    }
}
