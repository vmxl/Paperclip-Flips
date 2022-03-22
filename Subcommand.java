// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.commands.subcommands;

import net.minecraft.command.ICommandSender;

public interface Subcommand
{
    String getCommandName();
    
    boolean isHidden();
    
    String getCommandUsage();
    
    String getCommandDescription();
    
    boolean processCommand(final ICommandSender p0, final String[] p1);
}
