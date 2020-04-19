package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.ForwardLungeSkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;

public class ForwardLungeItem extends SkillProvider {

    private double increase;

    public ForwardLungeItem(int level, ItemTier tier) {
        super("Forward Lunge", level, tier, null, Material.BIRCH_DOOR);
        setLore();
    }

    @Override
    public void registerSkill(SwordSkillCaster c) {
        SCOPlayer s = c.getSwordSkillManager().getSCOPlayer();

        setModifiers();

        new ForwardLungeSkill(s, 15000, (SkillProvider)this, this.increase);
    }

    @Override
    public void setLore() {
        ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(getTierColor(this.tier) + "Powerful forward lunge technique.");
		switch(this.tier) {
		case COMMON:
            lore.add(getTierColor(this.tier) + "Damage Increased 20% for 2 seconds.");
		break;case UNCOMMON:
			lore.add(getTierColor(this.tier) + "Damage Increased 30% for 2 seconds.");
		break;case RARE:
			lore.add(getTierColor(this.tier) + "Damage Increased 40% for 2 seconds.");
		break;case LEGENDARY:
			lore.add(getTierColor(this.tier) + "Damage Increased 50% for 2 seconds.");
		break;case ETHEREAL:
            lore.add(getTierColor(this.tier) + "Damage Increased 60% for 2 seconds.");
        break;case GODLIKE:
			lore.add(getTierColor(this.tier) + "Damage Increased 70% for 2 seconds.");
        }
        lore.add(getTierColor(this.tier) + "Cooldown for 15 seconds");
		this.setLore(lore);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
			case COMMON:
				this.increase = 1.2;
			break;case UNCOMMON:
                this.increase = 1.3;
			break;case RARE:
                this.increase = 1.4;
			break;case LEGENDARY:
                this.increase = 1.5;
			break;case ETHEREAL:
                this.increase = 1.6;
			break;case GODLIKE:
                this.increase = 1.7;
		}
    }
    
}