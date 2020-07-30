package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SerratedBladeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Common Serrated Blade - Quartz Increases players critical hit chance.
 */
public class SerratedBladeItem implements SwordSkillProvider {

  private static long databaseId;
    @Override
    public long getDatabaseID() { return databaseId; }
    @Override
    public void setDatabaseID(long databaseId) { SerratedBladeItem.databaseId = databaseId; }

  private int increase;

  private ItemTier tier;
    @Override
    public ItemTier[] getAllowedTiers() {
      return new ItemTier[] {
        ItemTier.COMMON, ItemTier.UNCOMMON, ItemTier.RARE,
        ItemTier.LEGENDARY, ItemTier.ETHEREAL, ItemTier.GODLIKE
      };
    }

    @Override
    public ItemTier getTier() {
      return tier;
    }

  private int level;
    @Override
    public int[] getAllowedLevels() {
      return new int[] { 1 };
    }

    @Override
    public int getLevel() { return level; }

  public SerratedBladeItem(ItemTier tier, int level) {
    this.tier = tier;
    this.level = level;
  }

  @Override
  public SwordSkillType getType() {
    return SwordSkillType.PASSIVE;
  }

  @Override
  public Material getMaterial() {
    return Material.FLINT;
  }

  @Override
  public String getName() {
    return "Serrated Blade";
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
  public boolean isDroppable() {
    return false;
  }

  @Override
  public boolean isMovable() {
    return true;
  }

  @Override
  public NBTItem applyNBT(NBTItem item) {
    item.setString("config_string", getName().replaceAll(" ", "") + "-" + getTier().toString().toUpperCase());
    return item;
  }

  @Override
  public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
    SCOPlayer sp = caster.getSwordSkillManager().getSCOPlayer();
    sp.setCriticalChance(sp.getCriticalChance() + this.increase);
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
}