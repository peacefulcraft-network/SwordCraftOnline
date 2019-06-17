package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;

public class scoJoin implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("scojoin")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				SCOPlayer s = SwordCraftOnline.getGameManager().joinGame(p);
				return true;				
			}
		}
		
		return false;
	}
	
}
