package net.peacefulcraft.sco.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
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
    // I love 4 nested try catch statements.
    try {
      Class.forName("net.peacefulcraft.sco.items." + name);
      return true;
    } catch (ClassNotFoundException ex) {
      try {
        Class.forName("net.peacefulcraft.sco.items." + name +"Item");
        return true;
      } catch(ClassNotFoundException ex1) {
        try {
          Class.forName("net.peacefulcraft.sco.items.utilityitems." + name);
          return true;
        } catch(ClassNotFoundException ex2) {
          try {
            Class.forName("net.peacefulcraft.sco.items.utilityitems." + name + "Item");
            return true;
          } catch(ClassNotFoundException ex3) {
            return false;
          }
        }
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

    // Reseting to apply ephemeral
    item = nbti.getItem();
    if (itemIdentifier instanceof EphemeralAttributeHolder) {
      item = ((EphemeralAttributeHolder) itemIdentifier).applyEphemeralAttributes(item);
    }

    return item;
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

  /**
   * Check if two ItemIdentifiers are equal.
   * Non-deep check: Same Identifier class && tier
   * Deep: Non-deep && .getCustomData() values are String.equalsIgnoreCase()
   * @param item1 The first item to compare
   * @param item2 The second item to compare
   * @param deep True: Compare CustomDataHolder NBT values
   * @return 0 if items are equal.
   *         -1 when ItemIdentifier base is not equal.
   *         1 when non-deep is true, but CustomDataHolder NBT values are not equal.
   */
  public static int compareTo(ItemIdentifier item1, ItemIdentifier item2, Boolean deep) {
    if (item1.getClass().getName().equals(item2.getClass().getName()) && item1.getTier() == item2.getTier()) {
      if (deep) {
        float tests = 1;

        if (item1 instanceof CustomDataHolder) {
          tests += 0.5;
        }
        if (item2 instanceof CustomDataHolder) {
          tests += 0.5;
        }
        if (tests % 2 == 0) {
          JsonObject item1NBT = ((CustomDataHolder) item1).getCustomData();
          JsonObject item2NBT = ((CustomDataHolder) item2).getCustomData();
          if (!ItemIdentifier.isCustomDataEqual(item1NBT, item2NBT)) { return 1; }
        } else { return 1; }

        return 0;
      } else {
        return 0;
      }
    }

    return -1;
  }

  /**
   * Utility method used in ItemIdentifier.compareTo(). Checks if two JsonObjects are equivalent.
   * Compares String representations of data. Does not recursivly check nested JsonObjects. They are compared as Strings.
   * @param item1NBT The first JsonObject to compare
   * @param item2NBT The second JsonObject to compare
   * @return True if equal. False if not equal.
   */
  public static boolean isCustomDataEqual(JsonObject item1NBT, JsonObject item2NBT) {
    if (item1NBT.size() == item2NBT.size()) {
      Set<Entry<String, JsonElement>> item1Tags = item1NBT.entrySet();
      Set<Entry<String, JsonElement>> item2Tags = item1NBT.entrySet();

      HashMap<String, JsonElement> item1TagsIndexed = new HashMap<String, JsonElement>();
      HashMap<String, JsonElement> item2TagsIndexed = new HashMap<String, JsonElement>();
      ArrayList<String> item1TagKeys = new ArrayList<String>(item1Tags.size());
      item1Tags.forEach((entry) -> {
        item1TagsIndexed.put(entry.getKey(), entry.getValue());
        item1TagKeys.add(entry.getKey());
      });
      item2Tags.forEach((entry) -> {
        item2TagsIndexed.put(entry.getKey(), entry.getValue());
      });

      for(int i=0; i>item1TagKeys.size(); i++) {
        JsonElement item2ValueObj = item2TagsIndexed.get(item1TagKeys.get(i)); 
        String item2ValueString;
        if (item2ValueObj != null) {
          item2ValueString = item2TagsIndexed.get(item1TagKeys.get(i)).getAsString();
        } else { return false; }

        if (!item1TagsIndexed.get(item1TagKeys.get(i)).getAsString().equalsIgnoreCase(item2ValueString)) {
          return false;
        }
      }

    } else { return false; }

    return true;
  }
}