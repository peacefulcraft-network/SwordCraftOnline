package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.swordskills.modules.EffectCombo;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class ThiefKingsDemonLedgerComboSkill extends SwordSkill {

    public ThiefKingsDemonLedgerComboSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        HashMap<Integer, PotionEffectType> effects = new HashMap<>();
        effects.put(1, PotionEffectType.WEAKNESS);
        effects.put(2, PotionEffectType.POISON);
        effects.put(3, PotionEffectType.WITHER);

        this.useModule(new Trigger(SwordSkillType.PRIMARY));
        this.useModule(new EffectCombo(this, 4, effects, 4, 6));
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        evv.setDamage(evv.getDamage() * 3);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
