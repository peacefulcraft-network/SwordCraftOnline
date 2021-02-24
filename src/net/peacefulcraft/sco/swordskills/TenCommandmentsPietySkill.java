package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class TenCommandmentsPietySkill extends SwordSkill implements Runnable {

    private HashMap<ModifierUser, Long> vicMap = new HashMap<>();

    private BukkitTask vicTask;

    public TenCommandmentsPietySkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
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

            if(vicMu.getLivingEntity().hasPotionEffect(PotionEffectType.REGENERATION)) {
                if(vicMap.containsKey(vicMu)) { continue; }
                vicMap.put(vicMu, System.currentTimeMillis() + 20000);
                vicMu.getLivingEntity().addPotionEffect(new PotionEffect(
                    PotionEffectType.POISON, 
                    99999, 
                    2));
                if(vicMu instanceof SCOPlayer) {
                    Announcer.messagePlayer(
                        (SCOPlayer)vicMu, 
                        "[Piety] You try to heal near my conduit?.. No.", 
                        0);
                }
            }
        }

        Iterator<Entry<ModifierUser, Long>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<ModifierUser, Long> entry = iter.next();

            boolean nearbyCheck = true;
            for(Entity e : nearby) {
                if(e.getUniqueId().equals(entry.getKey().getLivingEntity().getUniqueId())) {
                    nearbyCheck = false;
                    break;
                } 
            }

            if(entry.getValue() <= System.currentTimeMillis() || nearbyCheck) {
                entry.getKey().getLivingEntity().removePotionEffect(PotionEffectType.POISON);
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
