package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemIdentifier;
import net.peacefulcraft.sco.items.WeaponAttributeHolder;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;

public class SevereUpwardStrikeSkill extends SwordSkill {

    private int cooldown;
    private double vectorMultiplier;

    public SevereUpwardStrikeSkill(SwordSkillCaster c, double vectorMultiplier, int cooldown, SwordSkillProvider provider) {
        super(c, provider);
        this.vectorMultiplier = vectorMultiplier;
        this.cooldown = cooldown;

        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.useModule(new TimedCooldown(this.cooldown));
    }

    @Override
    public boolean skillSignature(Event ev) {
        return true;
    }

    @Override
    public boolean skillPreconditions(Event ev) {
        if(this.c instanceof SCOPlayer) {
            SCOPlayer s = (SCOPlayer)this.c;

            ItemIdentifier identifier = ItemIdentifier.resolveItemIdentifier(
                s.getPlayer().getInventory().getItemInMainHand()  
            );
            if(!(identifier instanceof WeaponAttributeHolder)) { return false; }
        }
        return true;
    }

    @Override
    public void triggerSkill(Event ev) {
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        if(evv.getEntity() instanceof LivingEntity) {
            LivingEntity liv = (LivingEntity)evv.getEntity();

            Vector dir = liv.getLocation().getDirection();
            liv.setVelocity(dir.multiply(-1 * (3 + this.vectorMultiplier)));
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
