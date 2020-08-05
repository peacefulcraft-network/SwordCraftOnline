package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

public class GoldCoinItem implements ItemIdentifier {

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
  public ItemTier[] getAllowedTiers() {
    return new ItemTier[] { ItemTier.COMMON };
  }

  @Override
  public ItemTier getTier() {
    return ItemTier.COMMON;
  }
}