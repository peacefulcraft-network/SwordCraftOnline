package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;

public class SwordSkillTomeItem implements ItemIdentifier {

  private static long databaseId;
    @Override
    public long getDatabaseID() { return databaseId; }
    @Override
    public void setDatabaseID(long databaseId) { SwordSkillTomeItem.databaseId = databaseId; }

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