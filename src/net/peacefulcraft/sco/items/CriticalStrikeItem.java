package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class CriticalStrikeItem implements SwordSkillProvider {

  private ItemTier tier;
    @Override
    public ItemTier[] getAllowedTiers() {
      return new ItemTier[] { ItemTier.COMMON };
    }
    @Override
    public ItemTier getTier() {
      return tier;
    }

  private Integer level;
    @Override
    public Integer[] getAllowedLevels() {
      return new Integer[] { 1 };
    }
    @Override
    public Integer getLevel() {
      return level;
    }
    public void setLevel(Integer level) { this.level = level; }

  private Integer quantity;
    @Override
    public Integer getQuantity() {
      return quantity;
    }
    @Override
    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

  public CriticalStrikeItem(ItemTier tier, Integer quantity) {
    this.tier = tier;
    this.quantity = quantity;
  }

  @Override
  public Material getMaterial() {
    return Material.FLINT;
  }

  @Override
  public String getName() {
    return "Critical Strike";
  }

  @Override
  public ArrayList<String> getLore() {
    ArrayList<String> lore = new ArrayList<String>();
    lore.add(tier.getTierColor() + "A Beginners 3 hit combo.");
    switch (this.tier) {
      case COMMON:
        lore.add(tier.getTierColor() + "Combo Damage: 3");
        lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
        break;
      case UNCOMMON:
        lore.add(tier.getTierColor() + "Combo Damage: 4");
        lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
        break;
      case RARE:
        lore.add(tier.getTierColor() + "Combo Damage: 5");
        lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
        break;
      case LEGENDARY:
        lore.add(tier.getTierColor() + "Combo Damage: 7");
        lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
        break;
      case ETHEREAL:
        lore.add(tier.getTierColor() + "Combo Damage: 10");
        lore.add(tier.getTierColor() + "Cooldown: 4 seconds");
        break;
      case GODLIKE:
        lore.add(tier.getTierColor() + "Combo Damage: 12");
        lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
    }
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
  public SwordSkillType getType() {
    return SwordSkillType.PASSIVE;
  }

  @Override
  public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
    switch (this.tier) {
      case UNCOMMON:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 4);
      case RARE:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 5);
      case LEGENDARY:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 7);
      case ETHEREAL:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 10);
      case GODLIKE:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 12);
      default:
        return new CriticalStrikeSkill(caster, 5000, this, 3, 3);
    }
  }

  @Override
  public JsonObject getCustomData() {
    JsonObject json = new JsonObject();
    json.addProperty("level", this.level);
    return json;
  }

  @Override
  public void setCustomData(JsonObject data) {
    this.level = data.get("level").getAsInt();
  }

  @Override
  public void parseCustomItemData(ItemStack item) {
    NBTItem nbti = new NBTItem(item);
    this.level = nbti.getInteger("level");
  }

  @Override
  public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
    NBTItem nbti = new NBTItem(item);
    nbti.setInteger("level", data.get("level").getAsInt());
    return nbti.getItem();
  }
}