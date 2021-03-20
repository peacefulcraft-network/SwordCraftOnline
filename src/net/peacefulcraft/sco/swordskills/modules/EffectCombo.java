package net.peacefulcraft.sco.swordskills.modules;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.SwordSkill;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.Pair;

/**
 * Inflicts a specific potion effect each
 */
public class EffectCombo extends BasicCombo {

    private HashMap<Integer, PotionEffectType> effects;
    private int effectLevel;
    private int effectDuration;
    private ItemTier tier;
    private String name;

    public EffectCombo(SwordSkill ss, double activationThreshold, HashMap<Integer, PotionEffectType> effects, int effectLevel, int effectDuration, ItemTier tier, String name) {
        super(ss, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, activationThreshold);
        
        this.effects = effects;
        this.effectLevel = effectLevel;
        this.effectDuration = effectDuration;
        this.tier = tier;
        this.name = name;
    }

    @Override
    public boolean beforeSkillPreconditions(SwordSkill ss, Event ev) {
        // We want player interacts to pass to the trigger
        if(ev instanceof PlayerInteractEvent) { return true; }
        this.executeComboAccumulation(ev);
        return hasMetActivationThreshold();
    }

    @Override
    protected void executeComboAccumulation(Event ev) {   
        super.executeComboAccumulation(ev);

        if(this.comboAccumulation == 0) { return; }
        if(effects.get((int)this.comboAccumulation) == null) { return; }

        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(effects.get((int)this.comboAccumulation), this.effectDuration, this.effectLevel));
        SkillAnnouncer.messageSkill(
            mu, 
            name, 
            tier, 
            new Pair<String, Integer>(effects.get((int)this.comboAccumulation).toString(), this.effectLevel));
    }
    
}
