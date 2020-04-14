package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatColor;

public class TeleportCrystalItem implements ItemIdentifier {

  @Override
  public Material getMaterial() {
    return Material.DIAMOND;
  }

  @Override
  public String getName() {
    return "Teleport Crystal";
  }

  @Override
  public ArrayList<String> getLore() {
    ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_PURPLE + "Right click to teleport to last visited city.");
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
  public boolean isDynamic() {
    return false;
  }

  @Override
  public NBTItem applyNBT(NBTItem item) {
    return item;
  }
}