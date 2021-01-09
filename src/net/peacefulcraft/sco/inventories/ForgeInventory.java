package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class ForgeInventory extends BukkitInventoryBase {

    protected SCOPlayer s;

    public ForgeInventory(SCOPlayer s) {
        this.s = s;
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Forge Anvil");
        setBlockers();
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        return false;
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
        

    }

    @Override
    public void onClickThatInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onThisInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onThatInventoryDrag(InventoryDragEvent ev, HashMap<Integer, ItemIdentifier> items) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent ev) {
        // TODO Auto-generated method stub

    }

        /**
     * Initializes the blocked slots
     */
    private void setBlockers() {
        for (int row = 0; row <= 5; row++) {
            for (int col = 0; col <= 8; col++) {
                // Broke statements into different lines b\c I got annoyed.
                // Leaves 1 catalyst slot in row 1.
                // 3 component slots in row 3 and 4
                if (row == 0 || row == 5) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 1 && (col <= 1 || col == 3 || col == 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 2 && (col <= 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 3 && (col == 0 || col == 4 || col == 8)) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                } else if (row == 5 || (row == 4 && (col == 0 || col >= 4))) {
                    setItem(row * 9 + col, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
                }
            }
        }
        // Places start button and indicator buttons
        setItem(14, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
        setItem(16, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
        setItem(32, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));
        setItem(34, ItemIdentifier.generateIdentifier("BlackSlot", ItemTier.COMMON, 1));

        setItem(15, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(23, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(25, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
        setItem(33, ItemIdentifier.generateIdentifier("RedSlot", ItemTier.COMMON, 1));
    }

    
}
