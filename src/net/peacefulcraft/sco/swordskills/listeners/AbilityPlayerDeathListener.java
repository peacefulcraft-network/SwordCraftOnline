package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * AsyncPlayerChatListener
 */
public class AbilityPlayerDeathListener implements Listener{

  @EventHandler
  public void onPlayerMessage(PlayerDeathEvent ev) {
    GameManager.findSCOPlayer(ev.getEntity())
    .getSwordSkillManager().abilityExecuteLoop(SwordSkillTrigger.PLAYER_DEATH, ev);
  }
}