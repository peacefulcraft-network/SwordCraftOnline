package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;

public class SCOAdmin implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("scoadmin")){
			if(args.length == 0) {
				sender.sendMessage("scoadmin [ swordskillbook ]");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("swordskillbook")) {
				Player p = (Player) sender;
				SwordSkillInventory inv = SwordCraftOnline.getGameManager().findSCOPlayer(p).getData().getInventories().getInventory(SwordSkillInventory.class);
				
				inv.openInventory();
				return true;
			}
		}
		return false;
	}

}
