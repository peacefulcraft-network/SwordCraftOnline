package net.peacefulcraft.sco.inventories;

import org.bukkit.event.Listener;
import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.customitems.GoldCoin;

public class AlchemistInventory extends InventoryBase implements Listener {

    public AlchemistInventory(SCOPlayer s) {
        super(s.getPlayer(), InventoryType.MERCHANT);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 27, "Alchemist Shop");
        this.addButton(2, 2, (new GoldCoin()).create(1, true));
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