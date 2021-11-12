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
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class TenCommandmentsPacifismSkill extends SwordSkill implements Runnable {

    private HashMap<ModifierUser, Long> vicMap = new HashMap<>();

    private BukkitTask vicTask;

    private ItemTier tier;

    public TenCommandmentsPacifismSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
        super(c, provider);
        this.tier = tier;
        
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
        if(!(evv.getDamager() instanceof LivingEntity)) { return; }

        ModifierUser mu = (ModifierUser)c;
        ModifierUser damMu = ModifierUser.getModifierUser(evv.getDamager());
        if(damMu == null) { return; }

        if(mu.getLivingEntity().getNearbyEntities(15, 15, 15).contains(evv.getDamager())) {
            if(vicMap.containsKey(damMu)) { return; }

            double mult = mu.getMultiplier(ModifierType.SPIRITUAL, false);
            int duration = (int) Math.ceil(40 * mult);

            vicMap.put(damMu, System.currentTimeMillis() + (duration * 1000));
            damMu.queueChange(
                -(damMu.getMaxHealth() / 2), 
                duration);
            if(evv.getDamager() instanceof Player) {
                SCOPlayer s = GameManager.findSCOPlayer((Player)evv.getDamager());
                if(s == null) { return; }

                SkillAnnouncer.messageSkill(
                    s, 
                    "You dare strike another in the presence of my conduit.", 
                    "Ten Commandments: Pacifism", 
                    tier);
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
