package net.peacefulcraft.sco.swordskills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class HunterSightSkill extends SwordSkill {

    public HunterSightSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new TimedCooldown(43000));
        this.useModule(new Trigger(SwordSkillType.SECONDARY));
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

        for(Entity e : mu.getLivingEntity().getNearbyEntities(10, 10, 10)) {
            if(e instanceof LivingEntity) {
                LivingEntity liv = (LivingEntity)e;
                liv.addPotionEffect(new PotionEffect(
                    PotionEffectType.GLOWING, 
                    400, 
                    1));
            }
            if(e instanceof Player) {
                Player p = (Player)e;
                Announcer.messagePlayer(p, "You have been spotted...", 0);
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
