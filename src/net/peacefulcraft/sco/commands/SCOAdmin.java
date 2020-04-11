package net.peacefulcraft.sco.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.peacefulcraft.sco.SwordCraftOnline;
import net.peacefulcraft.sco.gamehandle.GameManager;
import net.peacefulcraft.sco.gamehandle.player.SCOPlayer;
import net.peacefulcraft.sco.inventories.InventoryType;
import net.peacefulcraft.sco.inventories.SwordSkillInventory;
import net.peacefulcraft.sco.items.ItemTier;
import net.peacefulcraft.sco.mythicmobs.drops.DropManager;
import net.peacefulcraft.sco.mythicmobs.drops.LootBag;
import net.peacefulcraft.sco.mythicmobs.mobs.ActiveMob;
import net.peacefulcraft.sco.mythicmobs.mobs.Centipede;
import net.peacefulcraft.sco.particles.Effect;
import net.peacefulcraft.sco.swordskills.SwordSkill;
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
				SwordSkillInventory inv = (SwordSkillInventory) GameManager.findSCOPlayer(p).getInventory(InventoryType.SWORD_SKILL);
				
				inv.openInventory();
				
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
					p.sendMessage(ChatColor.RED + "Could not find player. Are they online?");
					return true;
				}

				if(args.length > 2) {
					if(args[2].equalsIgnoreCase("inventory")) {
						
						if(args.length > 3) {
							if(args[3].equalsIgnoreCase("swordskill")) {
								p.openInventory(s.getInventoryManager().getInventory(InventoryType.SWORD_SKILL).getInventory());
								return true;
							} else {
								p.sendMessage(ChatColor.GOLD + "Valid agruments" + ChatColor.RED + " swordskill");
								return true;
							}
						}
						
					} else {
						p.sendMessage(ChatColor.GOLD + "Valid arguments:" + ChatColor.RED + " inventory");
						return true;
					}
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

			final String helpMessage = "Mythic Mob Commands: \nLoadDropTables: Refreshes droptables from config. \n" +
			"LoadMobs: Refreshes mobs from config. \nSpawn { Mob Internal }: Spawns mob on player location. \n" +
			"KillAll: Kills all active MythicMobs on server. \n" +
			"List { Mobs or DropTables }: Lists all loaded mobs or droptables. \n" +
			"GenerateDropTable { DropTable Internal }: Drops lootbag on player or simulates in console. \n" +
			"GetData { DropTable Internal or Mob Internal }: Retrieves data of loaded droptable or mob. \n";
			
			/**
			 * Handling for MM commands
			 */
			if(args[0].equalsIgnoreCase("mm")) {
				//Help command. Keep updated with options.
				if(args.length == 1 || args[1].equalsIgnoreCase("help")) {
					sender.sendMessage(helpMessage);
					return true;
				}

				//Reloads droptables then mobs.
				//Prevents null pointer errors on reloading mobs before droptables.
				if(args[1].equalsIgnoreCase("reload")) {
					//Despawns all active mobs
					SwordCraftOnline.getPluginInstance().getMobManager().removeAllMobs();
					SwordCraftOnline.getPluginInstance().getDropManager().loadDropTables();
					SwordCraftOnline.getPluginInstance().getMobManager().loadMobs();
					return true;
				}

				//Spawns instance of activemob on players location
				if(args[1].equalsIgnoreCase("spawn")) {
					if(!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.GREEN + "Cannot perform command from console.");
						return true;
					}
					Player p = (Player)sender;
					if(SwordCraftOnline.getPluginInstance().getMobManager().getMMList().keySet().contains(args[2])) {
						ActiveMob am = SwordCraftOnline.getPluginInstance().getMobManager().spawnMob(args[2], p.getLocation());
						if(am != null) {
							sender.sendMessage(ChatColor.GREEN + "Spawned " + args[2]);
							SwordCraftOnline.logInfo("Spawned " + args[2]);
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "Error Loading " + args[2] + "Active Mob Instance Null.");
						SwordCraftOnline.logInfo("[MOB SPAWN] Active Mob Instance Null");
						return true;
					}
					sender.sendMessage(ChatColor.GREEN + "File for " + args[2] + " Not Found.");
					SwordCraftOnline.logInfo("[MOB SPAWN] Not found: " + args[2]);
					return true;
				}

				//Kills all active instances of mobs
				if(args[1].equalsIgnoreCase("killall")) {
					int amount = SwordCraftOnline.getPluginInstance().getMobManager().removeAllMobs();
					if(sender instanceof Player) {
						sender.sendMessage(ChatColor.GREEN + "Removed " + amount + " Mythic Mobs!");
					}
					SwordCraftOnline.logInfo("[MOB KILL] Removed " + amount + " Mythic Mobs.");
					return true;
				}

				//Lists all loaded mobs or droptables
				if(args[1].equalsIgnoreCase("list")) {
					if(args[2].equalsIgnoreCase("mobs")) {
						List<ActiveMob> mobs = new ArrayList<ActiveMob>(SwordCraftOnline.getPluginInstance().getMobManager().getActiveMobs());
						sender.sendMessage(ChatColor.GREEN + "There are: " + mobs.size() + " Active Mobs.");
						if(mobs.size() > 0) {
							String l = ChatColor.GREEN + "Mobs: \n";
							for(int i = 1; i < mobs.size(); i++) {
								l += ChatColor.GREEN + "" + i + ". " + mobs.get(i).getDisplayName() + "\n";
							}
							sender.sendMessage(ChatColor.GREEN + l);
							return true;
						}
						return true;
					} else if (args[2].equalsIgnoreCase("droptables")) {
						Set<String> tables = SwordCraftOnline.getPluginInstance().getDropManager().getDroptableMap().keySet();
						String s = ChatColor.GREEN + "DropTables: \n";
						for(String ss : tables) {
							s += ChatColor.GREEN + ss + "\n";
						}
						sender.sendMessage(s);
						return true;
					}
				}

				//Generates droptable in console or player location
				if(args[1].equalsIgnoreCase("generatedroptable")) {
					//Sender is console. Simulate lootbag in console.
					if(!(sender instanceof Player)) {
						if(SwordCraftOnline.getPluginInstance().getDropManager().isInDropTable(args[2])) {
							SwordCraftOnline.logInfo("Simulating lootbag in console...");
							try{
								LootBag d = SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[2]).generate();
								sender.sendMessage(d.getInfo());
							} catch(Exception e) {
								SwordCraftOnline.logInfo("Specified Droptable has invalid drops.");
								e.printStackTrace();
							}
							return true;
						}
						//Droptable not loaded.
						SwordCraftOnline.logInfo("Attempted to load invalid droptable.");
						return true;
					}

					//Sender is player. Drop actual lootbag on player.
					Player p = (Player) sender;
					if(SwordCraftOnline.getPluginInstance().getDropManager().isInDropTable(args[2])) {
						SCOPlayer s = GameManager.findSCOPlayer(p);
						if(s == null) {
							p.sendMessage(ChatColor.GREEN + "Join SCO to generate droptable.");
							SwordCraftOnline.logInfo("Failed to generate droptable. Sender not in SCO.");
							return true;
						}
						SwordCraftOnline.logInfo("Loading lootbag from manager...");
						String info = "";
						try{
							LootBag d = SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[2]).generate(s);
							info = d.getInfo();
							DropManager.drop(p.getLocation(), d);
						} catch(NullPointerException e) {
							p.sendMessage(ChatColor.GREEN + "Specified droptable has invalid drops");
						}
						p.sendMessage(ChatColor.GREEN + "Generated droptable.");
						SwordCraftOnline.logInfo("Lootbag successully loaded from manager.");
						sender.sendMessage("LootBag Info: " + info);
						return true;
					}
					p.sendMessage(ChatColor.GREEN + "Specified droptable not loaded.");
					SwordCraftOnline.logInfo("Attempted to load invalid droptable.");
					return true;
				}

				//Gets data contained in yml file for mobs and droptables
				if(args[1].equalsIgnoreCase("getdata")) {
					if(args[2].equalsIgnoreCase("Droptable")) {
						sender.sendMessage(SwordCraftOnline.getPluginInstance().getDropManager().getDropTable(args[3]).getInfo());
						return true;
					}
					//TODO: Add support for mob
				}
			}

			if(args[0].equalsIgnoreCase("particletest")) {
				Player p = (Player)sender;
				if(args[1].isEmpty()) {
					p.sendMessage("Please enter valid particle.");
				}
				SwordCraftOnline.logInfo("Loading " + args[1] + " particle...");
				Effect effect = SwordCraftOnline.getEffectManager().getEffectByClassName(args[1]);
				if(effect == null) { 
					SwordCraftOnline.logSevere("Attempted to load invalid particle.");
					return true;
				}
				//effect.setTargetPlayer(p);
				effect.setLocation(p.getLocation());
				effect.start();
				SwordCraftOnline.logInfo("Particle started...");

				return true;
			}

			if(args[0].equalsIgnoreCase("centipedetest")){
				Player p = (Player)sender;
				if(args[1].equalsIgnoreCase("spawn")) {
					int size = 3;
					int repeat = 20;
					try{ 
						size = Integer.valueOf(args[2]);
					} catch(Exception ex) { SwordCraftOnline.logDebug("No size value set."); }
					try{
						repeat = Integer.valueOf(args[3]);
					} catch(Exception exx) { SwordCraftOnline.logDebug("No repeatition value set."); }

					Centipede test = new Centipede("Test", size, repeat);
					test.spawn(p.getLocation());
					if(!(test.setTarget(p))) {
						p.sendMessage("Failed to set target.");
					}
					return true;		
				}	
				if(args[1].equalsIgnoreCase("remove")) {
					SwordCraftOnline.getPluginInstance().getMobManager().removeAllCentipede();
					return true;
				}	
			}
		}
		return false;
	}

}
