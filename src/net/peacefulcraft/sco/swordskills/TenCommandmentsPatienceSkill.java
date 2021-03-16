package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class TenCommandmentsPatienceSkill extends SwordSkill implements Runnable {

    private HashMap<ModifierUser, Long> vicMap = new HashMap<>();

    private BukkitTask vicTask;

    private ItemTier tier;

    public TenCommandmentsPatienceSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
        this.listenFor(SwordSkillTrigger.PASSIVE);
        this.vicTask = Bukkit.getServer().getScheduler().runTaskTimer(
            SwordCraftOnline.getPluginInstance(), 
            this, 
            20, 
            40);
    }

    @Override
    public void run() {
        ModifierUser mu = (ModifierUser)c;

        List<Entity> nearby = mu.getLivingEntity().getNearbyEntities(15, 15, 15);
        for(Entity e : nearby) {
            if(!(e instanceof LivingEntity)) { continue; }

            ModifierUser vicMu = ModifierUser.getModifierUser(e);
            if(vicMu == null) { continue; }

            if(vicMu.getAttribute(Attribute.GENERIC_ARMOR) > 5) {
                if(vicMap.containsKey(vicMu)) { continue; }
                vicMap.put(vicMu, System.currentTimeMillis() + 40000);
                vicMu.getLivingEntity().addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 
                    99999, 
                    3));
                if(vicMu instanceof SCOPlayer) {
                    SkillAnnouncer.messageSkill(
                        vicMu, "Stay collected near my conduit... else you may lose your eyes.", 
                        "Ten Commandments: Patience", 
                        tier);
                }
            }
        }

        Iterator<Entry<ModifierUser, Long>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<ModifierUser, Long> entry = iter.next();

            // Check if entity is nearby
            // If entity is in nearby list we set false
            // If entity is not nearby we leave true to clear effect
            boolean nearbyCheck = true;
            for(Entity e : nearby) {
                if(e.getUniqueId().equals(entry.getKey().getLivingEntity().getUniqueId())) {
                    nearbyCheck = false;
                    break;
                } 
            }

            if(entry.getValue() <= System.currentTimeMillis() || nearbyCheck) {
                entry.getKey().getLivingEntity().removePotionEffect(PotionEffectType.BLINDNESS);
                iter.remove();
            }
        }
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

    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        this.vicTask.cancel();
        Iterator<Entry<ModifierUser, Long>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<ModifierUser, Long> entry = iter.next();
            entry.getKey().getLivingEntity().removePotionEffect(PotionEffectType.BLINDNESS);
        }
        vicMap.clear();
    }
    
}
