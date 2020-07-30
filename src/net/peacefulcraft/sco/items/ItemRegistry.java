package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.storage.tasks.SyncItemRegistryTask;

public class ItemRegistry {
  private boolean initialized;
    public boolean isInitialized() { return initialized; }
    public void setInitialized() { initialized = true; }

  private ArrayList<ItemIdentifier> localSkills;
    public ArrayList<ItemIdentifier> getLocalSkills() { return localSkills; }

  public ItemRegistry() {
    this.localSkills = new ArrayList<ItemIdentifier>();
  }

  /**
   * Adds an item to the list of local, known items.
   */
  public void registerItemIdentifier(ItemIdentifier identifier) throws RuntimeException {
    if (initialized) {
      throw new RuntimeException("Attempted to add a new item identifier to the item registry after it was already initialized. (" + identifier.getName() + " " + identifier.getTier() + ")");
    }

    localSkills.add(identifier);
  }

  /**
   * Initiates a **blocking** MySQL task that sychrnonizes the global
   * item registry with the database, fetching item database ids.
   */
  public void synchronizeRegistry() {
    (new SyncItemRegistryTask(this)).runTask(SwordCraftOnline.getPluginInstance());
  }
}