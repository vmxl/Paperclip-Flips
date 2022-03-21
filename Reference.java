

package me.paperclip.paperclip.flipper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference
{
    public static final String MOD_ID = "paperclip";
    public static final String NAME = "paperclip";
    public static final String VERSION = "v0.9.2.1";
    public static final Logger logger;
    
    static {
        logger = LogManager.getLogger("paperclip");
    }
}
