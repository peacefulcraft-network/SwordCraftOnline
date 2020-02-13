package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillType;
import net.peacefulcraft.sco.swordskills.TamedSpiritSkill;

/**
 * Pet skill - Leather
 * Increases players critical hit chance when pet is active
 */
public class TamedSpiritItem extends SkillProvider {

    private int increase;

    public TamedSpiritItem(int level, ItemTier tier) {
        super("Tamed Spirit", level, tier, null, Material.LEATHER);
        setLore();
    }

    @Override
    public void registerSkill(SCOPlayer s) {
        setModifiers();

        s.setCriticalChance(s.getCriticalChance() + this.increase);

        TamedSpiritSkill ts = new TamedSpiritSkill(s, -1, (SkillProvider) this, this.increase);
        s.getSwordSkillManager().registerSkill(SwordSkillType.PASSIVE, ts);
    }

    @Override
    public void setLore() {
        ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(getTierColor(this.tier) + "Unleash your pets wild side.");
		switch(this.tier) {
		case COMMON:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +1%");
		break;case UNCOMMON:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +2%");
		break;case RARE:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +3%");
		break;case LEGENDARY:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +5%");
		break;case MASTERY:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +7%");
		break;case ETHEREAL:
			lore.add(getTierColor(this.tier) + "Critical Hit Chance: +10%");
		}
		this.setLore(lore);
    }

    @Override
    public void setModifiers() {
        switch(this.tier) {
			case COMMON:
				this.increase = 1;
			break;case UNCOMMON:
                this.increase = 2;
			break;case RARE:
                this.increase = 3;
			break;case LEGENDARY:
                this.increase = 5;
			break;case MASTERY:
                this.increase = 7;
			break;case ETHEREAL:
                this.increase = 10;
		}
    }
}