package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.ParrySkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;

public class ParryItem extends SkillProvider {

    private int increase;

    public ParryItem(int level, ItemTier tier) {
        super("Parry", level, tier, null, Material.IRON_INGOT);
        setLore();
    }

    @Override
    public void registerSkill(SwordSkillCaster c) {
        SCOPlayer s = c.getSwordSkillManager().getSCOPlayer();

        setModifiers();

        s.setCriticalChance(s.getCriticalChance() + this.increase);

        new ParrySkill(s, -1, (SkillProvider)this, this.increase);
    }

    @Override
    public void setLore() {
        ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(getTierColor(this.tier) + "A beginners parry technique.");
		switch(this.tier) {
		case COMMON:
			lore.add(getTierColor(this.tier) + "Parry Chance: +5%");
		break;case UNCOMMON:
			lore.add(getTierColor(this.tier) + "Parry Chance: +7%");
		break;case RARE:
			lore.add(getTierColor(this.tier) + "Parry Chance: +9%");
		break;case LEGENDARY:
			lore.add(getTierColor(this.tier) + "Parry Chance: +11%");
		break;case ETHEREAL:
            lore.add(getTierColor(this.tier) + "Parry Chance: +13%");
        break;case GODLIKE:
			lore.add(getTierColor(this.tier) + "Parry Chance: +15%");
		}
		this.setLore(lore);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
			case COMMON:
				this.increase = 5;
			break;case UNCOMMON:
                this.increase = 7;
			break;case RARE:
                this.increase = 9;
			break;case LEGENDARY:
                this.increase = 11;
			break;case ETHEREAL:
                this.increase = 13;
			break;case GODLIKE:
                this.increase = 15;
		}
    }
    
}