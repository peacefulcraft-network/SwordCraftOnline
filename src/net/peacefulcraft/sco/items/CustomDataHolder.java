package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.SwordCraftOnline;

/**
 * Sub category of ItemIdentifier.
 * For items which need to store custom data, in addition to the item-wide fields on item identiifer.
 */
public interface CustomDataHolder {

  /**
   * Get the custom nbt data on the item in JSON form
   * @return JsonObject with the custom nbt data
   */
  public abstract JsonObject getCustomData();

  /**
   * Takes a JSON object of custom data to attatch to the ItemIdentifier
   * @param data The custom nbt data to attatch
   */
  public abstract void setCustomData(JsonObject data);

  /**
   * Take in an this custom data holder and read off the custom nbt data,
   * populating the properties of this custom data holder.
   * @param item An ItemStack which this CustomDataHolder represents
   */
  public abstract void parseCustomItemData(ItemStack item);

  /**
   * Take in a JSON object of custom data for this object and
   * apply it to the supplied item stack's NBT tags.
   * @param item The item to apply the NBT tags to
   * @param data The NBT tags to apply
   */
  public abstract ItemStack applyCustomItemData(ItemStack item, JsonObject data);

  public static ArrayList<String> parseDynamicLore(JsonObject data) {
    try {
      ArrayList<String> desc = new ArrayList<>();
      String s = data.get("lore").getAsString();
      for(String split : s.split("\n")) {
        String sColor = StringUtils.substringBetween(split, "ChatColor{", "}");
        if(!(sColor == null || sColor.isEmpty())) {
          split = StringUtils.substringAfter(split, "}");
          split = ChatColor.valueOf(sColor) + split;
        }

        desc.add(split);
      }
      return desc;
    } catch(NullPointerException ex) {
      SwordCraftOnline.logDebug("[Custom Data Holder] Failed to load lore from Json.");
      return new ArrayList<String>();
    }   
  }
}