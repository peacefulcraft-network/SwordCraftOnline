package net.peacefulcraft.sco.items;

import org.bukkit.Material;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.SerratedBladeSkill;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * Common Serrated Blade - Quartz
 * Increases players critical hit chance.
 */
public class SerratedBladeItem extends SkillProvider {
    
    
    public SerratedBladeItem(int level, ItemTier tier) {
        super("Serrated Blade", level, tier, null, Material.QUARTZ);
    }

    @Override
    public void registerSkill(SCOPlayer s) {
        //TODO: Add Modifiers and new swordskill type.
        setModifiers();
        SerratedBladeSkill sb = new SerratedBladeSkill(s, delay, provider, increase);
        s.getSwordSkillManager().registerSkill(SwordSkillType., skill);
    }
}