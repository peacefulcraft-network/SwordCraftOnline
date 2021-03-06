package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class TeleportCrystalItem implements ItemIdentifier {

  @Override
  public String getName() { return "Teleport Crystal"; }

  @Override
  public String getDisplayName() {
    return ItemTier.getTierColor(getTier()) + "Teleport Crystal";
  }

  @Override
  public ArrayList<String> getLore() {
    ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Right click to teleport to last visited city.");
    return lore;
  }

  @Override
  public Material getMaterial() { return Material.DIAMOND; }

  private Integer quantity;
    @Override
    public Integer getQuantity() { return quantity; }

    @Override
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

  @Override
  public ItemTier[] getAllowedTiers() { return new ItemTier[] { ItemTier.UNCOMMON }; }

  @Override
  public ItemTier getTier() { return ItemTier.UNCOMMON; }

  @Override
  public boolean isDroppable() { return false; }

  @Override
  public boolean isMovable() { return true; }

  public TeleportCrystalItem(ItemTier tier, Integer quantity) {
    // Tier is static so it is just there for static constructor invocation in ItemIdentifier
    this.quantity = quantity;
  }

}