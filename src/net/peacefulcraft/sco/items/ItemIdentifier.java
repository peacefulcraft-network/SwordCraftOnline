package net.peacefulcraft.sco.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Class to expose the necessary properties for saving a given
 * to the inventory database.
 */
public interface ItemIdentifier {

  /**
   * @return The Minecraft Material for the item.
   */
  public abstract Material getMaterial();

  /**
   * @return The name of the item. Should be the name of the ItemIdentifier class with appropriate spacing.
   *         Spaces are .replaceAll()'d to match items in game with their ItemIdentifier class.
   */
  public abstract String getName();

  /**
   * @return The lore to be applied to the item.
   */
  public abstract ArrayList<String> getLore();

  /**
   * Indicates whether a player can drop this item.
   * @return True if this item can be dropped.
   *         False if this item can not be dropped.
   */
  public abstract boolean isDroppable();

  /**
   * Indicates whether the player cna move this item in their inventory.
   * @return True if the item can be moved.
   *         False if the item can not be moved.
   */
  public abstract boolean isMovable();

  /**
   * Static items are normal items in the player's inventory. These items will be saved to the database
   * on inventory saves.
   * Dynamic items are items that are representative of or derivative of something within the game. These items
   * should not be saved to the database. IE. Sword Skill Tome or Primary / Secondary Trigger items
   * @return True if the item is dynamic.
   *         False if it is static.
   */
  public abstract boolean isDynamic();

  /**
   * Apply the appropriate NBT flags for this item to the item stack.
   * @param item Item on which flags should be applied.
   * @return The resulting ItemStack.
   */
  public abstract NBTItem applyNBT(NBTItem item);

  /**
   * Check if an item with the give name exists.
   * @param name Name of the item.
   * @return True if item exist.
   *         False if item doesn't exist.
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
   * Generates the requested item as an ItemStack
   * Static items are normal items in the player's inventory. These items will be saved to the database
   * on inventory saves.
   * Dynamic items are items that are representative of or derivative of something within the game. Thes
   * @param name The name/class of the item.
   * @param amount ItemStack quantity.
   * @param dynamic True for a dynamic item, false for a static item (see above).
   */
  public static ItemStack generateItem(String name, int amount, boolean dynamic) throws RuntimeException {
    try {
      name = name.replaceAll(" ", "");
      Class<?> classs = Class.forName("net.peacefulcraft.sco.items." + name);
      Constructor<?> constructor = classs.getConstructor();
      
      ItemIdentifier itemIdentifier = ((ItemIdentifier) constructor.newInstance());
 
      ItemStack item = new ItemStack(itemIdentifier.getMaterial(), amount);
 
      NBTItem nbti = new NBTItem(item);
      nbti = itemIdentifier.applyNBT(nbti);
      nbti.setBoolean("dynamic", dynamic);

      return nbti.getItem();
			
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

    throw new RuntimeException();
  }
}