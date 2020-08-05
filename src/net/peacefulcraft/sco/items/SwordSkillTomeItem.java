package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class SwordSkillTomeItem implements ItemIdentifier {

  private int quantity;
    @Override
    public int getQuantity() {
      return quantity;
    }
    @Override
    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

  public SwordSkillTomeItem(ItemTier tier, int quantity) {
    // Tier is static so it is just there for static constructor invocation in ItemIdentifier
    this.quantity = quantity;
  }

  @Override
  public Material getMaterial() {
    return Material.BOOK;
  }

  @Override
  public String getName() {
    return "Sword Skill Tome";
  }

  @Override
  public ArrayList<String> getLore() {
    ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Right click to store your sword skill knowledge.");
		return lore;
  }

  @Override
  public boolean isDroppable() {
    return false;
  }

  @Override
  public boolean isMovable() {
    return false;
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