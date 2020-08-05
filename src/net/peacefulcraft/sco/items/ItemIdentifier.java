package net.peacefulcraft.sco.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Class to expose the necessary properties for saving a given to the inventory
 * database.
 */
public interface ItemIdentifier {

  /**
   * @return The Minecraft Material for the item.
   */
  public abstract Material getMaterial();

  /**
   * @return Possible tiers for the item
   */
  public abstract ItemTier[] getAllowedTiers();

  /**
   * @return Sword Skill ItemTier
   */
  public abstract ItemTier getTier();

  /**
   * @return The name of the item. Should be the name of the ItemIdentifier class
   *         with appropriate spacing. Spaces are .replaceAll()'d to match items
   *         in game with their ItemIdentifier class.
   */
  public abstract String getName();

  /**
   * @return The lore to be applied to the item.
   */
  public abstract ArrayList<String> getLore();

  /**
   * @return The number of items this identifier represents
   */
  public abstract int getQuantity();
  
  /**
   * @param quantity number of items this identifier represents
   */
  public abstract void setQuantity(int quantity);

  /**
   * Indicates whether a player can drop this item.
   * 
   * @return True if this item can be dropped. False if this item can not be
   *         dropped.
   */
  public abstract boolean isDroppable();

  /**
   * Indicates whether the player cna move this item in their inventory.
   * 
   * @return True if the item can be moved. False if the item can not be moved.
   */
  public abstract boolean isMovable();

  /**
   * Check if an item with the give name exists.
   * 
   * @param name Name of the item.
   * @return True if item exist. False if item doesn't exist.
   */
  public static boolean itemExists(String name) {
    try {
      Class.forName("net.peacefulcraft.sco.items." + name);
      return true;
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * Generates the requested ItemIdenfiier.
   * @param name of the identiifer
   * @param item tier to generate
   * @param quantity Number of items this identifier represents
   */
  public static ItemIdentifier generateIdentifier(String name, ItemTier tier, int quantity) {
    try {
      name = name.replaceAll(" ", "");
      Class<?> clas = Class.forName("net.peacefulcraft.sco.items." + name + "Item");
      Constructor<?> constructor = clas.getConstructor();
    
      ItemIdentifier identiifer = ((ItemIdentifier) constructor.newInstance(tier, quantity));

      // Check that the requested item tier is allowed
      for (ItemTier allowedTier : identiifer.getAllowedTiers()) {
        if (tier == allowedTier) {
          return identiifer;
        }
      }

		} catch (ClassNotFoundException e) {
			SwordCraftOnline.logSevere("Attempted to create item " + name + ", but no coresponding class was found in net.peacefulcraft.sco.items");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " must have a constuctor with arguments (int, ItemTier)");
		} catch (SecurityException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " does not have a public constructor.");
		} catch (InstantiationException | InvocationTargetException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " generated exception during reflective instantiation:");
		} catch (IllegalAccessException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " is an abstract class and cannot be instantiated.");
		} catch (IllegalArgumentException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " received an invalid arguement type during insantiation. Arguements must be of type (int, ItemTier).");
		} catch (ClassCastException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " must implement SwordSkillProvider.");
    }

    throw new RuntimeException("Failed to generate ItemIdentifier with requested scope " + name + " " + tier.toString());
  }

  /**
   * Generates the requested item as an ItemStack
   * @param name The name/class of the item.
   * @param tier The tier of the desired item
   * @param amount ItemStack quantity.
   */
  public static ItemStack generateItem(String name, ItemTier tier, int amount) throws RuntimeException {
    return ItemIdentifier.generateItem(name, tier, amount, null);
  }

    /**
   * Generates the requested item as an ItemStack
   * @param name The name/class of the item.
   * @param tier The tier of the desired item
   * @param amount ItemStack quantity.
   * @param data Json object with custom NBT flags
   */
  public static ItemStack generateItem(String name, ItemTier tier, int amount, JsonObject data) throws RuntimeException {
    ItemIdentifier itemIdentifier = ItemIdentifier.generateIdentifier(name, tier);
    ItemStack item = new ItemStack(itemIdentifier.getMaterial(), amount);

    if (data != null && itemIdentifier instanceof CustomDataHolder) {
      item = ((CustomDataHolder) itemIdentifier).applyCustomItemData(item, data);
    }

    NBTItem nbti = new NBTItem(item);
    nbti.setBoolean("movable", itemIdentifier.isMovable());
    nbti.setBoolean("droppable", itemIdentifier.isDroppable());
    nbti.setString("identifier", name.replaceAll(" ", ""));
    nbti.setString("tier", tier.toString());

    return item;
  }
}