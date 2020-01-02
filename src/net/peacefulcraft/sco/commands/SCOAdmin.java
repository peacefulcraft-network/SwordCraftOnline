package net.peacefulcraft.sco.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.Item;

public class SCOAdmin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("scoadmin")) {
			if (args.length == 0) {
				sender.sendMessage("scoadmin [ swordskillbook | generateitem ]");
				return true;
			}

			if (args[0].equalsIgnoreCase("swordskillbook")) {
				Player p = (Player) sender;
				SwordCraftOnline.getGameManager();
				SwordSkillInventory inv = GameManager.findSCOPlayer(p).getData().getInventories()
						.getInventory(SwordSkillInventory.class);
				
				inv.openInventory();
				return true;
			}

			if (args[0].equals("generateitem")) {
				Player p = (Player) sender;
				if(!Item.itemExists(args[1])) { return false; }

				if(args.length == 2) {
					p.getInventory().addItem(Item.giveItem(args[1], null));
					return true;
				} 
				if(args.length == 3) {
					if(!Item.tierExists(args[2])) {return false; }

					p.getInventory().addItem(Item.giveItem(args[1], args[2]));
					return true;
				}
				p.sendMessage("scoadmin [ swordskillbook | generateitem ]");
				return true;
			}
		}
		return false;
	}

}
