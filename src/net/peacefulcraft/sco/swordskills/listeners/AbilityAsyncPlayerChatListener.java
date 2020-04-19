package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkillType;

/**
 * AsyncPlayerChatListener
 */
public class AbilityAsyncPlayerChatListener implements Listener{

  @EventHandler
  public void onPlayerMessage(AsyncPlayerChatEvent ev) {
    Bukkit.getScheduler().runTask(SwordCraftOnline.getPluginInstance(), () -> {
      SwordCraftOnline.getGameManager().findSCOPlayer(ev.getPlayer())
      .getSwordSkillManager().abilityExecuteLoop(SwordSkillType.PLAYER_SEND_CHAT, ev);
    });
  }
}