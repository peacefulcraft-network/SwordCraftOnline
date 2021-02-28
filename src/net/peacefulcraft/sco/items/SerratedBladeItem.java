package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.peacefulcraft.sco.swordskills.SerratedBladeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillDesc;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Common Serrated Blade - Quartz Increases players critical hit chance.
 */
public class SerratedBladeItem implements SwordSkillProvider {

  @Override
  public String getName() { return "Serrated Blade"; }

  @Override
  public String getDisplayName() {
    return ItemTier.getTierColor(getTier()) + "Serrated Blade";
  }

  @Override
  public ArrayList<String> getLore() {
    return desc.getDesc();
  }

  @Override
  public Material getMaterial() { return Material.FLINT; }

  private Integer quantity;
    @Override
    public Integer getQuantity() { return quantity; }
    
    @Override
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

  private ItemTier tier;
    @Override
    public ItemTier[] getAllowedTiers() {
      return new ItemTier[] {
          ItemTier.COMMON, ItemTier.UNCOMMON, ItemTier.RARE,
          ItemTier.LEGENDARY, ItemTier.ETHEREAL, ItemTier.GODLIKE
        };
    }
    
    @Override
    public ItemTier getTier() { return tier; }

  @Override
  public boolean isDroppable() { return false; }

  @Override
  public boolean isMovable() { return true; }

  private SwordSkillDesc desc;
  private SwordSkillType type;

  public SerratedBladeItem(ItemTier tier, Integer quantity) {
    this.tier = tier;
    this.quantity = quantity;
    this.type = SwordSkillType.PASSIVE;
    this.desc = new SwordSkillDesc(tier, type);
    desc.add("A beginners sword upgrade.");
    switch (this.tier) {
      case COMMON:
        desc.add("Critical Hit Chance: +1%");
      break; case UNCOMMON:
        desc.add("Critical Hit Chance: +2%");
      break; case RARE:
        desc.add("Critical Hit Chance: +3%");
      break; case LEGENDARY:
        desc.add("Critical Hit Chance: +5%");
      break; case ETHEREAL:
        desc.add("Critical Hit Chance: +7%");
      break; case GODLIKE:
        desc.add("Critical Hit Chance: +10%");
    }
  }

  @Override
  public SwordSkillType getType() { return type; }

  @Override
  public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
    switch (this.tier) {
      case UNCOMMON:
        return new SerratedBladeSkill(caster, this, 2);
      case RARE:
        return new SerratedBladeSkill(caster, this, 3);
      case LEGENDARY:
        return new SerratedBladeSkill(caster, this, 5);
      case ETHEREAL:
        return new SerratedBladeSkill(caster, this, 7);
      case GODLIKE:
        return new SerratedBladeSkill(caster, this, 10);
      default:
        return new SerratedBladeSkill(caster, this, 1);
    }
  }

  @Override
  public JsonObject getCustomData() {
    return new JsonObject();
  }

  @Override
  public void setCustomData(JsonObject data) {
  }

  @Override
  public void parseCustomItemData(ItemStack item) {

  }

  @Override
  public ItemStack applyCustomItemData(ItemStack item, JsonObject data) {
    return item;
  }

  @Override
  public void setModifiers() {

  }
}