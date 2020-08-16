package net.peacefulcraft.sco.inventories;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;

public class PersistentPlayerInventory extends InventoryBase {

  private SCOPlayer s;
    public SCOPlayer getSCOPlayer() { return s; }

  public PersistentPlayerInventory(SCOPlayer s, ItemIdentifier[] items) {
    this.s = s;
    this.inventory = s.getPlayer().getInventory();
    this.setInventoryContents(items);
    s.getPlayer().updateInventory();
  }

  @Override
  public InventoryType getType() {
    return InventoryType.PLAYER;
  }

  @Override
  public void initializeDefaultLoadout() { /* Nothing to do here */ }

  @Override
  public void resizeInventory(int newSize) { /* Nothing to do here */ }
  
}