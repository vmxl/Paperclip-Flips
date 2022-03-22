// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.commands;

import java.util.Collection;
import net.minecraft.util.BlockPos;
import me.paperclip.paperclipflipper.utils.Utils;
import java.util.Objects;
import net.minecraft.client.gui.GuiScreen;
import me.paperclip.paperclipflipper.Main;
import gg.essential.api.EssentialAPI;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.command.ICommandSender;
import me.paperclip.paperclipflipper.commands.subcommands.Subcommand;
import net.minecraft.command.CommandBase;

public class paperclipCommand extends CommandBase
{
    private final Subcommand[] subcommands;
    
    public paperclipCommand(final Subcommand[] subcommands) {
        this.subcommands = subcommands;
    }
    
    public boolean func_71519_b(final ICommandSender sender) {
        return true;
    }
    
    public List<String> func_71514_a() {
        return Arrays.asList("paperclipflipper", "paperclipflips");
    }
    
    public String func_71517_b() {
        return "paperclip";
    }
    
    public String func_71518_a(final ICommandSender sender) {
        return "/paperclip <subcommand> <arguments>";
    }
    
    public void sendHelp(final ICommandSender sender) {
        final List<String> commandUsages = new LinkedList<String>();
        for (final Subcommand subcommand : this.subcommands) {
            if (!subcommand.isHidden()) {
                commandUsages.add(EnumChatFormatting.AQUA + "/paperclip " + subcommand.getCommandName() + " " + subcommand.getCommandUsage() + EnumChatFormatting.DARK_AQUA + " - " + subcommand.getCommandDescription());
            }
        }
        sender.func_145747_a((IChatComponent)new ChatComponentText(EnumChatFormatting.GOLD + "paperclip " + EnumChatFormatting.GREEN + "v0.9.2.1" + "\n" + String.join("\n", commandUsages)));
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) {
        if (args.length == 0) {
            EssentialAPI.getGuiUtil().openScreen((GuiScreen)Main.config.gui());
            return;
        }
        for (final Subcommand subcommand : this.subcommands) {
            if (Objects.equals(args[0], subcommand.getCommandName())) {
                if (!subcommand.processCommand(sender, Arrays.copyOfRange(args, 1, args.length))) {
                    Utils.sendMessageWithPrefix("&cFailed to execute command, command usage: /paperclip " + subcommand.getCommandName() + " " + subcommand.getCommandUsage());
                }
                return;
            }
        }
        Utils.sendMessageWithPrefix(EnumChatFormatting.RED + "The subcommand wasn't found, please refer to the help message below for the list of subcommands");
        this.sendHelp(sender);
    }
    
    public List<String> func_180525_a(final ICommandSender sender, final String[] args, final BlockPos pos) {
        final List<String> possibilities = new LinkedList<String>();
        for (final Subcommand subcommand : this.subcommands) {
            possibilities.add(subcommand.getCommandName());
        }
        if (args.length == 1) {
            return (List<String>)func_175762_a(args, (Collection)possibilities);
        }
        return null;
    }
}
