package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.swordskills.CriticalStrikeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.SwordSkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

public class CriticalStrikeItem implements ItemIdentifier, SwordSkillProvider {

  private ItemTier tier;

  public CriticalStrikeItem(ItemTier tier) {
    this.tier = tier;
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
		switch(this.tier) {
		case COMMON:
			lore.add(tier.getTierColor() + "Combo Damage: 3");
			lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
		break;case UNCOMMON:
			lore.add(tier.getTierColor() + "Combo Damage: 4");
			lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
		break;case RARE:
			lore.add(tier.getTierColor() + "Combo Damage: 5");
			lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
		break;case LEGENDARY:
			lore.add(tier.getTierColor() + "Combo Damage: 7");
			lore.add(tier.getTierColor() + "Cooldown: 5 seconds");
		break;case ETHEREAL:
			lore.add(tier.getTierColor() + "Combo Damage: 10");
			lore.add(tier.getTierColor() + "Cooldown: 4 seconds");
		break;case GODLIKE:
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
  public boolean isDynamic() {
    return false;
  }

  @Override
  public NBTItem applyNBT(NBTItem item) {
    item.setString("config_string", getName().replaceAll(" ", "") + "-" + getTier().toString().toUpperCase());
    return item;
  }

  @Override
  public ItemTier getTier() {
    return tier;
  }

  @Override
  public SwordSkillType getType() {
    return SwordSkillType.PASSIVE;
  }

  @Override
  public SwordSkill registerSwordSkill(SwordSkillCaster caster) {
    switch(this.tier) {
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
}