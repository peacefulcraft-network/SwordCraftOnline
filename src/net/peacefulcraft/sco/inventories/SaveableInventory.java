package net.peacefulcraft.sco.inventories;

public interface SaveableInventory {
  /**
   * Returns the database id for this inventory
   * @return Invnetory database id
   */
  public abstract long getInventoryId();

  /**
   * Sets the inventories database id
   * @param inventoryId The id of this inventory in the database
   */
  public abstract void setInventoryId(long inventoryId);

  /**
   * Logic for saving this inventory
   */
  public abstract void saveInventory();
}