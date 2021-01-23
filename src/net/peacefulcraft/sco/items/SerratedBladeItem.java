package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SerratedBladeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

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
    ArrayList<String> lore = new ArrayList<String>();
    lore.add(tier.getTierColor() + "A beginners sword upgrade.");

    switch (this.tier) {
      case COMMON:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +1%");
        break;
      case UNCOMMON:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +2%");
        break;
      case RARE:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +3%");
        break;
      case LEGENDARY:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +5%");
        break;
      case ETHEREAL:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +7%");
        break;
      case GODLIKE:
        lore.add(tier.getTierColor() + "Critical Hit Chance: +10%");
    }

    return lore;
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

  private int level;

  @Override
  public boolean isDroppable() { return false; }

  @Override
  public boolean isMovable() { return true; }

  private int increase;

  public SerratedBladeItem(ItemTier tier, Integer level) {
    this.tier = tier;
    this.level = level;
  }

  @Override
  public SwordSkillType getType() { return SwordSkillType.PASSIVE; }

  @Override
  public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
    SCOPlayer sp = caster.getSwordSkillManager().getSCOPlayer();
    sp.addToCombatModifier(CombatModifier.CRITICAL_CHANCE, this.increase, -1);
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

  @Override
  public void setModifiers() {

  }
}