package net.peacefulcraft.sco.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Class to expose the necessary properties for saving a given to the inventory
 * database.
 */
public interface ItemIdentifier {

  /**
   * @return The name of the item. Should be the name of the ItemIdentifier class
   *         with appropriate spacing. Spaces are .replaceAll()'d to match items
   *         in game with their ItemIdentifier class.
   */
  public abstract String getName();

  /**
   * @return The display name of item in inventory. Can be different from name
   *         of file in package.
   */
  public abstract String getDisplayName();


  /**
   * @return The lore to be applied to the item.
   */
  public abstract ArrayList<String> getLore();

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
   * @return The number of items this identifier represents
   */
  public abstract Integer getQuantity();
  
  /**
   * @param quantity number of items this identifier represents
   */
  public abstract void setQuantity(Integer quantity);

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
      try {
        Class.forName("net.peacefulcraft.sco.items." + name +"Item");
        return true;
      } catch(ClassNotFoundException ex1) {
        return false;
      }
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
      Class<?> params[] = new Class[] { ItemTier.class, Integer.class };
      Constructor<?> constructor = clas.getConstructor(params);
    
      ItemIdentifier identiifer = ((ItemIdentifier) constructor.newInstance(tier, quantity));

      // Check that the requested item tier is allowed
      for (ItemTier allowedTier : identiifer.getAllowedTiers()) {
        if (tier == allowedTier) {
          return identiifer;
        }
      }

		} catch (ClassNotFoundException e) {
      if(!name.contains("utilityitems.") && (!name.isEmpty() || !name.equals(" "))) { 
        return generateIdentifier("utilityitems." + name, tier, quantity); 
      } 
      SwordCraftOnline.logSevere("Attempted to create item " + name.replace("utilityitems.", "") + ", but no coresponding class was found in net.peacefulcraft.sco.items");
		} catch (NoSuchMethodException e) {
			SwordCraftOnline.logSevere("net.peacefulcraft.sco.items." + name + " must have a constuctor with arguments (ItemTier, int)");
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

  public static ItemStack generateItem(ItemIdentifier item) throws RuntimeException {
    if (item instanceof CustomDataHolder) {
      return ItemIdentifier.generateItem(item.getName(), item.getTier(), item.getQuantity(), ((CustomDataHolder) item).getCustomData());
    } else {
      return ItemIdentifier.generateItem(item.getName(), item.getTier(), item.getQuantity());
    }
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
    ItemIdentifier itemIdentifier = ItemIdentifier.generateIdentifier(name, tier, amount);
    ItemStack item = new ItemStack(itemIdentifier.getMaterial(), amount);

    // Bad things happen if you try to give NBTI air
    if (itemIdentifier.getMaterial() == Material.AIR) {
      return item;
    }

    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(itemIdentifier.getDisplayName());
    itemMeta.setLore(itemIdentifier.getLore());
    item.setItemMeta(itemMeta);

    if (data != null && itemIdentifier instanceof CustomDataHolder) {
      item = ((CustomDataHolder) itemIdentifier).applyCustomItemData(item, data);
    }

    NBTItem nbti = new NBTItem(item);
    nbti.setBoolean("movable", itemIdentifier.isMovable());
    nbti.setBoolean("droppable", itemIdentifier.isDroppable());
    nbti.setString("identifier", name.replaceAll(" ", ""));
    nbti.setString("tier", tier.toString());

    if (itemIdentifier instanceof EphemeralAttributeHolder) {
      ((EphemeralAttributeHolder) itemIdentifier).applyEphemeralAttributes(item);
    }

    return nbti.getItem();
  }

  /**
   * Translate an ItemStack to it's item identifier with custom data parsed out of the item.
   * @param item The ItemStack to pasre
   * @return The coresponding ItemIdentifier with NBT values read from the ItemStack. Returns AirItem if unresolvable.
   */
  public static ItemIdentifier resolveItemIdentifier(ItemStack item) {
    if (item != null && item.getType() != Material.AIR) {
      NBTItem nbti = new NBTItem(item);
      if (nbti.hasKey("identifier")){
        if (ItemIdentifier.itemExists(nbti.getString("identifier"))) {
          String name = nbti.getString("identifier");
          ItemTier tier = ItemTier.COMMON;
          if (nbti.hasKey("tier")) {
            try {
              tier = ItemTier.valueOf(nbti.getString("tier").toUpperCase());
            } catch(IllegalArgumentException ex) {
              SwordCraftOnline.logWarning("Item " + name + " had invalid ItemTier " + nbti.getString("tier") + " falling back to COMMON");
            }
          } else {
            SwordCraftOnline.logWarning("Item " + name + " has no ItemTier encoded. Assuming COMMON");
          }

          ItemIdentifier identifier = ItemIdentifier.generateIdentifier(name, tier, item.getAmount());
          if (identifier instanceof CustomDataHolder) {
            ((CustomDataHolder) identifier).parseCustomItemData(item);
          }

          if (identifier instanceof EphemeralAttributeHolder) {
            ((EphemeralAttributeHolder) identifier).parseEphemeralAttributes(item);
          }

          return identifier;

        } else {
          SwordCraftOnline.logWarning("Found unregonizable item with identifier " + nbti.getString("identifier"));
        }
      } else {
        SwordCraftOnline.logWarning("Found unrecognizable item of material " + item.getType() + " and display name " + item.getItemMeta().getDisplayName());
      }
    }

    return new AirItem(ItemTier.COMMON, 0);
  }
}