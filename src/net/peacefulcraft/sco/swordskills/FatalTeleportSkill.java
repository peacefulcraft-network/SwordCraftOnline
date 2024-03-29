package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class FatalTeleportSkill extends SwordSkill {

    private ModifierUser lastHit = null;
    private double damageModifier;
    private ItemTier tier;

    public FatalTeleportSkill(SwordSkillCaster c, double damageModifier, int cooldown, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.damageModifier = damageModifier;
        this.tier = tier;

        this.useModule(new TimedCooldown(cooldown, (ModifierUser)c, "Fatal Teleport", tier));
        this.useModule(new Trigger(SwordSkillType.SWORD, (ModifierUser)c));
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT);
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if(ev instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
            ModifierUser mu = ModifierUser.getModifierUser(evv.getEntity());
            if(mu == null) { return false; }

            this.lastHit = mu;
            return false;
        } else if(ev instanceof PlayerInteractEvent) {
            if(this.lastHit == null || this.lastHit.getLivingEntity().isDead()) { return false; }
        }
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        if(ev instanceof PlayerInteractEvent) {
            PlayerInteractEvent evv = (PlayerInteractEvent)ev;
            ModifierUser mu = ModifierUser.getModifierUser(evv.getPlayer());
            if(mu == null) { return; }

            Vector inverse = lastHit.getLivingEntity().getLocation().getDirection().normalize().multiply(-1);
            Location behind = lastHit.getLivingEntity().getLocation().add(inverse);

            SkillAnnouncer.messageSkill(
                mu, 
                "Look behind you...", 
                "Fatal Teleport", 
                tier);

            mu.getLivingEntity().teleport(behind);
            mu.queueChange(
                Attribute.GENERIC_ATTACK_DAMAGE,
                mu.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) + damageModifier, 
                2);
        }
    }

    @Override
    public void skillUsed() {
        this.lastHit = null;
    }

    @Override
    public void unregisterSkill() {

    }
    
}
