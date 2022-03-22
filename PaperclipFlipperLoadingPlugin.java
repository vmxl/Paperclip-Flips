// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.tweaker;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.Name("PaperclipFlipper FTW")
public class DizzyFlipperLoadingPlugin implements IFMLLoadingPlugin
{
    public String[] getASMTransformerClass() {
        return new String[] { DizzyFlipperTransformer.class.getName() };
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
