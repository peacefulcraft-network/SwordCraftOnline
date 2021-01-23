package net.peacefulcraft.sco.swordskills.modules;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

/**
 * Inflicts a specific potion effect each
 */
public class EffectCombo extends BasicCombo {

    private HashMap<Integer, PotionEffectType> effects;
    private int effectLevel;
    private int effectDuration;

    public EffectCombo(SwordSkill ss, double activationThreshold, HashMap<Integer, PotionEffectType> effects, int effectLevel, int effectDuration) {
        super(ss, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, activationThreshold);
        
        this.effects = effects;
        this.effectLevel = effectLevel;
    }

    @Override
    protected void executeComboAccumulation(Event ev) {
        this.executeComboAccumulation(ev);
        
        if(this.comboAccumulation == 0) { return; }
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(effects.get((int)this.comboAccumulation), this.effectDuration, this.effectLevel));
    }
    
}
