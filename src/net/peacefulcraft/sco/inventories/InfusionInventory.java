package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.utilityitems.BlackSlot;
import net.peacefulcraft.sco.items.utilityitems.BlueSlot;

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
                // Black locked slots
                if(row == 0 || row >= 4 || ((row < 4 && row > 0) && (col == 0 || col == 8 || col == 4))) {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                }
                // Blue locked slots
                if(((row == 1 || row == 3) && (col >= 5 && col <= 7)) || (row == 2 && (col == 5 || col == 7))) {
                    this.addButton(row, col, (new BlueSlot()).create(1, false, false));
                }
            }
        }
    }
}
