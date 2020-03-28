package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.swordskills.HeavyImpactSkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;

public class HeavyImpactItem extends SkillProvider {

    private int damage;

    public HeavyImpactItem(int level, ItemTier tier) {
        super("Heavy Impact", level, tier, null, Material.IRON_INGOT);
        setLore();
    }

    @Override
    public void registerSkill(SwordSkillCaster c) {
        setModifiers();

        new HeavyImpactSkill(c, 15000, (SkillProvider)this, this.damage);
    }

    @Override
    public void setLore() {
        ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(getTierColor(this.tier) + "Leap into the air and crash down on your enemies.");
		switch(this.tier) {
		case COMMON:
            lore.add(getTierColor(this.tier) + "Deals 3 Hearts AOE on impact.");
		break;case UNCOMMON:
			lore.add(getTierColor(this.tier) + "Deals 4 Hearts AOE on impact.");
		break;case RARE:
			lore.add(getTierColor(this.tier) + "Deals 5 Hearts AOE on impact.");
		break;case LEGENDARY:
			lore.add(getTierColor(this.tier) + "Deals 6 Hearts AOE on impact.");
		break;case ETHEREAL:
            lore.add(getTierColor(this.tier) + "Deals 8 Hearts AOE on impact.");
        break;case GODLIKE:
			lore.add(getTierColor(this.tier) + "Deals 10 Hearts AOE on impact.");
        }
        lore.add(getTierColor(this.tier) + "Cooldown for 15 seconds");
		this.setLore(lore);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
			case COMMON:
				this.damage = 3;
			break;case UNCOMMON:
                this.damage = 4;
			break;case RARE:
                this.damage = 5;
			break;case LEGENDARY:
                this.damage = 6;
			break;case ETHEREAL:
                this.damage = 8;
			break;case GODLIKE:
                this.damage = 10;
		}
    }
    
}