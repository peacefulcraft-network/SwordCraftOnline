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
import net.peacefulcraft.sco.inventory.InventoryType;
import net.peacefulcraft.sco.item.ItemTier;
import net.peacefulcraft.sco.swordskill.SwordSkill;
import net.peacefulcraft.sco.swordskills.utilities.Generator;
import net.peacefulcraft.sco.swordskills.utilities.Validator;

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
				SwordSkillInventory inv = (SwordSkillInventory) GameManager.findSCOPlayer(p).getData().getInventories()
						.getInventory(InventoryType.SWORD_SKILL);
				
				inv.openInventory();
				
				// Track the inventory so it saves when closed
				return true;
			}

			if (args[0].equalsIgnoreCase("generateitem")) {
				Player p = (Player) sender;
				// TODO: ITEM VALIDATION
				//if(!Item.itemExists(args[1])) { return false; }

				// TODO: MAKE UTIL ITEMS SKILLPROVIDERS WITH NO EQUIP ABILITY
				if(args.length == 2) {
					//p.getInventory().addItem(Item.giveItem(args[1], null));
					return true;
				} 
				if(args.length == 3) {
					if(!Validator.teirExists(args[2])) {return false; }

					p.getInventory().addItem(Generator.generateItem(args[1], 1, ItemTier.valueOf(args[2])));
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
			
			if(args[0].equalsIgnoreCase("swordskills")) {
				SCOPlayer s;
				if(args.length > 0) {
					s = GameManager.findSCOPlayerByName(args[1]);
					if(s == null) { sender.sendMessage(ChatColor.RED + "Could not find player"); return true; }
				}else {
					s = GameManager.findSCOPlayer((Player) sender);
				}
				
				sender.sendMessage(s.getName() + "'s currently active Sword Skills");
				for(SwordSkill skill : s.getSwordSkillManager().getSkills()) {
					sender.sendMessage("- " + skill.getProvider().getName());
				}
				return true;
			}

		}
		return false;
	}

}
