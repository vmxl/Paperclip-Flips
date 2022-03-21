
package me.paperclip.paperclipflipper;

import me.paperclip.paperclipflipper.utils.ApiHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.google.gson.JsonElement;
import java.util.Map;
import me.paperclip.paperclipflipper.utils.Utils;
import java.util.LinkedList;

public class Tasks
{
    public static Thread updateBalance;
    public static Thread updateBazaarItem;
    public static Thread updateFilters;
    
    static {
        Tasks.updateBalance = new Thread(() -> {
            while (true) {
                if (Config.enabled) {
                    try {
                        ApiHandler.updatePurse();
                        Thread.sleep(15000L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e2) {
                        try {
                            Utils.sendMessageWithPrefix("&cFailed to update balance, please check if you set your API key correctly.");
                            Thread.sleep(60000L);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        e2.printStackTrace();
                    }
                }
                else {
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }, "Not Enough Coins Balance Updating Task");
        Tasks.updateBazaarItem = new Thread(() -> {
            while (true) {
                if (!Config.enabled) {
                    if (!Config.bestSellingMethod) {
                        try {
                            Thread.sleep(100L);
                        }
                        catch (InterruptedException e4) {
                            e4.printStackTrace();
                        }
                        continue;
                    }
                }
                try {
                    ApiHandler.updateBazaar();
                    Thread.sleep(2500L);
                }
                catch (Exception e5) {
                    e5.printStackTrace();
                    try {
                        Thread.sleep(60000L);
                    }
                    catch (InterruptedException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
        }, "Not Enough Coins Bazaar Updating Task");
        LinkedList<String> filters;
        final Iterator<Map.Entry<String, JsonElement>> iterator;
        Map.Entry<String, JsonElement> f;
        final Iterator<JsonElement> iterator2;
        JsonElement filter;
        Tasks.updateFilters = new Thread(() -> {
            while (true) {
                if (Config.hideSpam) {
                    filters = new LinkedList<String>();
                    try {
                        Utils.getJson("https://nec.robothanzo.dev/filter").getAsJsonObject().getAsJsonObject("result").entrySet().iterator();
                        while (iterator.hasNext()) {
                            f = iterator.next();
                            f.getValue().getAsJsonArray().iterator();
                            while (iterator2.hasNext()) {
                                filter = iterator2.next();
                                filters.add(filter.getAsString().toLowerCase(Locale.ROOT));
                            }
                        }
                        Main.chatFilters = filters;
                        Thread.sleep(60000L);
                    }
                    catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                else {
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException e7) {
                        e7.printStackTrace();
                    }
                }
            }
        }, "Not Enough Coins Filters Updating Task");
    }
}
