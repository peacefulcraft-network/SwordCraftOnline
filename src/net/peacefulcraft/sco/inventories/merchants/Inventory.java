package net.peacefulcraft.sco.inventories.merchants;

import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryBase;
import net.peacefulcraft.sco.inventories.InventoryType;

public class Inventory extends InventoryBase {

    public Inventory(SCOPlayer s, String name, int size) {
        super(s.getPlayer(), InventoryType.MERCHANT);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, size, name);
    }

    @Override
    public void initializeDefaultLoadout() {
        return;
    }

    @Override
    public void resizeInventory(int size) {
        return;
    }

    @Override
    public void saveInventory() {
        return;
    }

    @Override
    public void openInventory() {
        super.openInventory();
    }

    @Override
    public void closeInventory() {
        super.closeInventory();
    }
    
}