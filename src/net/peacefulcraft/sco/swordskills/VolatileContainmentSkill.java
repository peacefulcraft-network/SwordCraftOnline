package net.peacefulcraft.sco.swordskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.modules.TimedCooldown;
import net.peacefulcraft.sco.swordskills.modules.Trigger;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser.CombatModifier;
import net.peacefulcraft.sco.utilities.Pair;

public class VolatileContainmentSkill extends SwordSkill {

    private int taskId;
    private ItemTier tier;

    public VolatileContainmentSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK);
        this.useModule(new Trigger(SwordSkillType.PRIMARY, (ModifierUser)c));
        this.useModule(new TimedCooldown(27000, (ModifierUser)c, "Volatile Containment", tier));
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
        SkillAnnouncer.messageSkill(
            mu, 
            new Pair<String, Double>(CombatModifier.PARRY_CHANCE.toString(), -0.4), 
            "Volatile Containment", 
            tier);

        double mult = mu.getMultiplier(new ModifierType[] { ModifierType.ICE, ModifierType.FIRE }, false);

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
                            (int) Math.ceil(2 * mult)));
                        liv.addPotionEffect(new PotionEffect(
                            PotionEffectType.SLOW,
                            60,
                            (int) Math.ceil(1 * mult)));

                        ModifierUser mu = ModifierUser.getModifierUser(liv);
                        if(mu == null) { continue; }

                        List<Pair<String,Integer>> posLis = new ArrayList<>();
                        posLis.add(new Pair<String, Integer>(PotionEffectType.WITHER.toString(), 5));
                        posLis.add(new Pair<String, Integer>(PotionEffectType.SLOW.toString(), 3));
                        SkillAnnouncer.messageSkill(
                            mu, 
                            "Volatile Containment", 
                            tier, 
                            posLis);
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
