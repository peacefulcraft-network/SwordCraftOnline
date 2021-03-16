package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

/**
 * AsyncPlayerChatListener
 */
public class AbilityPlayerRespawnListener implements Listener{

  @EventHandler
  public void onPlayerMessage(PlayerRespawnEvent ev) {
    GameManager.findSCOPlayer(ev.getPlayer())
    .getSwordSkillManager().abilityExecuteLoop(SwordSkillTrigger.PLAYER_RESPAWN, ev);
  }
}