package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;

public class scoLeave implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("scoLeave")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				SwordCraftOnline.getGameManager().leaveGame(p);
				return true;
			}
		}

		
		return false;
	}

}
