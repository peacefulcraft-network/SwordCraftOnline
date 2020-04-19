package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * AbilityEntityTeleportListener
 */
public class AbilityEntityTeleportListener implements Listener{

  @EventHandler
  public void onEntityTeleport(EntityTeleportEvent ev) {
    if(ev.getEntity() instanceof Player) {
      SwordCraftOnline.getGameManager().findSCOPlayer((Player) ev.getEntity())
      .getSwordSkillManager().abilityExecuteLoop(SwordSkillType.ENTITY_TELEPORT, ev);

    } else if(ev.getEntity() instanceof LivingEntity) {
      ActiveMob mob = SwordCraftOnline.getPluginInstance().getMobManager().getMythicMobInstance(ev.getEntity());
      if (mob != null) {
        mob.getSwordSkillManager().abilityExecuteLoop(SwordSkillType.ENTITY_TELEPORT, ev);
      }
    }
  }
}