package net.peacefulcraft.sco.swordskills;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo;
import net.peacefulcraft.sco.swordskills.modules.BasicCombo.SwordSkillComboType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;

public class PoorMansThirdStrikeSkill extends SwordSkill {

    private ItemTier tier;

    public PoorMansThirdStrikeSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;

        this.useModule(new TimedCooldown(17000, (ModifierUser)c, "Poor Mans Third Strike", tier, null, "PlayerInteractEvent"));
        this.useModule(new BasicCombo(this, SwordSkillComboType.CONSECUTIVE_HITS_WITHOUT_TAKING_DAMAGE, 3));
        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));

        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
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
        if(ev instanceof PlayerInteractEvent) { return; }

        ModifierUser mu = (ModifierUser)c;
        double mult = mu.getMultiplier(ModifierType.PHYSICAL, false);

        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        evv.setDamage(evv.getDamage() * 1.8 * mult);
        
        SkillAnnouncer.messageSkill(
            (ModifierUser)c, 
            "Combo completed", 
            "Poor Mans Third Strike", 
            tier);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
