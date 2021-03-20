package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.utilities.Pair;

public class SerpentsBiteSkill extends SwordSkill {

    private int poisonModifier;
    private ItemTier tier;

    public SerpentsBiteSkill(SwordSkillCaster c, int poisonModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.poisonModifier = poisonModifier;
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
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
        ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
        if(mu == null) { return; }

        mu.getLivingEntity().addPotionEffect(
            new PotionEffect(PotionEffectType.POISON, 100, poisonModifier)
        );
        SkillAnnouncer.messageSkill(
            mu, 
            "Serpents Bite", 
            tier, 
            new Pair<String, Integer>(PotionEffectType.POISON.toString(), 5));
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
