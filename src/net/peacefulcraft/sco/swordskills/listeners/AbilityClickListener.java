package net.peacefulcraft.sco.swordskills.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.swordskills.SwordSkillManager;
import net.peacefulcraft.sco.swordskills.SwordSkillTrigger;

public class AbilityClickListener implements Listener
{
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent ev) {

		SwordSkillManager executor = SwordCraftOnline.getGameManager().findSCOPlayer(ev.getPlayer()).getSwordSkillManager();

		if(ev.getAction().equals(Action.LEFT_CLICK_AIR) || ev.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			executor.abilityExecuteLoop(SwordSkillTrigger.PLAYER_INTERACT_LEFT_CLICK, ev);
		} else {
			executor.abilityExecuteLoop(SwordSkillTrigger.PLAYER_INTERACT_RIGHT_CLICK, ev);
		}

		executor.abilityExecuteLoop(SwordSkillTrigger.PLAYER_INTERACT, ev);
	}
}
