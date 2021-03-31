package net.peacefulcraft.sco.inventories;

import java.util.HashMap;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.GoldCoinItem;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.ItemTier;

public class AlchemistInventory extends BukkitInventoryBase {

    public AlchemistInventory(SCOPlayer s) {
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 27, "Alchemist Shop");
        this.addItem(new GoldCoinItem(ItemTier.COMMON, 1));
    }

    @Override
    public boolean isInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void openInventory(SCOPlayer s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClickThisInventory(InventoryClickEvent ev, ItemIdentifier cursorItem, ItemIdentifier clickedItem) {
        // TODO Auto-generated method stub

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
    
}