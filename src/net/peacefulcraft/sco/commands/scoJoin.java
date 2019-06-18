package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;

public class scoJoin implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("scojoin")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				SwordCraftOnline.getGameManager().joinGame(p);
				return true;				
			} else {
				System.out.println("@" + sender + " Command only executable by players");
			}
		}
		
		return false;
	}
	
}
