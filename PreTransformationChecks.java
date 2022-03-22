// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.tweaker;

import net.minecraft.launchwrapper.Launch;

public class PreTransformationChecks
{
    public static boolean deobfuscated;
    public static boolean usingNotchMappings;
    
    static void runChecks() {
        PreTransformationChecks.deobfuscated = false;
        PreTransformationChecks.deobfuscated = Launch.blackboard.get("fml.deobfuscatedEnvironment");
        PreTransformationChecks.usingNotchMappings = !PreTransformationChecks.deobfuscated;
    }
}
