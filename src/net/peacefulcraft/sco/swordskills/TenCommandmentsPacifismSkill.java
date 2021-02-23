package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.Announcer;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;

public class TenCommandmentsPacifismSkill extends SwordSkill implements Runnable {

    private HashMap<ModifierUser, Long> vicMap = new HashMap<>();

    private BukkitTask vicTask;

    public TenCommandmentsPacifismSkill(SwordSkillCaster c, SwordSkillProvider provider) {
        super(c, provider);
        
        this.listenFor(SwordSkillTrigger.ENTITY_DAMAGE_ENTITY_GIVE);
        this.vicTask = Bukkit.getServer().getScheduler().runTaskTimer(
            SwordCraftOnline.getPluginInstance(), 
            this, 
            20, 
            40);
    }

    @Override
    public void run() {
        Iterator<Entry<ModifierUser, Long>> iter = vicMap.entrySet().iterator();
        while(iter.hasNext()) {
            Entry<ModifierUser, Long> entry = iter.next();

            if(entry.getValue() <= System.currentTimeMillis()) {
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
        if(!(ev instanceof EntityDamageByEntityEvent)) { return; }
        EntityDamageByEntityEvent evv = (EntityDamageByEntityEvent)ev;
        if(!(evv.getEntity() instanceof LivingEntity)) { return; }

        ModifierUser mu = (ModifierUser)c;
        if(mu.getLivingEntity().getNearbyEntities(15, 15, 15).contains(evv.getEntity())) {
            ModifierUser damMu = ModifierUser.getModifierUser(evv.getEntity());
            if(damMu == null) { return; }

            if(vicMap.containsKey(damMu)) { return; }
            vicMap.put(damMu, System.currentTimeMillis() + 40000);
            damMu.queueChange(
                -(damMu.getMaxHealth() / 2), 
                40);
            if(evv.getEntity() instanceof Player) {
                Announcer.messagePlayer(
                    (Player)evv.getEntity(), 
                    "[Pacifism] You dare strike another in the presence of my conduit.", 
                    0);
            }
        }
    }

    @Override
    public void skillUsed() {
        
    }

    @Override
    public void unregisterSkill() {
        this.vicTask.cancel();
        vicMap.clear();
    }
    
}
