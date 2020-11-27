package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.utilityitems.CraftingBlockedSlot;

public class CraftingInventory extends InventoryBase {

    public CraftingInventory(SCOPlayer s) {
        super(s.getPlayer(), InventoryType.CRAFTING);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Crafting Table");
        setBlockers();
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

    private void setBlockers() {
        for(int row = 0; row <= 5; row++) {
            for(int col = 0; col <= 8; col++) {
                if(row == 0 || row >= 4 || ((row < 4 && row > 0) && (col == 0 || col == 8 || col == 4))) {
                    this.addButton(row, col, (new CraftingBlockedSlot()).create(1, false, false));
                }
            }
        }
    }
    
}
