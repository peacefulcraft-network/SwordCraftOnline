package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class VampireCarvingSkill extends SwordSkill {

    private Double healAdd;
    private ItemTier tier;

    public VampireCarvingSkill(SwordSkillCaster c, Double modifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);

        this.healAdd = modifier;
        this.tier = tier;
       
        this.listenFor(SwordSkillTrigger.PASSIVE);
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
        if(!(ev instanceof EntityDamageByEntityEvent)) { return; }
        if(healAdd == null) { return; }

        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        ModifierUser mu = (ModifierUser)c;

        double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);
        
        int addHealth = evv.getDamage() * healAdd < 1 ? 1 : (int) (evv.getDamage() * healAdd);
        addHealth *= mult;
        mu.convertHealth(-addHealth, true);
        SkillAnnouncer.messageSkill(
            mu, 
            "Healed for " + addHealth + " HP.", 
            "Vampire Carving", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
