package net.peacefulcraft.sco.items;

import java.util.ArrayList;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SerratedBladeSkill;
import net.peacefulcraft.sco.swordskills.SkillProvider;
import net.peacefulcraft.sco.swordskills.SwordSkillCaster;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

/**
 * Common Serrated Blade - Quartz Increases players critical hit chance.
 */
public class SerratedBladeItem extends SkillProvider {
    
    private int increase;
    
    public SerratedBladeItem(int level, ItemTier tier) {
        super("Serrated Blade", level, tier, null, Material.QUARTZ);
        setLore();
    }

    @Override
    public void registerSkill(SwordSkillCaster s) {
        SCOPlayer sp = s.getSwordSkillManager().getSCOPlayer();
        
        setModifiers();

        sp.addCombatModifier(CombatModifier.CRITICAL_CHANCE, this.increase, -1);

        new SerratedBladeSkill(sp, -1, (SkillProvider) this, this.increase);
    }

    @Override
    public void setLore() {
        ArrayList<String> lore = SkillProvider.addDesc(this.tier);
		lore.add(ItemTier.getTierColor(this.tier) + "A beginners sword upgrade.");
		switch(this.tier) {
		case COMMON:
			lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +1%");
		break;case UNCOMMON:
			lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +2%");
		break;case RARE:
			lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +3%");
		break;case LEGENDARY:
			lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +5%");
		break;case ETHEREAL:
            lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +7%");
        break;case GODLIKE:
			lore.add(ItemTier.getTierColor(this.tier) + "Critical Hit Chance: +10%");
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
			break;case ETHEREAL:
                this.increase = 7;
			break;case GODLIKE:
                this.increase = 10;
		}
    }
}