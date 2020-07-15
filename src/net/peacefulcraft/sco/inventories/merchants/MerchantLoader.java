package net.peacefulcraft.sco.inventories.merchants;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.io.IOHandler;
import net.peacefulcraft.sco.mythicmobs.io.IOLoader;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class MerchantLoader {

    private HashMap<MerchantInventoryType, MerchantInventory> inventoryMap;

    public MerchantLoader() {
        this.inventoryMap = new HashMap<>();

        loadInventories();
    }

    public void loadInventories() {
        this.inventoryMap.clear();
        IOLoader<SwordCraftOnline> defaultInvs = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(),
                "ExampleInv.yml", "Inventories");
        defaultInvs = new IOLoader<SwordCraftOnline>(SwordCraftOnline.getPluginInstance(), "ExampleInv.yml",
                "Inventories");
        List<File> invFiles = IOHandler.getAllFiles(defaultInvs.getFile().getParent());
        List<IOLoader<SwordCraftOnline>> invLoaders = IOHandler.getSaveLoad(SwordCraftOnline.getPluginInstance(),
                invFiles, "Inventories");

        for (IOLoader<SwordCraftOnline> sl : invLoaders) {
            for (String name : sl.getCustomConfig().getConfigurationSection("").getKeys(false)) {
                MythicConfig mc = new MythicConfig(name, sl.getFile(), sl.getCustomConfig());
                String file = sl.getFile().getPath();

                try {
                    MerchantInventory mi = new MerchantInventory(file, name, mc);
                    String type = sl.getCustomConfig().getString(name + ".Type");
                    this.inventoryMap.put(MerchantInventoryType.valueOf(type.toUpperCase()), mi);
                } catch (IllegalArgumentException ex) {
                    SwordCraftOnline.logInfo(
                            "[Merchant Loader] Attempted to load MerchantInventory with invalid type: " + name);
                    continue;
                }
            }
        }
        SwordCraftOnline.logInfo("[Merchant Loader] Loading complete!");
    }

    /** @return merchant inventory associated with mob display name */
    public MerchantInventory getMerchantInventory(String name) {
        for (MerchantInventory mi : inventoryMap.values()) {
            if (mi.getNames().contains(name)) {
                return mi;
            }
        }
        return null;
    }

    public static boolean isShop(String name) {
        name = name.toUpperCase().replace(" ", "_");
        for(ShopName s : ShopName.values()) {
            if(name.equalsIgnoreCase(s.toString())) {
                return true;
            }
        }
        return false;
    }

    public static String getTag(String name) {
        return ChatColor.BLUE + "[" + ChatColor.GOLD + "" + name + ChatColor.BLUE + "]";
    }

    public enum MerchantInventoryType {
        ALCHEMIST, BLACKSMITH, BLACK_MARKET, WANDERING_MERCHANT;
    }

    public enum ShopName {
        ALCHEMIST_SHOP, IRONWORKS, BLACK_MARKET, WANDERING_MERCHAT;
    }
}