package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class GoldCoinItem implements ItemIdentifier {

  private static long databaseId;
    @Override
    public long getDatabaseID() { return databaseId; }
    @Override
    public void setDatabaseID(long databaseId) { GoldCoinItem.databaseId = databaseId; }

  @Override
  public Material getMaterial() {
    return Material.GOLD_NUGGET;
  }

  @Override
  public String getName() {
    return "Gold Coin";
  }

  @Override
  public ArrayList<String> getLore() {
    ArrayList<String> lore = new ArrayList<String>();
    lore.add("Just a shiny coin.");
    return lore;
  }

  @Override
  public boolean isDroppable() {
    return false;
  }

  @Override
  public boolean isMovable() {
    return true;
  }

  @Override
  public NBTItem applyNBT(NBTItem item) {
    return item;
  }

  @Override
  public ItemTier[] getAllowedTiers() {
    return new ItemTier[] { ItemTier.COMMON };
  }

  @Override
  public ItemTier getTier() {
    return ItemTier.COMMON;
  }
}