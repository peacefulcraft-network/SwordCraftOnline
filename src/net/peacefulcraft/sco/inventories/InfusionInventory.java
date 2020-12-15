package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.utilityitems.BlackSlot;
import net.peacefulcraft.sco.items.utilityitems.BlueSlot;
import net.peacefulcraft.sco.items.utilityitems.InfusionStartSlot;
import net.peacefulcraft.sco.items.utilityitems.RedSlot;

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
                // Broke statements into different lines b\c I got annoyed.
                // Leaves 1 catalyst slot in row 1.
                // 3 component slots in row 3
                if(row == 0 || row == 5) {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                } else if(row == 1 && (col <= 1 || col == 3 || col == 4 || col == 8)) {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                } else if(row == 2 && (col <= 4 || col == 8)) {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                } else if(row == 3 && (col == 0 || col == 4 || col == 8)) {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                } else if(row == 5 || (row == 4 && (col == 0 || col >= 4)))  {
                    this.addButton(row, col, (new BlackSlot()).create(1, false, false));
                }
            }
        }
        // Places start button and indicator buttons
        this.addButton(2, 4, (new InfusionStartSlot()).create(1, false, false));
        this.addButton(1, 4, (new RedSlot()).create(1, false, false));
        this.addButton(3, 4, (new RedSlot()).create(1, false, false));
    }
}
