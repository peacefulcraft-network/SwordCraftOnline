package net.peacefulcraft.sco.swordskills;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitTask;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.announcer.SkillAnnouncer;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.swordskills.utilities.ModifierUser;
import net.peacefulcraft.sco.swordskills.utilities.Modifier.ModifierType;

public class TenCommandmentsReposeSkill extends SwordSkill implements Runnable {

    private HashMap<ModifierUser, Long> vicMap = new HashMap<>();

    private BukkitTask vicTask;

    private ItemTier tier;

    public TenCommandmentsReposeSkill(SwordSkillCaster c, SwordSkillProvider provider, ItemTier tier) {
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

        for(Entity e : mu.getLivingEntity().getNearbyEntities(15, 15, 15)) {
            if(!(e instanceof LivingEntity)) { continue; }

            ModifierUser vicMu = ModifierUser.getModifierUser(e);
            if(vicMu == null) { continue; }

            if(vicMu.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) >= ModifierUser.getBaseGenericMovement(vicMu)) {
                if(vicMap.containsKey(vicMu)) { continue; }

                double mult = mu.getMultiplier(ModifierType.SPIRITUAL, false);

                vicMap.put(vicMu, System.currentTimeMillis() + 35000);
                vicMu.queueChange(
                    Attribute.GENERIC_ATTACK_DAMAGE, 
                    -((vicMu.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) / 2) * mult), 
                    35);

                SkillAnnouncer.messageSkill(
                    mu, 
                    "You are next to my conduit. You should be... calm.", 
                    "Ten Commandments: Repose", 
                    tier);
            }
        }

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

    }

    @Override
    public void skillUsed() {

    }

    @Override
    public void unregisterSkill() {
        this.vicTask.cancel();
        this.vicMap.clear();
    }
    
}
