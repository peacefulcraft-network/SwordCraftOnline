package net.peacefulcraft.sco.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.inventories.listeners.InventoryCloser;
import net.peacefulcraft.sco.items.Item;

public class SCOAdmin implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("scoadmin")) {
			if (args.length == 0) {
				sender.sendMessage("scoadmin [ swordskillbook | generateitem | playerdata | setplayerdata ]");
				return true;
			}

			if (args[0].equalsIgnoreCase("swordskillbook")) {
				Player p = (Player) sender;
				SwordCraftOnline.getGameManager();
				SwordSkillInventory inv = GameManager.findSCOPlayer(p).getData().getInventories()
						.getInventory(SwordSkillInventory.class);
				
				inv.openInventory();
				
				// Track the inventory so it saves when closed
				InventoryCloser.trackInventory(p.getUniqueId(), inv);
				return true;
			}

			if (args[0].equalsIgnoreCase("generateitem")) {
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
				return false;
			}

			if (args[0].equalsIgnoreCase("playerdata")) {
				Player p = (Player) sender;
				
				SCOPlayer s = GameManager.findSCOPlayerByName(args[1]);
				if(s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player");
					return true;
				}

				p.sendMessage(s.getPlayerData());
				return true;
			}

			if (args[0].equalsIgnoreCase("setplayerdata")) {
				Player p = (Player) sender;
				
				if(args.length == 1) {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED + "critical_chance, crit_chance, critical_multiplier, crit_mult, " + 
									"player_kills, parry, admin_override, admin_over");
					return true;
				}
				SCOPlayer s = GameManager.findSCOPlayerByName(args[1]);
				if(s == null) {
					p.sendMessage(ChatColor.RED + "Could not find player");
					return true;
				}

				double i;
				if(args.length == 2) {
					i = 1;
				} else {
					i = Double.parseDouble(args[3]);
				}

				String data = args[2];
				if(data.equalsIgnoreCase("critical_chance") || data.equalsIgnoreCase("crit_chance")) {
					s.setCriticalChance((int) i);
					p.sendMessage(ChatColor.GOLD + "Critical Chance set to: " + ChatColor.RED + i);
					return true;
				} else if(data.equalsIgnoreCase("critical_multiplier") || data.equalsIgnoreCase("crit_mult")) {
					s.setCriticalMultiplier(i);
					p.sendMessage(ChatColor.GOLD + "Critical Multiplier set to: " + ChatColor.RED + i);
					return true;
				} else if(data.equalsIgnoreCase("player_kills")) {
					p.sendMessage(ChatColor.GOLD + "Player Kills set to: " + ChatColor.RED + i);
					s.setPlayerKills((int) i);
					return true;
				} else if(data.equalsIgnoreCase("admin_override") || data.equalsIgnoreCase("admin_over")) {
					p.sendMessage(ChatColor.GOLD + "Admin Override set to: " + ChatColor.RED + s.hasOverride());
					s.setAdminOverride(!s.hasOverride());
					return true;
				} else if(data.equalsIgnoreCase("parry")) {
					p.sendMessage(ChatColor.GOLD + "Parry Chance set to: " + ChatColor.RED + i);
					s.setParryChance((int) i);
					return true;
				} else {
					p.sendMessage(ChatColor.GOLD + "Valid arguments: " + ChatColor.RED + "critical_chance, crit_chance, critical_multiplier, crit_mult, " + 
								"player_kills, parry, admin_override, admin_over");
					return true;
				}
			}

		}
		return false;
	}

}
