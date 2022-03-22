// 
// Decompiled by Procyon v0.5.36
// 

package me.paperclip.paperclipflipper.objects;

public enum BestSellingMethod
{
    NPC("NPC"), 
    BAZAAR("Bazaar"), 
    LBIN("Lowest-BIN"), 
    NONE("None");
    
    private final String string;
    
    private BestSellingMethod(final String string) {
        this.string = string;
    }
    
    @Override
    public String toString() {
        return this.string;
    }
}
