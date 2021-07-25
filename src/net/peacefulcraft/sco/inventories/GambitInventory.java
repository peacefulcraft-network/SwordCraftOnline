package net.peacefulcraft.sco.inventories;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gambit.PlayerArenaManager;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.items.ShopItemIdentifier;
import net.peacefulcraft.sco.swordskills.SwordSkillDataManager;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class GambitInventory extends BukkitInventoryBase {

    protected SCOPlayer s;

    private GambitInventoryType state;
    private int page;

    public GambitInventory(SCOPlayer s) {
        this.s = s;
        state = GambitInventoryType.BASE;
        page = 0;

        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Gambit Store");

        setBase();
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return this.inventory == inventory;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        SwordCraftOnline.getInventoryListeners().onInventoryOpen(s.getPlayer().openInventory(this.inventory), this);
    }

    @Override
    public void closeInventory() {
        
    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        if (!ev.getView().getTitle().equalsIgnoreCase("Gambit Store")) { return; }

        // Clicked skill icon
        if (clickedItem instanceof SwordSkillProvider) {
            NBTItem nbti = new NBTItem(ev.getCurrentItem());
            if (nbti.hasKey("isInfoProvider") && nbti.getBoolean("isInfoProvider")) {
                this.inventory.clear();
                state = GambitInventoryType.INDIVIDUAL;
                page = 0;

                String internalName = clickedItem.getName();
                ItemTier[] tiers = clickedItem.getAllowedTiers();
                int i = 1;
                for (ItemTier tier : tiers) {
                    int price = ShopItemIdentifier.getPrice(tier);
                    this.inventory.setItem(18 + i, ShopItemIdentifier.generateShopItem(internalName, tier, 1, price));
                }
            } else if (nbti.hasKey("isShop") && nbti.getBoolean("isShop")) {
                int price = nbti.getInteger("price");

                if (s.getAlphaExperience() < price) {
                    Announcer.messagePlayer(
                        s, 
                        PlayerArenaManager.getPrefix(), 
                        "You do not have enough experience for that purchase.", 
                        0);
                } else {
                    s.setAlphaExperience(s.getAlphaExperience() - price);
                    Announcer.messagePlayer(
                        s, 
                        PlayerArenaManager.getPrefix(), 
                        "Purchased: " + ItemTier.getTierColor(clickedItem.getTier()) + "" + clickedItem.getDisplayName(), 
                        0);

                    ItemIdentifier ident = ItemIdentifier.generateIdentifier(
                        clickedItem.getName(), 
                        clickedItem.getTier(), 
                        1);
                    s.getPlayerInventory().addItem(ident);
                }
            }
        }

        // Paging options
        if (clickedItem.getName().equalsIgnoreCase("GreenSlot")) {
            HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.valueOf(state.toString().toUpperCase()));
            if (prep == null) { ev.setCancelled(true); }

            setInventory(prep);
        } else if (clickedItem.getName().equalsIgnoreCase("RedSlot")) {
            if (page == 0) {
                setBase();
            } else {
                page--;
                HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.valueOf(state.toString().toUpperCase()));
                if (prep == null) { ev.setCancelled(true); }

                setInventory(prep);
            }
        } else if (clickedItem.getName().equalsIgnoreCase("PurpleSlot")) {
            setBase();
        }

        // Menu type options
        if (clickedItem.getName().equalsIgnoreCase("GambitSwordSelect")) {
            this.inventory.clear();
            state = GambitInventoryType.SWORD;
            page = 0;

            HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.SWORD);
            setInventory(prep);
        } else if (clickedItem.getName().equalsIgnoreCase("GambitPrimarySelect")) {
            this.inventory.clear();
            state = GambitInventoryType.PRIMARY;
            page = 0;

            HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.PRIMARY);
            setInventory(prep);
        } else if (clickedItem.getName().equalsIgnoreCase("GambitSecondarySelect")) {
            this.inventory.clear();
            state = GambitInventoryType.SECONDARY;
            page = 0;

            HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.SECONDARY);
            setInventory(prep);
        } else if (clickedItem.getName().equalsIgnoreCase("GambitPassiveSelect")) {
            this.inventory.clear();
            state = GambitInventoryType.PASSIVE;
            page = 0;

            HashMap<Integer, ItemStack> prep = prepareInventory(SwordSkillType.PASSIVE);
            setInventory(prep);
        }
    }

    /**
     * Takes prepped map and converts it to inventory
     * @param prep map
     */
    private void setInventory(HashMap<Integer, ItemStack> prep) {
        for (Entry<Integer, ItemStack> entry : prep.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets to base inventory with 4 options
     */
    private void setBase() {
        this.inventory.clear();
        this.state = GambitInventoryType.BASE;

        this.inventory.setItem(12, ItemIdentifier.generateItem("GambitSwordSelect", ItemTier.COMMON, 1));
        this.inventory.setItem(13, ItemIdentifier.generateItem("GambitPrimarySelect", ItemTier.COMMON, 1));
        this.inventory.setItem(14, ItemIdentifier.generateItem("GambitSecondarySelect", ItemTier.COMMON, 1));
        this.inventory.setItem(15, ItemIdentifier.generateItem("GambitPassiveSelect", ItemTier.COMMON, 1));
    }

    /**
     * Prepares page appropriate inventory as hashmap
     * 
     * @param type
     * @return Hashmap for inventory conversion. Null if invalid procedure
     */
    private HashMap<Integer, ItemStack> prepareInventory(SwordSkillType type) {
        List<String> lis = SwordSkillDataManager.getByType(type);

        HashMap<Integer, ItemStack> out = new HashMap<>();

        boolean isFirst = true;
        row:
        for (int row = 0; row <= 4; row++) {
            for (int col = 0; col <= 8; col++) {
                try {
                    int index = row * 9 + col;
                    String name = lis.get(index + (page * 45));
                    isFirst = false;

                    JsonObject data = new JsonObject();
                    data.addProperty("isInfoProvider", true);
                    ItemIdentifier ident = ItemIdentifier.generateIdentifier(name, 1);

                    out.put(index, ItemIdentifier.generateItem(ident));
                } catch(IndexOutOfBoundsException ex) {
                    if (isFirst) { return null; }
                    break row;
                }
            }
        }
        out.put(45, ItemIdentifier.generateItem("RedSlot", ItemTier.COMMON, 1));
        out.put(53, ItemIdentifier.generateItem("GreenSlot", ItemTier.COMMON, 1));
        out.put(49, ItemIdentifier.generateItem("PurpleSlot", ItemTier.COMMON, 1));

        return out;
    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        
    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        
    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {

    }    

    /**
     * Determines state of gambit inventory
     */
    private enum GambitInventoryType {
        /** Starting position */
        BASE,
        /** Sword page */ 
        SWORD,
        /** Primary page */ 
        PRIMARY, 
        /** Secondary page */
        SECONDARY,
        /** Passive page */
        PASSIVE, 
        /** Individual item page */
        INDIVIDUAL;
    }
}
