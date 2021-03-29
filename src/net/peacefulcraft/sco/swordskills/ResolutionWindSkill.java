package net.peacefulcraft.sco.swordskills;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class ResolutionWindSkill extends SwordSkill {

    private double vectorModifier;
    private ItemTier tier;

    public ResolutionWindSkill(SwordSkillCaster c, double vectorModifier, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.vectorModifier = vectorModifier;

        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);

        this.useModule(new Trigger(SwordSkillType.SECONDARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(15000, (ModifierUser)c, "Resolution Wind", tier, null, "PlayerInteractEvent"));
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
        ModifierUser mu = (ModifierUser)c;
        Location center = mu.getLivingEntity().getLocation();

        for(Entity e : mu.getLivingEntity().getNearbyEntities(5, 5, 5)) {
            if(e instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)e;
                Location t = liv.getLocation().subtract(center);
                double distance = liv.getLocation().distance(center);

                t.getDirection().normalize().multiply(-1);
                t.multiply(distance / 1.2);
                t.multiply(2 + vectorModifier);

                liv.setVelocity(t.toVector());

                ModifierUser vic = ModifierUser.getModifierUser(liv);
                if(vic == null) { continue; }
                SkillAnnouncer.messageSkill(
                    mu, 
                    "Get back!", 
                    "Resolution Wind", 
                    tier);
            }
        }
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
