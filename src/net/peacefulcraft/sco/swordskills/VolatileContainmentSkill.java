package net.peacefulcraft.sco.swordskills;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;

public class VolatileContainmentSkill extends SwordSkill {

    private int taskId;

    public VolatileContainmentSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.PRIMARY));
        this.useModule(new TimedCooldown(27000));
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
        mu.queueChange(
            CombatModifier.PARRY_CHANCE, 
            -0.4, 
            5);

        taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SwordCraftOnline.getPluginInstance(), new Runnable(){
            int count = 0;
            
            @Override
			public void run() {
                if(count == 19) {
                    Bukkit.getServer().getScheduler().cancelTask(taskId);
                }        

                for(Entity e : mu.getLivingEntity().getNearbyEntities(5, 5, 5)) {
                    if(e instanceof LivingEntity) {
                        LivingEntity liv = (LivingEntity)e;
                        liv.addPotionEffect(new PotionEffect(
                            PotionEffectType.WITHER, 
                            100, 
                            2));
                    }
                }
                count++;
			}
        }, 2, 10);
    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {

    }
    
}
