package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * AsyncPlayerChatListener
 */
public class AbilityPlayerDeathListener implements Listener{

  @EventHandler
  public void onPlayerMessage(PlayerDeathEvent ev) {
    SwordCraftOnline.getGameManager().findSCOPlayer(ev.getEntity())
    .getSwordSkillManager().abilityExecuteLoop(SwordSkillType.PLAYER_DEATH, ev);
  }
}