package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * AsyncPlayerChatListener
 */
public class AbilityPlayerMoveListener implements Listener{

  @EventHandler
  public void onPlayerMessage(PlayerMoveEvent ev) {
    SwordCraftOnline.getGameManager().findSCOPlayer(ev.getPlayer())
    .getSwordSkillManager().abilityExecuteLoop(SwordSkillType.PLAYER_MOVE, ev);
  }
}