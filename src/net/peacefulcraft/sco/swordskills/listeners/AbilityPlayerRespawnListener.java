package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * AsyncPlayerChatListener
 */
public class AbilityPlayerRespawnListener implements Listener{

  @EventHandler
  public void onPlayerMessage(PlayerRespawnEvent ev) {
    SwordCraftOnline.getGameManager().findSCOPlayer(ev.getPlayer())
    .getSwordSkillManager().abilityExecuteLoop(SwordSkillType.PLAYER_RESPAWN, ev);
  }
}