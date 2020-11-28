package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.utilityitems.CraftingBlockedSlot;

public class InfusionInventory extends InventoryBase {

    public InfusionInventory(SCOPlayer s) {
        super(s.getPlayer(), InventoryType.INFUSION);
        this.inventory = SwordCraftOnline.getPluginInstance().getServer().createInventory(null, 54, "Infusion Table");
        setBlockers();
    }

    @Override
    public void initializeDefaultLoadout() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resizeInventory(int size) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveInventory() {
        // TODO Auto-generated method stub

    }
    
    /**
     * Initializes the blocked slots
     */
    private void setBlockers() {
        for(int row = 0; row <= 5; row++) {
            for(int col = 0; col <= 8; col++) {
                if(row == 0 || row >= 4 || col == 0 || (col >= 4 && (row != 3 && col != 6))) {
                    this.addButton(row, col, (new CraftingBlockedSlot()).create(1, false, false));
                }
            }
        }
    }
}
