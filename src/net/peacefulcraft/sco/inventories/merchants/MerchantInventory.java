package net.peacefulcraft.sco.inventories.merchants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryBase;
import net.peacefulcraft.sco.inventories.merchants.MerchantLoader.MerchantInventoryType;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.mythicmobs.io.MythicConfig;

public class MerchantInventory {
    
    private String file;

    private String internalName;

    private MythicConfig config;

    /*Shop type */
    private MerchantInventoryType type;
        public MerchantInventoryType getType() { return type; }

    /*Names of mythic mobs that can open the inventory */
    private List<String> names;
        public List<String> getNames() { return Collections.unmodifiableList(names); }

    private List<String> itemsStr;

    /*Items in shop */
    private HashMap<Integer, List<ItemStack>> items;

    /*Name of shop displayed to player */
    private String shopName;

    private List<String> closingPhrase;
        public String getClosingPhrase() { return closingPhrase.get(SwordCraftOnline.r.nextInt(closingPhrase.size()-1)); }

    private List<String> denyPhrase;
        public String getDenyPhrase() { return denyPhrase.get(SwordCraftOnline.r.nextInt(denyPhrase.size()-1)); }

    public MerchantInventory(String file, String internalName, MythicConfig mc) {
        this.file = file;
        this.internalName = internalName;
        this.config = mc;

        String typeStr = config.getString("Type");
        this.type = MerchantInventoryType.valueOf(typeStr.toUpperCase());
        this.names = config.getStringList("Names");
        this.itemsStr = config.getStringList("Items");
        this.shopName = config.getString("ShopName");
        this.closingPhrase = config.getStringList("ClosingPhrase");
        this.denyPhrase = config.getStringList("DenyPhrase");

        if(itemsStr.size() == 0) {
            items = null;
        } else {
            /*Format: 0-Custom item name, 1-price, 2-floor */
            for(String s : itemsStr) {
                String[] split = s.split(" ");
                if(ItemIdentifier.itemExists(split[0])) {
                    Integer floor = Integer.valueOf(split[2]);
                    Integer price = Integer.valueOf(split[1]);
                    ItemStack item = ItemIdentifier.generate(split[0], 1, price);

                    if(!items.containsKey(floor)) {
                        items.put(floor, new ArrayList<ItemStack>());
                    }
                    items.get(floor).add(item);
                }
            }
        }
    }

    public void openInventory(SCOPlayer s) {
        List<ItemStack> lis = items.get(s.getFloor());
        int size = (int) Math.ceil(lis.size()/7);

        Inventory inv = new Inventory(s, this.shopName, size);

        int i = 0;
        int row = 2;
        int col = 2;
        while(i < lis.size()) {
            inv.addButton(row, col, lis.get(i));
            if(col == 8) {
                col = 2;
            } else {
                col += 1;
            }
            if(row >= inv.getSize()/3) {
                row = 2;
            } else {
                row += 1;
            }
            i++;
        }
        inv.openInventory();
    }

    public List<ItemStack> getItems(int floor) {
        return items.get(floor);
    }
}