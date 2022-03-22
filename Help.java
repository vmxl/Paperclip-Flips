// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.commands.subcommands;

import me.paperclip.paperclipflipper.Main;
import net.minecraft.command.ICommandSender;

public class Help implements Subcommand
{
    @Override
    public String getCommandName() {
        return "help";
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
        return "Sends the help message";
    }
    
    @Override
    public boolean processCommand(final ICommandSender sender, final String[] args) {
        Main.commandManager.sendHelp(sender);
        return true;
    }
}
